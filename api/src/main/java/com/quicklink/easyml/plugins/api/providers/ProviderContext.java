/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;


import com.quicklink.easyml.plugins.api.Parameter;
import com.quicklink.easyml.plugins.api.ParameterImpl;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ProviderContext - Context received by easmly-app.
 *
 * @author Denis Mehilli
 */
public class ProviderContext {

  private final ProviderPlugin plugin;
  private final UUID providerId;
  private final String providerName;
  private final Map<String, ?> parameters;

  public ProviderContext(ProviderPlugin plugin, @NotNull UUID providerId, @NotNull String providerName, @NotNull Map<String, ?> parameters) {
    this.plugin = plugin;
    this.providerId = providerId;
    this.providerName = providerName;
    this.parameters = parameters;
  }

  public UUID providerId() {
    return providerId;
  }

  public @NotNull String providerName() {
    return providerName;
  }

  public @Nullable <T> T param(@NotNull Parameter<T> key) {
    return (T) parameters.getOrDefault(key.getKey(), null);
  }

}
