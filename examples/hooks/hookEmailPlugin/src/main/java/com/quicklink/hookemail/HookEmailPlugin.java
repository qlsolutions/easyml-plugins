package com.quicklink.hookemail;

import com.quicklink.pluginservice.KeyParam;
import com.quicklink.pluginservice.hooks.HookContext;
import com.quicklink.pluginservice.hooks.HookPlugin;
import org.jetbrains.annotations.NotNull;

public class HookEmailPlugin extends HookPlugin {

  static KeyParam<Double> tolerance_high = KeyParam.of("tolerance-high", 0D);
  static KeyParam<Double> tolerance_low = KeyParam.of("tolerance-low", 0D);

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
