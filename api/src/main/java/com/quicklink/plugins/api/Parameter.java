package com.quicklink.plugins.api;

import java.io.Serializable;

public record Parameter(
    String key,
    String type,
    Object defaultValue,
    String description
) implements Serializable {

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

}