/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api;

import com.quicklink.easyml.plugins.api.hooks.HookPlugin;
import com.quicklink.easyml.plugins.api.providers.ProviderPlugin;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Server - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 17/10/2024
 */
public interface Server {

  <T> boolean updateProviderParameter(@NotNull UUID providerId, @NotNull Parameter<T> key, @NotNull T newValue);

  @Nullable <T extends HookPlugin> T getHookPlugin(@NotNull Class<T> pluginClass);

  @Nullable <T extends ProviderPlugin> T getProviderPlugin(@NotNull Class<T> pluginClass);


}
