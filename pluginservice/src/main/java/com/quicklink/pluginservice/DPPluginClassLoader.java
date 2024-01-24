package com.quicklink.pluginservice;

import java.net.URL;
import java.net.URLClassLoader;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.LoggerFactory;

@Internal
public abstract class DPPluginClassLoader extends URLClassLoader {

  public DPPluginClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public abstract DPPluginDescription getPluginDescription();

  public abstract DPPlugin getPlugin();

  public abstract DPPlugin getPluginInit();

  public abstract void setPluginInit(DPPlugin pluginInit);

  void initialize(DPPlugin plugin) {
    if (this.getPlugin() != null || this.getPluginInit() != null) {
      throw new IllegalArgumentException("Plugin already initialized!");
    }

    this.setPluginInit(plugin);

    plugin.pluginDescription = getPluginDescription();
    plugin.pluginLogger = LoggerFactory.getLogger(
        "Plugin %sv%s".formatted(getPluginDescription().name(), getPluginDescription().version()));

  }

}