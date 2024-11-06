/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api;

import com.quicklink.easyml.plugins.api.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * EasyMLPlugins - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 17/10/2024
 */
public final class EasyML {
  private EasyML() {

  }

  private static Server server;
  private static JsonMapper jsonMapper;

  public static @NotNull Server getServer() {
    if(server == null) {
      throw new IllegalStateException("Error retrieving server from plugin");
    }
    return server;
  }

  public static @NotNull JsonMapper getJsonMapper() {
    if(jsonMapper == null) {
      throw new IllegalStateException("Error retrieving jsonMapper from plugin");
    }
    return jsonMapper;
  }
}
