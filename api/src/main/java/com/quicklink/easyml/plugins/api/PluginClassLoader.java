/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api;

import java.net.URL;
import java.net.URLClassLoader;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PluginClassLoader - Plugin class loader.
 *
 * @author Denis Mehilli
 */
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