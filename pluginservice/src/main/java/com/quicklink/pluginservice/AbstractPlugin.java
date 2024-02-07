package com.quicklink.pluginservice;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPlugin {

  public @NotNull Optional<Logger> getLogger() {
    if(debugMode) {
      return Optional.ofNullable(pluginLogger);
    }
    return Optional.empty();
  }

  public abstract void onEnable();

  /* INTERNAL */

  private final String name;
  private final String version;
  private final Logger pluginLogger;

  private final ClassLoader classLoader;

  private boolean enabled;
  private final List<Parameter> parameters;

  private boolean debugMode = true;

  public @NotNull String getName() {
    return name;
  }

  public @NotNull String getVersion() {
    return version;
  }

  @Internal
  public AbstractPlugin(@NotNull String name, @NotNull String version, KeyParam<?>... keys) {
    this.name = name;
    this.version = version;
    this.pluginLogger = LoggerFactory.getLogger("Plugin %sv%s".formatted(name, version));

    classLoader = this.getClass().getClassLoader();
    if (!(getClass().getClassLoader() instanceof PluginClassLoader)) {
      throw new IllegalStateException("Plugin requires " + PluginClassLoader.class.getName());
    }
    ((PluginClassLoader) classLoader).initialize(this);

    enabled = false;

    // add keys
    parameters = new ArrayList<>();
    parameters.addAll(Arrays.stream(keys).map(KeyParam::asParameter).toList());
  }

  public @Internal boolean isEnabled() {
    return enabled;
  }

  public @Internal void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public @Internal @NotNull ClassLoader getClassLoader() {
    return classLoader;
  }

  public @Internal @NotNull List<Parameter> getParameters() {
    return parameters;
  }

  public @Internal void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }
}