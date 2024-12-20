/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.webserver;

import static com.quicklink.webserver.Keys.addr;
import static com.quicklink.webserver.Keys.authenticationType;
import static com.quicklink.webserver.Keys.password;
import static com.quicklink.webserver.Keys.protocol;
import static com.quicklink.webserver.Keys.requestMethod;
import static com.quicklink.webserver.Keys.template_to_send;
import static com.quicklink.webserver.Keys.tolerance_high;
import static com.quicklink.webserver.Keys.tolerance_low;
import static com.quicklink.webserver.Keys.username;

import com.quicklink.easyml.plugins.api.hooks.HookContext;
import com.quicklink.easyml.plugins.api.hooks.HookPlugin;
import java.io.IOException;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

/**
 * WebServerPlugin - Plugin entrypoint.
 *
 * @author Denis Mehilli
 */
public class WebServerPlugin extends HookPlugin {

  public static final MediaType JSON = MediaType.get("application/json");

  public WebServerPlugin() {
    super(
        "WebServer",
        "1.0.0",
        tolerance_high,
        tolerance_low,
        addr,
        protocol,
        template_to_send,
        requestMethod,
        authenticationType,
        username,
        password
    );
  }

  @Override
  public void onCreate(@NotNull HookContext hookContext) {

  }

  @Override
  public void run(@NotNull HookContext ctx) {
    var url = "%s://%s".formatted(ctx.param(protocol).toLowerCase(), ctx.param(addr));

    var body = ctx.parseString(ctx.param(template_to_send));

    var client = new OkHttpClient();
    var req = new Request.Builder()
        .url(url)
        .method(ctx.param(requestMethod), RequestBody.create(body, JSON));

    try {
      switch (ctx.param(authenticationType)) {
        case "NONE" -> {
        }
        case "BASIC" -> {
          String credential = Credentials.basic(ctx.param(username), ctx.param(password));
          req.addHeader("Authorization", credential);
        }
        case "BEARER_TOKEN" -> {
          var token = "Bearer " + ctx.param(password);
          req.addHeader("Authorization", token);
        }
        default -> {
          throw new RuntimeException(
              "Invalid authentication type '%s'".formatted(ctx.param(authenticationType)));
        }
      }
      var build = req.build();
      try (var response = client.newCall(build).execute()) {
//        response.body().string();
        getLogger().info("Sent request");
      }
    } catch (IOException e) {
      throw new RuntimeException("Error making the request", e);
    }

  }

  @Override
  public void onEnable() {
    getLogger().info("Loaded");
  }
}
