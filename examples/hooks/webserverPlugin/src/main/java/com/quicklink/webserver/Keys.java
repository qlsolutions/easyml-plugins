package com.quicklink.webserver;

import com.quicklink.pluginservice.KeyParam;

public final class Keys {
  static KeyParam<Double> tolerance_high = KeyParam.of("tolerance-high", 0D);
  static KeyParam<Double> tolerance_low = KeyParam.of("tolerance-low", 0D);
  static KeyParam<String> addr = KeyParam.of("addr", "173.125.1.21");
  static KeyParam<String> protocol = KeyParam.of("protocol", "HTTP", "Options: HTTP/HTTPS");
  static KeyParam<String> template_to_send = KeyParam.of("template-to-send", """
      {
          "modelId": {model},
          "timestamp": {timestamp},
          "status": {anomalyStatus},
          "score": {predicted},
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
}
