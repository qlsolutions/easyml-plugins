/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;


import com.quicklink.easyml.plugins.api.AbstractPlugin;
import com.quicklink.easyml.plugins.api.ParamLang;
import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Collection;
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

  public ProviderPlugin(@NotNull String name, @NotNull String version, int limit,
      @NotNull ParamLang eng, @NotNull ParamLang it, Parameter<?>... keys) {
    super(name, version, genParams(limit, eng, it, keys));
  }

  private static Parameter<?>[] genParams(int limit, @NotNull ParamLang eng, @NotNull ParamLang it,
      Parameter<?>... keys) {
    var arr = new Parameter[keys.length + 1];
    arr[0] = Parameter
        .create("limit", limit)
        .lang(Locale.ENGLISH, eng.title(), eng.description())
        .lang(Locale.ITALIAN, it.title(), it.description())

        .build();
    System.arraycopy(keys, 0, arr, 1, keys.length);
    return arr;
  }

  public abstract @NotNull Collection<Serie> getSeries(
      ProviderContext ctx);

  public abstract @NotNull List<Record> getSerieData(
      ProviderContext ctx, String serieId, long startTs, long endTs);

  public abstract @NotNull About status(ProviderContext ctx);

  public @NotNull List<Record> getFutureData(ProviderContext ctx, String serieId, long startTs,
      long endTs) {
    throw notImplementedExc;
  }
}