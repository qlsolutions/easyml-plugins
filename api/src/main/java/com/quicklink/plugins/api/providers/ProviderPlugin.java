package com.quicklink.plugins.api.providers;



import com.quicklink.plugins.api.AbstractPlugin;
import com.quicklink.plugins.api.KeyParam;
import java.util.Collection;
import java.util.List;

public abstract class ProviderPlugin extends AbstractPlugin {

  public abstract Collection<Serie> getSeries(
      ProviderContext ctx);

  public abstract List<Record> getSerieData(
      ProviderContext ctx, String serieId, long startTs, long endTs);

  public abstract About status(ProviderContext ctx);

  public List<Record> getFutureData(ProviderContext ctx, String serieId, long startTs, long endTs) {
    throw notImplementedExc;
  }

  /* INTERNAL */
  public static final RuntimeException notImplementedExc = new RuntimeException();

  public ProviderPlugin(String name, String version, int limit, String limitDescription, KeyParam<?>... keys) {
    super(name, version, genParams(limit, limitDescription, keys));
  }

  private static KeyParam<?>[] genParams(int limit, String limitDescription, KeyParam<?>... keys) {
    var arr = new KeyParam[keys.length + 1];
    arr[0] = KeyParam.of("limit", limit, limitDescription);
    System.arraycopy(keys, 0, arr, 1, keys.length);
    return arr;
  }
}