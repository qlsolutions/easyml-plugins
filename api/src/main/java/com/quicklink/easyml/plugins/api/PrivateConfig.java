package com.quicklink.easyml.plugins.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateConfig {

  @NotNull public final AbstractPlugin plugin;
  @NotNull final Optional<Logger> logger;
  @NotNull public final ClassLoader classLoader;
  @NotNull public final List<Parameter> parameters;

  public boolean enabled = false;
  public boolean debugMode = false;


  public PrivateConfig(@NotNull AbstractPlugin plugin, @NotNull ClassLoader cl, Parameter<?>... keys) {
    this.plugin = plugin;
    this.logger = Optional.ofNullable(LoggerFactory.getLogger("Plugin %sv%s".formatted(plugin.getName(), plugin.getVersion())));
    this.classLoader = cl;
    if (!(classLoader instanceof PluginClassLoader)) {
      throw new IllegalStateException("Plugin requires " + PluginClassLoader.class.getName());
    }
    ((PluginClassLoader) classLoader).initialize(plugin);

    // add keys
    parameters = new ArrayList<>();
    parameters.addAll(Arrays.stream(keys).toList());
  }

}
