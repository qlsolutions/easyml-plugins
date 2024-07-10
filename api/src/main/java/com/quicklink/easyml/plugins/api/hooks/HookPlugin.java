package com.quicklink.easyml.plugins.api.hooks;

import com.quicklink.easyml.plugins.api.AbstractPlugin;
import com.quicklink.easyml.plugins.api.Parameter;
import org.jetbrains.annotations.NotNull;

public abstract class HookPlugin extends AbstractPlugin {
  public abstract void run(@NotNull HookContext ctx);

  public HookPlugin(@NotNull String name, @NotNull String version, Parameter<?>... keys) {
    super(name, version, keys);
  }

}
