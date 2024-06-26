package com.quicklink.easyml.plugins.api.providers;

import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record Serie(@NotNull String id, @NotNull String diplayName, @NotNull List<String> tags) {

  public Serie(@NotNull String id, @NotNull String diplayName, String... tags) {
    this(id, diplayName, Arrays.asList(tags));
  }
}
