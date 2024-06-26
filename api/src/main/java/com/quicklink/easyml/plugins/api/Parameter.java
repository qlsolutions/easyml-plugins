package com.quicklink.easyml.plugins.api;

import java.io.Serializable;
import org.jetbrains.annotations.NotNull;

public record Parameter(
    @NotNull String key,
    @NotNull String type,
    @NotNull Object defaultValue,
    @NotNull String description
) implements Serializable {

  @Override
  protected @NotNull Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

}