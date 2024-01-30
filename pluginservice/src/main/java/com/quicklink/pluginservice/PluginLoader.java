package com.quicklink.pluginservice;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface PluginLoader {
  Class<?> getClassByName(final String name);

  void setClass(final String name, final Class<?> clazz);
}
