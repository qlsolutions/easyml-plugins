package com.quicklink.plugins.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateConfig {

  public final AbstractPlugin plugin;
  final Logger logger;
  public final ClassLoader classLoader;
  public final List<Parameter> parameters;

  public boolean enabled = false;
  public boolean debugMode = false;


  public PrivateConfig(AbstractPlugin plugin, ClassLoader cl, KeyParam<?>... keys) {
    this.plugin = plugin;
    this.logger = LoggerFactory.getLogger("Plugin %sv%s".formatted(plugin.getName(), plugin.getVersion()));
    this.classLoader = cl;
    if (!(classLoader instanceof PluginClassLoader)) {
      throw new IllegalStateException("Plugin requires " + PluginClassLoader.class.getName());
    }
    ((PluginClassLoader) classLoader).initialize(plugin);

    // add keys
    parameters = new ArrayList<>();
    parameters.addAll(Arrays.stream(keys).map(KeyParam::asParameter).toList());
  }

}
