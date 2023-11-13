package com.quicklink.pluginservice;


import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;

public abstract class Plugin {

  public @NotNull Logger getLogger() {
    return pluginLogger;
  }

  public abstract void onEnable();

  public abstract ParametersMap getDefaultParameters();

  public abstract Collection<Serie> getSeries(Context ctx);

  public abstract List<Record> getSerieData(Context ctx, String serieId, long startTs, long endTs);

  public abstract About status(Context ctx);

  public List<Record> getFutureData(Context ctx, String serieId, long startTs, long endTs) {
    throw notImplementedExc;
  }

  /* INTERNAL */
  public static final RuntimeException notImplementedExc = new RuntimeException();
  private final ClassLoader classLoader;
  PluginDescription pluginDescription;
  Logger pluginLogger;
  private boolean enabled;

  @Internal
  public Plugin() {
    classLoader = this.getClass().getClassLoader();
    if (!(getClass().getClassLoader() instanceof PluginClassLoader)) {
      throw new IllegalStateException("Plugin requires " + PluginClassLoader.class.getName());
    }
    ((PluginClassLoader) classLoader).initialize(this);
    enabled = false;
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
  public PluginDescription getPluginDescription() {
    return pluginDescription;
  }

}