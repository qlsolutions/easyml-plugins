/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;


import com.quicklink.easyml.plugins.api.AbstractPlugin;
import com.quicklink.easyml.plugins.api.Parameter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

/**
 * ProviderPlugin - Class to be implemented by a Provider plugin.
 *
 * @author Denis Mehilli
 */
public abstract class ProviderPlugin extends AbstractPlugin {

  /* INTERNAL */
  public static final RuntimeException notImplementedExc = new RuntimeException();

  private final boolean limitParameterSupported;

  public ProviderPlugin(@NotNull String name, @NotNull String version, int weeksLimit, Parameter<?>... keys) {
    super(name, version, genParams(weeksLimit, keys));
    limitParameterSupported = true;
  }

  public ProviderPlugin(@NotNull String name, @NotNull String version, Parameter<?>... keys) {
    super(name, version, Arrays.asList(keys));
    limitParameterSupported = false;
  }

  public boolean isLimitParameterSupported() {
    return limitParameterSupported;
  }

  private static List<Parameter<?>> genParams(int limit, Parameter<?>... keys) {
    var list = new ArrayList<Parameter<?>>();

    list.add(Parameter
        .create("limit", limit)
        .lang(Locale.ENGLISH, "Limit","Weeks limit for request")
        .lang(Locale.ITALIAN, "Limite", "Limite di settimane per richiesta")
        .build());

    list.addAll(Arrays.asList(keys));
    return list;
  }

  public abstract void onCreate(@NotNull ProviderContext ctx);

  public abstract @NotNull List<Serie> getSeries(
      ProviderContext ctx);

  public abstract @NotNull LinkedList<TimedValue> getSerieData(
      @NotNull ProviderContext ctx, @NotNull String serieId,
      @NotNull Instant start,
      @NotNull Instant end);

  public abstract @NotNull About status(@NotNull ProviderContext ctx);

  public @NotNull LinkedList<TimedValue> getFutureData(@NotNull ProviderContext ctx, @NotNull String serieId,
      @NotNull Instant start, @NotNull Instant end) {
    throw notImplementedExc;
  }


}