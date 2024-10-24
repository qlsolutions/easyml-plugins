/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;


import com.quicklink.easyml.plugins.api.Parameter;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
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
  private final UUID providerId;
  private final String providerName;
  private final Map<String, ?> parameters;

  @Nullable
  private final Instant start;
  @Nullable
  private final Instant end;

  public ProviderContext(ProviderPlugin plugin, @NotNull UUID providerId, @NotNull String providerName, @NotNull Map<String, ?> parameters,
      @Nullable Instant start, @Nullable Instant end) {
    this.plugin = plugin;
    this.providerId = providerId;
    this.providerName = providerName;
    this.parameters = parameters;
    this.start = start;
    this.end = end;
  }

  public ProviderContext( ProviderPlugin plugin, UUID providerId, @NotNull String nameApp, @NotNull Map<String, ?> parameters) {
    this(plugin, providerId, nameApp, parameters, null, null);
  }

  public UUID providerId() {
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
    if (start == null || end == null) {
      throw new UnsupportedOperationException("Start ts or end ts not set");
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(start.toEpochMilli());


    var iterator = new DateRangeIterator(calendar, new Date(end.toEpochMilli()), limit(), field);
    return StreamUtils.asStream(iterator);
  }

}
