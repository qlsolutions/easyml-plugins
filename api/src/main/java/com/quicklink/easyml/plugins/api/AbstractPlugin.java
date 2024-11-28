/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api;


import java.util.List;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * AbstractPlugin - Plugin abstract class, extended by HookPlugin, ProviderPlugin.
 *
 * @author Denis Mehilli
 */
public abstract class AbstractPlugin {

  @Internal
  public final PrivateConfig pvt;
  private final String name;
  private final String version;

  @Internal
  public AbstractPlugin(@NotNull String name, @NotNull String version, @NotNull List<Parameter<?>> keys) {
    this.name = name;
    this.version = version;
    this.pvt = new PrivateConfig(this, this.getClass().getClassLoader(), keys);
  }

  public abstract void onEnable();

  public @NotNull Logger getLogger() {
    return pvt.logger;
  }

  public @NotNull String getName() {
    return name;
  }

  public @NotNull String getVersion() {
    return version;
  }

}