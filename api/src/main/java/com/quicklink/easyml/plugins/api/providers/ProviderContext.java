/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;


import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ProviderContext - Context received by easmly-app.
 *
 * @author Denis Mehilli
 */
public class ProviderContext {

  private final ProviderPlugin plugin;
  private final int providerId;
  private final String providerName;
  private final Map<String, ?> parameters;

  @Nullable
  private final Long startTs;
  @Nullable
  private final Long endTs;

  public ProviderContext(ProviderPlugin plugin, int providerId, @NotNull String providerName, @NotNull Map<String, ?> parameters,
      @Nullable Long startTs, @Nullable Long endTs) {
    this.plugin = plugin;
    this.providerId = providerId;
    this.providerName = providerName;
    this.parameters = parameters;
    this.startTs = startTs;
    this.endTs = endTs;
  }

  public ProviderContext( ProviderPlugin plugin, int idApp, @NotNull String nameApp, @NotNull Map<String, ?> parameters) {
    this(plugin, idApp, nameApp, parameters, null, null);
  }

  public int providerId() {
    return providerId;
  }

  public @NotNull String providerName() {
    return providerName;
  }

  public @Nullable <T> T param(@NotNull Parameter<T> key) {
    return (T) parameters.getOrDefault(key.key(), null);
  }

  public int limit() {
    if(!plugin.isLimitParameterSupported()) {
      throw new UnsupportedOperationException("This plugin does not support the limit parameter");
    }
    return (int) parameters.get("limit");
  }

  public Stream<DateRange> dateRangeStream(int field) {
    if (startTs == null || endTs == null) {
      throw new UnsupportedOperationException("Start ts or end ts not set");
    }
    Calendar start = Calendar.getInstance();
    start.setTime(new Date(startTs));

    var iterator = new DateRangeIterator(start, new Date(endTs), limit(), field);
    return StreamUtils.asStream(iterator);
  }

}
