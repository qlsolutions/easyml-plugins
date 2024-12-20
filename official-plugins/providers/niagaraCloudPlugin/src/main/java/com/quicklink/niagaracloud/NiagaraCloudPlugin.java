package com.quicklink.niagaracloud;

import static com.quicklink.niagaracloud.Keys.*;

import com.quicklink.easyml.plugins.api.providers.ProviderPlugin;
import com.quicklink.easyml.plugins.api.providers.Serie;
import com.quicklink.easyml.plugins.api.providers.TimedValue;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * NiagaraCloudPlugin - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 27/11/2024
 */
public class NiagaraCloudPlugin extends ProviderPlugin {

  private final Map<UUID, NiagaraCloudClient> cacheAccess = new ConcurrentHashMap<>();


  public NiagaraCloudPlugin() {
    super("NiagaraCloud", "1.0.0", CLIENT_ID, CLIENT_SECRET, CUSTOMER_ID, HOST_ID);
  }

  @Override
  public void onCreate(@NotNull UUID providerId) {
    NiagaraCloudClient client = cacheAccess.computeIfAbsent(providerId, uuid -> new NiagaraCloudClient(providerId));

  }

  @Override
  public @NotNull List<Serie> getSeries(@NotNull UUID providerId) {
    NiagaraCloudClient client = cacheAccess.computeIfAbsent(providerId, uuid -> new NiagaraCloudClient(providerId));


    try {
      return client.getSeries();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @NotNull LinkedList<TimedValue> getSerieData(@NotNull UUID providerId, @NotNull String serieId, @NotNull Instant start, @NotNull Instant end) {
    NiagaraCloudClient client = cacheAccess.computeIfAbsent(providerId, uuid -> new NiagaraCloudClient(providerId));
    try {
      return client.getSerieData(serieId, DateTimeFormatter.ISO_INSTANT.format(start), DateTimeFormatter.ISO_INSTANT.format(end));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean status(@NotNull UUID id) {
    return true;
  }

  @Override
  public void onEnable() {
    getLogger().info("Loaded");
  }
}
