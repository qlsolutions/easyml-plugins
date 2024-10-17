/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Server - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 17/10/2024
 */
public interface Server {

  <T> boolean updateHookParameter(int hookId, @NotNull Parameter<T> key, @Nullable T newValue);

  <T> boolean updateProviderParameter(int providerId, @NotNull Parameter<T> key, @Nullable T newValue);

}
