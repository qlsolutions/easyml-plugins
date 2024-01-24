package com.quicklink.pluginservice;


import com.quicklink.parameters.api.KeyParam;
import com.quicklink.parameters.api.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;

public abstract class DPPlugin {

  public @NotNull Logger getLogger() {
    return pluginLogger;
  }

  public abstract void onEnable();

  public abstract Collection<Serie> getSeries(DPContext ctx);

  public abstract List<Record> getSerieData(DPContext ctx, String serieId, long startTs, long endTs);

  public abstract About status(DPContext ctx);

  public List<Record> getFutureData(DPContext ctx, String serieId, long startTs, long endTs) {
    throw notImplementedExc;
  }

  /* INTERNAL */
  public static final RuntimeException notImplementedExc = new RuntimeException();
  private final ClassLoader classLoader;
  DPPluginDescription pluginDescription;
  Logger pluginLogger;
  private boolean enabled;

  private final List<Parameter> parameters;

  @Internal
  public DPPlugin(int limit, String limitDescription, KeyParam<?>... keys) {
    classLoader = this.getClass().getClassLoader();
    if (!(getClass().getClassLoader() instanceof DPPluginClassLoader)) {
      throw new IllegalStateException("Plugin requires " + DPPluginClassLoader.class.getName());
    }
    ((DPPluginClassLoader) classLoader).initialize(this);
    enabled = false;

    // add keys
    parameters = new ArrayList<>();
    parameters.add(new Parameter("limit", "int", limit, limitDescription));
    parameters.addAll(Arrays.stream(keys).map(KeyParam::asParameter).toList());
  }

  @Internal
  public boolean isEnabled() {
    return enabled;
  }

  @Internal
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Internal
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  @Internal
  public DPPluginDescription getPluginDescription() {
    return pluginDescription;
  }

  @Internal
  public List<Parameter> getParameters() {
    return parameters;
  }
}