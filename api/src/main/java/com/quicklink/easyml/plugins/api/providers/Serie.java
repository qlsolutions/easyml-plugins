/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;

import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Serie - A "category" of data requested by the plugin.
 *
 * @author Denis Mehilli
 */
public record Serie(@NotNull String id, @NotNull String displayName, @NotNull List<String> tags) {

  public Serie(@NotNull String id, @NotNull String displayName, String... tags) {
    this(id, displayName, Arrays.asList(tags));
  }
}
