package com.quicklink.pluginservice.hooks;

import com.quicklink.pluginservice.AbstractPlugin;
import com.quicklink.pluginservice.KeyParam;
import org.jetbrains.annotations.NotNull;

public abstract class HookPlugin extends AbstractPlugin {
  public abstract void run(HookContext ctx);

  public HookPlugin(@NotNull String name, @NotNull String version, KeyParam<?>... keys) {
    super(name, version, keys);
  }

}
