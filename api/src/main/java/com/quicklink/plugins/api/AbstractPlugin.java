package com.quicklink.plugins.api;


import java.util.Optional;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public abstract class AbstractPlugin {

  private final String name;
  private final String version;

  @Internal
  public final PrivateConfig pvt;

  @Internal
  public AbstractPlugin(@NotNull String name, @NotNull String version, KeyParam<?>... keys) {
    this.name = name;
    this.version = version;
    this.pvt = new PrivateConfig(this, this.getClass().getClassLoader(), keys);
  }

  public abstract void onEnable();

  public @NotNull Optional<Logger> getLogger() {
    if(pvt.debugMode) {
      return Optional.ofNullable(pvt.logger);
    }
    return Optional.empty();
  }

  public @NotNull String getName() {
    return name;
  }

  public @NotNull String getVersion() {
    return version;
  }

}