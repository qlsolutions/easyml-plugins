package com.quicklink.pluginservice;

import java.net.URL;
import java.net.URLClassLoader;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class PluginClassLoader extends URLClassLoader {

  public PluginClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public abstract AbstractPlugin getPlugin();

  public abstract AbstractPlugin getPluginInit();

  public abstract void setPluginInit(AbstractPlugin pluginInit);

  void initialize(AbstractPlugin plugin) {
    if (this.getPlugin() != null || this.getPluginInit() != null) {
      throw new IllegalArgumentException("Plugin already initialized!");
    }

    this.setPluginInit(plugin);


  }

}