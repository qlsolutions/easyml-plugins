package com.quicklink.hookemail;

import com.quicklink.pluginservice.KeyParam;
import com.quicklink.pluginservice.hooks.HookContext;
import com.quicklink.pluginservice.hooks.HookPlugin;
import java.security.Key;
import org.jetbrains.annotations.NotNull;

public class HookEmailPlugin extends HookPlugin {

  static KeyParam<Double> tolerance_high = KeyParam.of("tolerance-high", 0D);
  static KeyParam<Double> tolerance_low = KeyParam.of("tolerance-low", 0D);
  static KeyParam<String> from_name = KeyParam.of("From (name)", "Quicklink EasyML Server");
  static KeyParam<String> from_address = KeyParam.of("From (address)", "easyml@qlsol.com");
  static KeyParam<String> to_name = KeyParam.of("To (name)", "Info Quicklink");
  static KeyParam<String> to_address = KeyParam.of("To (address)", "info@qlsol.com");

  static KeyParam<String> object_start = KeyParam.of("Mail object on anomaly start", "[EasyML] anomaly detected! (score{model}, score={predicted})");
  static KeyParam<String> content_start = KeyParam.of("Mail content on anomaly start",
      "Anomaly detected by EasyML! model={model}, date={timestamp}, predicted={predicted}, max-predicted={max-predicted}, min-predicted={min-predicted}, observed={observed}");

  static KeyParam<String> object_end = KeyParam.of("Mail object on anomaly end", "[EasyML] anomaly end! (score{model}, score={predicted})");
  static KeyParam<String> content_end = KeyParam.of("Mail content on anomaly end",
      "Anomaly end! model={model}, date={timestamp}, predicted={predicted}, max-predicted={max-predicted}, min-predicted={min-predicted}, observed={observed}");



  public HookEmailPlugin() {
    super("Email", "1.0.0");
  }

  @Override
  public void run(HookContext ctx) {
    if(ctx.getStatus().equalsIgnoreCase("started")) {

    } else {

    }
  }

  @Override
  public void onEnable() {

  }
}
