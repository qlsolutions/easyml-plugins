package com.quicklink.webserver;

import com.quicklink.pluginservice.KeyParam;
import com.quicklink.pluginservice.hooks.HookContext;
import com.quicklink.pluginservice.hooks.HookPlugin;

public class WebServerPlugin extends HookPlugin {

  static KeyParam<Double> tolerance_high = KeyParam.of("tolerance-high", 0D);
  static KeyParam<Double> tolerance_low = KeyParam.of("tolerance-low", 0D);
  static KeyParam<String> addr = KeyParam.of("tolerance-low", "173.125.1.21");
  static KeyParam<Integer> port = KeyParam.of("host-port", 8856);
  static KeyParam<String> protocol = KeyParam.of("protocol", "http");
  static KeyParam<String> template_to_send = KeyParam.of("template-to-send", """
      {
          "modelId": {model},
          "timestamp": {timestamp},
          "status": {anomalyStatus},
          "predicted": {predicted},
          "observed": {observed},
      }
      """);


  public WebServerPlugin() {
    super(
        "WebServer",
        "1.0",
        tolerance_high,
        tolerance_low,
        addr,
        port,
        protocol,
        template_to_send
    );
  }

  @Override
  public void run(HookContext hookContext) {
    getLogger().info("Runned web server plugin");
  }

  @Override
  public void onEnable() {

  }
}
