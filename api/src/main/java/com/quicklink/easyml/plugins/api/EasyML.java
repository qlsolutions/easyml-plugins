/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api;

import com.quicklink.easyml.plugins.api.json.JsonMapper;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

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
  private static OkHttpClient httpClient;

  private static void init(@NotNull Server server, @NotNull JsonMapper jsonMapper, @NotNull OkHttpClient httpClient) {
    EasyML.server = server;
    EasyML.jsonMapper = jsonMapper;
    EasyML.httpClient = httpClient;
  }

  public static @NotNull Server getServer() {
    if (server == null) {
      throw new IllegalStateException("Error retrieving server from plugin");
    }
    return server;
  }

  public static @NotNull JsonMapper getJsonMapper() {
    if (jsonMapper == null) {
      throw new IllegalStateException("Error retrieving jsonMapper from plugin");
    }
    return jsonMapper;
  }

  public static @NotNull OkHttpClient getHttpClient() {
    if(httpClient == null) {
      throw new IllegalStateException("Error retrieving httpClient from plugin");
    }
    return httpClient;
  }
}
