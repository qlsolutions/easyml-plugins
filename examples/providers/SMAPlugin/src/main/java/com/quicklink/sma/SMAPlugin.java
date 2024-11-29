package com.quicklink.sma;

import com.quicklink.easyml.plugins.api.providers.About;
import com.quicklink.easyml.plugins.api.providers.ProviderContext;
import com.quicklink.easyml.plugins.api.providers.ProviderPlugin;
import com.quicklink.easyml.plugins.api.providers.Serie;
import com.quicklink.easyml.plugins.api.providers.TimedValue;
import com.quicklink.sma.client.SMAClient;
import com.quicklink.sma.client.SMAClient.Mode;
import com.quicklink.sma.client.model.DataResponse.SetValue;
import com.quicklink.sma.utils.ISO8601;
import com.quicklink.sma.utils.Utils;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * SMAPlugin - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 03/10/2024
 */
public class SMAPlugin extends ProviderPlugin {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private Map<UUID, SMAClient> cacheAccess;

  public SMAPlugin() {
    super("Smart Energy Management", "1.0.0", 1,
        Keys.CLIENT_ID, Keys.CLIENT_SECRET, Keys.PLANT_ID, Keys.DEVICE_ID, Keys.MODE, Keys.EMAIL);
  }

  @Override
  public void onCreate(@NotNull ProviderContext providerContext) {

  }

  @Override

  public @NotNull List<Serie> getSeries(ProviderContext ctx) {
    var CLIENT_ID = ctx.param(Keys.CLIENT_ID);
    var CLIENT_SECRET = ctx.param(Keys.CLIENT_SECRET);
    var DEVICE_ID = ctx.param(Keys.DEVICE_ID);
    var MODE = ctx.param(Keys.MODE);
    var EMAIL = ctx.param(Keys.EMAIL);

    Mode mode;
    try {
      mode = Mode.valueOf(MODE);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid SMAClient Mode '%s'".formatted(MODE));
    }

    // get cached client or create new
    var client = cacheAccess.computeIfAbsent(ctx.providerId(),
        id -> new SMAClient(getLogger(), CLIENT_ID, CLIENT_SECRET, mode));
    if (!client.clientId.equals(CLIENT_ID) || !client.clientSecret.equals(CLIENT_SECRET)) {
      // updated client id or client secret?
      client = new SMAClient(getLogger(), CLIENT_ID, CLIENT_SECRET, mode);
      cacheAccess.put(ctx.providerId(), client);
    }

    var status = client.getStatus(EMAIL);
    var permissionState = status.state();
    if(permissionState.equals("Pending")) {
      throw new RuntimeException("Permission not accepted yet, current state is '%s'".formatted(permissionState));
    }
    if (!permissionState.equals("Accepted")) {

      var emailSentStatus = client.sendEmail(EMAIL);

      if(!emailSentStatus.state().equals("Pending")) {
        throw new RuntimeException("Permission email sent, but state was expected to be 'Pending' but is '%s'".formatted(emailSentStatus.state()));
      }

      throw new RuntimeException("Permission not sent yet, current state is '%s'".formatted(permissionState));
    }


    var sets = client.getSets(DEVICE_ID);

    List<Serie> series = new ArrayList<>();
    for (var setType : sets.sets()) {

      for (var serie : SMASetType.valueOf(setType).series) {
        series.add(new Serie(Utils.toSerieId(setType, serie), Utils.capitalize(serie)));
      }
    }

    return series;
  }


  @Override
  public @NotNull LinkedList<TimedValue> getSerieData(ProviderContext ctx, @NotNull String serieId,
      @NotNull Instant startTs,
      @NotNull Instant endTs) {

    var CLIENT_ID = ctx.param(Keys.CLIENT_ID);
    var CLIENT_SECRET = ctx.param(Keys.CLIENT_SECRET);
    var DEVICE_ID = ctx.param(Keys.DEVICE_ID);
    var MODE = ctx.param(Keys.MODE);
    var EMAIL = ctx.param(Keys.EMAIL);

    Mode mode;
    try {
      mode = Mode.valueOf(MODE);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("SMAClient Mode '%s'".formatted(MODE));
    }

    // get cached client or create new
    var client = cacheAccess.computeIfAbsent(ctx.providerId(),
        id -> new SMAClient(getLogger(), CLIENT_ID, CLIENT_SECRET, mode));
    if (!client.clientId.equals(CLIENT_ID) || !client.clientSecret.equals(CLIENT_SECRET)) {
      // updated client id or client secret?
      client = new SMAClient(getLogger(), CLIENT_ID, CLIENT_SECRET, mode);
      cacheAccess.put(ctx.providerId(), client);
    }

    var status = client.getStatus(EMAIL);

    var permissionState = status.state();
    if(permissionState.equals("Pending")) {
      throw new RuntimeException("Permission not accepted yet, current state is '%s'".formatted( permissionState));
    }
    if (!permissionState.equals("Accepted")) {
      var emailSentStatus = client.sendEmail(EMAIL);

      if(!emailSentStatus.state().equals("Pending")) {
        throw new RuntimeException("Permission email sent, but state was expected to be 'Pending' but is '%s'".formatted(emailSentStatus.state()));
      }

      throw new RuntimeException("Permission not sent yet, current state is '%s'".formatted( permissionState));
    }


    return getData(serieId, startTs, endTs, client, DEVICE_ID);
  }

  @Override
  public @NotNull About status(@NotNull ProviderContext ctx) {
    return new About(true, "hostId", "1.0.0");
  }

  @Override
  public void onEnable() {
    cacheAccess = new ConcurrentHashMap<>();
    getLogger().info("Loaded");
  }


  @NotNull
  private LinkedList<TimedValue> getData(String serieId, Instant start, Instant end, SMAClient client,
      String deviceId) {
    var setType = SMASetType.valueOf(Utils.setTypeFromSerieId(serieId));
    var serie = Utils.serieFromSerieId(serieId);

    var startWeekDate = formatter.format(start.atZone(ZoneOffset.UTC).toLocalDate());
    var setsValues = client.getData(serieId, setType, deviceId, startWeekDate);

    if (setsValues.set().isEmpty()) {
      throw new RuntimeException("Device '%s': Serie '%s' of setType '%s' has no data in %s".formatted(
          deviceId, serie, setType, ISO8601.format(start)));
    }

    var list = new LinkedList<TimedValue>();

    for (SetValue setValue : setsValues.set()) {
      Double value = setValue.unknown().getOrDefault(serie, null);
      if (value == null) {
        continue;
      }
      Instant time = ISO8601.parse(setValue.time() + "Z");
      if(time.isAfter(end)) {
        continue;
      }
      list.add(new TimedValue(time, value));
    }

    return list;
  }
}
