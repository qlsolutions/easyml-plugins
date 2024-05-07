package com.quicklink.plugins.api;

import java.net.URL;
import java.net.URLClassLoader;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal
public abstract class PluginClassLoader extends URLClassLoader {

  public PluginClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public abstract @NotNull AbstractPlugin getPlugin();

  public abstract @Nullable AbstractPlugin getPluginInit();

  public abstract void setPluginInit(@NotNull AbstractPlugin pluginInit);

  void initialize(@NotNull AbstractPlugin plugin) {
    if (this.getPluginInit() != null) {
      throw new IllegalArgumentException("Plugin already initialized!");
    }

    this.setPluginInit(plugin);
  }

}