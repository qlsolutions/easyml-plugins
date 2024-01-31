package com.quicklink.pluginservice;


import java.util.Objects;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class KeyParam<T> {

  public static KeyParam<Double> of(@NotNull String id, double defaultValue, @NotNull String description) {
    return new KeyParam<>(id, defaultValue, description, "float64");
  }
  public static KeyParam<Double> of(@NotNull String id, double defaultValue) {
    return new KeyParam<>(id, defaultValue, "", "float64");
  }

  public static KeyParam<Integer> of(@NotNull String id,  int defaultValue, @NotNull String description) {
    return new KeyParam<>(id, defaultValue, description, "int");
  }
  public static KeyParam<Integer> of( @NotNull String id, int defaultValue) {
    return new KeyParam<>(id, defaultValue, "", "int");
  }

  public static KeyParam<String> of(@NotNull String id, @NotNull String defaultValue, @NotNull String description) {
    return new KeyParam<>(id, defaultValue, description, "string");
  }
  public static KeyParam<String> of(@NotNull String id, @NotNull String defaultValue) {
    return new KeyParam<>(id, defaultValue, "", "string");
  }

  public static KeyParam<String> ofSecret(@NotNull String id, @NotNull String defaultValue, @NotNull String description) {
    return new KeyParam<>(id, defaultValue, description, "secret");
  }
  public static KeyParam<String> ofSecret(@NotNull String id, @NotNull String defaultValue) {
    return new KeyParam<>(id, defaultValue, "", "secret");
  }


  private static final Pattern keyPattern = Pattern.compile("[a-zA-Z-]+");

  private static void isValidKey(String key) {
    if (!keyPattern.matcher(key).matches()) {
      throw new IllegalStateException("Invalid parameter key '%s'".formatted(key));
    }
  }

  private final String id;
  private final T defaultValue;
  private final String description;
  private final String type;

  private KeyParam(@NotNull String id, @NotNull T defaultValue, @NotNull String description, @NotNull String type) {
    this.id = id;
    this.defaultValue = defaultValue;
    this.description = description;
    this.type = type;

    isValidKey(id);
  }

  public String getId() {
    return id;
  }

  public Parameter asParameter() {
    return new Parameter(id, type, defaultValue, description);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KeyParam<?> keyParam = (KeyParam<?>) o;
    return Objects.equals(id,
        keyParam.id) && Objects.equals(defaultValue, keyParam.defaultValue)
        && Objects.equals(description, keyParam.description) && Objects.equals(
        type, keyParam.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, defaultValue, description, type);
  }

  @Override
  public String toString() {
    return "KeyParam{" +
        "id='" + id + '\'' +
        ", defaultValue=" + defaultValue +
        ", description='" + description + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
