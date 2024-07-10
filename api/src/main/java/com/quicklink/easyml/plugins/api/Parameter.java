package com.quicklink.easyml.plugins.api;

import com.quicklink.easyml.plugins.api.KeyParam.Type;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

public final class Parameter {

  private @NotNull String key;
  private @NotNull String type;
  private @NotNull Object defaultValue;
  private @NotNull String description;

  public Parameter(
      @NotNull String key,
      @NotNull String type,
      @NotNull Object defaultValue,
      @NotNull String description
  ) {
    this.key = key;
    this.type = type;
    this.defaultValue = defaultValue;
    this.description = description;
  }

  @AvailableSince(value = "0.1.0rc2")
  public boolean isSecret() {
    return type.equals(Type.SECRET.name());
  }

  public @NotNull String key() {
    return key;
  }

  public @NotNull String type() {
    return type;
  }

  public @NotNull Object defaultValue() {
    return defaultValue;
  }

  public @NotNull String description() {
    return description;
  }

  public Parameter key(@NotNull String key) {
    this.key = key;
    return this;
  }

  public Parameter type(@NotNull String type) {
    this.type = type;
    return this;
  }

  public Parameter defaultValue(@NotNull Object defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  public Parameter description(@NotNull String description) {
    this.description = description;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (Parameter) obj;
    return Objects.equals(this.key, that.key) &&
        Objects.equals(this.type, that.type) &&
        Objects.equals(this.defaultValue, that.defaultValue) &&
        Objects.equals(this.description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, type, defaultValue, description);
  }

  @Override
  public String toString() {
    return "Parameter[" +
        "key=" + key + ", " +
        "type=" + type + ", " +
        "defaultValue=" + defaultValue + ", " +
        "description=" + description + ']';
  }


}