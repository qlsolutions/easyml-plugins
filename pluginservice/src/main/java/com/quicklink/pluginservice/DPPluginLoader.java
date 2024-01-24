package com.quicklink.pluginservice;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface DPPluginLoader {
  Class<?> getClassByName(final String name);

  void setClass(final String name, final Class<?> clazz);
}
