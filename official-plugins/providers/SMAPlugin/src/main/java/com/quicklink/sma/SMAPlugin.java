package com.quicklink.sma;

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
  public void onCreate(@NotNull UUID providerId) {

  }

  @Override
  public @NotNull List<Serie> getSeries(@NotNull UUID providerId) {

    // get cached client or create new
    var client = cacheAccess.computeIfAbsent(providerId, id -> new SMAClient(providerId, getLogger()));

    var status = client.getStatus();
    var permissionState = status.state();
    if (permissionState.equals("Pending")) {
      throw new RuntimeException(
          "Permission not accepted yet, current state is '%s'".formatted(permissionState));
    }
    if (!permissionState.equals("Accepted")) {

      var emailSentStatus = client.sendEmail();

      if (!emailSentStatus.state().equals("Pending")) {
        throw new RuntimeException(
            "Permission email sent, but state was expected to be 'Pending' but is '%s'".formatted(
                emailSentStatus.state()));
      }

      throw new RuntimeException(
          "Permission not sent yet, current state is '%s'".formatted(permissionState));
    }

    var sets = client.getSets();

    List<Serie> series = new ArrayList<>();
    for (var setType : sets.sets()) {

      for (var serie : SMASetType.valueOf(setType).series) {
        series.add(new Serie(Utils.toSerieId(setType, serie), Utils.capitalize(serie)));
      }
    }

    return series;
  }


  @Override
  public @NotNull LinkedList<TimedValue> getSerieData(@NotNull UUID providerId,
      @NotNull String serieId,
      @NotNull Instant startTs,
      @NotNull Instant endTs) {

    // get cached client or create new
    var client = cacheAccess.computeIfAbsent(providerId,
        id -> new SMAClient(providerId, getLogger()));

    var status = client.getStatus();

    var permissionState = status.state();
    if (permissionState.equals("Pending")) {
      throw new RuntimeException(
          "Permission not accepted yet, current state is '%s'".formatted(permissionState));
    }
    if (!permissionState.equals("Accepted")) {
      var emailSentStatus = client.sendEmail();

      if (!emailSentStatus.state().equals("Pending")) {
        throw new RuntimeException(
            "Permission email sent, but state was expected to be 'Pending' but is '%s'".formatted(
                emailSentStatus.state()));
      }

      throw new RuntimeException(
          "Permission not sent yet, current state is '%s'".formatted(permissionState));
    }

    return getData(serieId, startTs, endTs, client);
  }

  @Override
  public boolean status(@NotNull UUID providerId) {
    return true;
  }

  @Override
  public void onEnable() {
    cacheAccess = new ConcurrentHashMap<>();
    getLogger().info("Loaded");
  }


  @NotNull
  private LinkedList<TimedValue> getData(String serieId, Instant start, Instant end, SMAClient client) {
    var setType = SMASetType.valueOf(Utils.setTypeFromSerieId(serieId));
    var serie = Utils.serieFromSerieId(serieId);

    var startWeekDate = formatter.format(start.atZone(ZoneOffset.UTC).toLocalDate());
    var setsValues = client.getData(serieId, setType, startWeekDate);

    if (setsValues.set().isEmpty()) {
      throw new RuntimeException(
          "Device '%s': Serie '%s' of setType '%s' has no data in %s".formatted(
              Keys.DEVICE_ID.get(client.providerId), serie, setType, ISO8601.format(start)));
    }

    var list = new LinkedList<TimedValue>();

    for (SetValue setValue : setsValues.set()) {
      Double value = setValue.unknown().getOrDefault(serie, null);
      if (value == null) {
        continue;
      }
      Instant time = ISO8601.parse(setValue.time() + "Z");
      if (time.isAfter(end)) {
        continue;
      }
      list.add(new TimedValue(time, value));
    }

    return list;
  }
}
