package com.quicklink.plugins.api.hooks;

import com.quicklink.plugins.api.AbstractPlugin;
import com.quicklink.plugins.api.KeyParam;
import org.jetbrains.annotations.NotNull;

public abstract class HookPlugin extends AbstractPlugin {
  public abstract void run(@NotNull HookContext ctx);

  public HookPlugin(@NotNull String name, @NotNull String version, KeyParam<?>... keys) {
    super(name, version, keys);
  }

}
