package com.quicklink.easyml.plugins.api.providers;



import com.quicklink.easyml.plugins.api.AbstractPlugin;
import com.quicklink.easyml.plugins.api.KeyParam;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public abstract class ProviderPlugin extends AbstractPlugin {

  public abstract @NotNull Collection<Serie> getSeries(
      ProviderContext ctx);

  public abstract @NotNull List<Record> getSerieData(
      ProviderContext ctx, String serieId, long startTs, long endTs);

  public abstract @NotNull About status(ProviderContext ctx);

  public @NotNull List<Record> getFutureData(ProviderContext ctx, String serieId, long startTs, long endTs) {
    throw notImplementedExc;
  }

  /* INTERNAL */
  public static final RuntimeException notImplementedExc = new RuntimeException();

  public ProviderPlugin(@NotNull String name, @NotNull String version, int limit, @NotNull String limitDescription, KeyParam<?>... keys) {
    super(name, version, genParams(limit, limitDescription, keys));
  }

  private static KeyParam<?>[] genParams(int limit, @NotNull String limitDescription, KeyParam<?>... keys) {
    var arr = new KeyParam[keys.length + 1];
    arr[0] = KeyParam.of("limit", limit, limitDescription);
    System.arraycopy(keys, 0, arr, 1, keys.length);
    return arr;
  }
}