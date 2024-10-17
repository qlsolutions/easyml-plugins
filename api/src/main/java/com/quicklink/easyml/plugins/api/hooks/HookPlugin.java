/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.hooks;

import com.quicklink.easyml.plugins.api.AbstractPlugin;
import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

/**
 * HookPlugin - Class to be implemented by a Hook plugin.
 *
 * @author Denis Mehilli
 */
public abstract class HookPlugin extends AbstractPlugin {

  public HookPlugin(@NotNull String name, @NotNull String version, Parameter<?>... keys) {
    super(name, version, Arrays.asList(keys));
  }

  public abstract void onCreate(@NotNull HookContext ctx);

  public abstract void run(@NotNull HookContext ctx);

}
