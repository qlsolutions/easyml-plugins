package com.quicklink.webserver;

import static com.quicklink.webserver.Keys.*;

import com.quicklink.plugins.api.hooks.HookContext;
import com.quicklink.plugins.api.hooks.HookPlugin;
import java.io.IOException;
import java.util.Base64;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

public class WebServerPlugin extends HookPlugin {

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
  public static final MediaType JSON = MediaType.get("application/json");
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
        case "NONE" -> {}
        case "BASIC" -> {
          String credential = Credentials.basic(ctx.param(username), ctx.param(password));
          req.addHeader("Authorization", credential);
        }
        case "BEARER_TOKEN" -> {
          var token = "Bearer " + ctx.param(password);
          req.addHeader("Authorization", token);
        }
        default -> {
          throw  new RuntimeException("Invalid authentication type '%s'".formatted(ctx.param(authenticationType)));
        }
      }
      var build = req.build();
      try (var response = client.newCall(build).execute()) {
//        response.body().string();
        getLogger().ifPresent(logger -> logger.info("Sent request"));
      }
    } catch (IOException e) {
      getLogger().ifPresent(logger -> logger.error("Error making the request", e));
    }

  }

  @Override
  public void onEnable() {
    getLogger().ifPresent(logger -> logger.info("Loaded"));
  }
}
