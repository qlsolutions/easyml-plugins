package com.quicklink.webserver;

import com.quicklink.pluginservice.KeyParam;
import com.quicklink.pluginservice.hooks.HookContext;
import com.quicklink.pluginservice.hooks.HookPlugin;
import java.io.IOException;
import java.util.Base64;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class WebServerPlugin extends HookPlugin {

  static KeyParam<Double> tolerance_high = KeyParam.of("tolerance-high", 0D);
  static KeyParam<Double> tolerance_low = KeyParam.of("tolerance-low", 0D);
  static KeyParam<String> addr = KeyParam.of("addr", "173.125.1.21");
  static KeyParam<String> protocol = KeyParam.of("protocol", "HTTP", "Options: HTTP/HTTPS");
  static KeyParam<String> template_to_send = KeyParam.of("template-to-send", """
      {
          "modelId": {model},
          "timestamp": {timestamp},
          "status": {anomalyStatus},
          "max-predicted": {max-predicted},
          "min-predicted": {min-predicted},
          "observed": {observed},
      }
      """);

  static KeyParam<String> requestMethod = KeyParam.of("request-method", "POST",
      "Options: GET, POST, PUT, PATCH, OPTIONS, HEAD, DELETE");
  static KeyParam<String> authenticationType = KeyParam.of("authentication-type", "NONE",
      "Options: NONE, BASIC, BEARER_TOKEN");
  static KeyParam<String> username = KeyParam.of("username", "");
  static KeyParam<String> password = KeyParam.of("password", "");


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
  public void run(HookContext ctx) {
    var url = "%s://%s".formatted(ctx.param(protocol).toLowerCase(), ctx.param(addr));

    var body = ctx.param(template_to_send)
        .replaceAll("\\{model}", "" + ctx.getModelId())
        .replaceAll("\\{timestamp}", "" + ctx.getTimestamp())
        .replaceAll("\\{anomalyStatus}", ctx.getStatus())
        .replaceAll("\\{max-predicted}", "" + ctx.getMaxPredicted())
        .replaceAll("\\{min-predicted}","" + ctx.getMinPredicted())
        .replaceAll("\\{observed}", "" + ctx.getObserver())
        ;

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
