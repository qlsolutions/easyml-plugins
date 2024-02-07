package com.quicklink.pluginservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateConfig {

  public final AbstractPlugin plugin;
  final Logger logger;
  private final ClassLoader classLoader;

  public boolean enabled = false;
  public boolean debugMode = false;

  private final List<Parameter> parameters;

  public PrivateConfig(AbstractPlugin plugin, KeyParam<?>... keys) {
    this.plugin = plugin;
    this.logger = LoggerFactory.getLogger("Plugin %sv%s".formatted(plugin.getName(), plugin.getVersion()));
    this.classLoader = this.getClass().getClassLoader();
    if (!(classLoader instanceof PluginClassLoader)) {
      throw new IllegalStateException("Plugin requires " + PluginClassLoader.class.getName());
    }
    ((PluginClassLoader) classLoader).initialize(plugin);

    // add keys
    parameters = new ArrayList<>();
    parameters.addAll(Arrays.stream(keys).map(KeyParam::asParameter).toList());
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

}