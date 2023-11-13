package com.quicklink.pluginservice;

import java.net.URL;
import java.net.URLClassLoader;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.LoggerFactory;

@Internal
public abstract class PluginClassLoader  extends URLClassLoader {

  public PluginClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public abstract PluginDescription getPluginDescription();

  public abstract Plugin getPlugin();

  public abstract Plugin getPluginInit();

  public abstract void setPluginInit(Plugin pluginInit);

  void initialize(Plugin plugin) {
    if (this.getPlugin() != null || this.getPluginInit() != null) {
      throw new IllegalArgumentException("Plugin already initialized!");
    }

    this.setPluginInit(plugin);

    plugin.pluginDescription = getPluginDescription();
    plugin.pluginLogger = LoggerFactory.getLogger(
        "Plugin %sv%s".formatted(getPluginDescription().name(), getPluginDescription().version()));

  }

}