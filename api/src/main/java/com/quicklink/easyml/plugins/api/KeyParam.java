package com.quicklink.easyml.plugins.api;


import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

public class KeyParam<T> {

  public enum Type {
    DOUBLE("float64"),
    INT("int"),
    STRING("string"),
    SECRET("secret");

    private final String name;

    Type(String name) {
      this.name = name;
    }
  }

  @AvailableSince(value = "0.1.0rc2")
  public static Builder create(@NotNull String name) {
    return new Builder(name);
  }

  public static class Builder<T> {
    private final String id;
    private Object defaultValue;
    private String description;
    private Type type;

    public Builder(@NotNull String id) {
      this.id = id;
    }


    public Builder<T> defaultValue(@NotNull String defaultValue) {
      this.defaultValue = defaultValue;
      this.type = Type.STRING;
      return this;
    }

    public Builder<T> defaultValue(double defaultValue) {
      this.defaultValue = defaultValue;
      this.type = Type.DOUBLE;
      return this;
    }

    public Builder<T> defaultValue(int defaultValue) {
      this.defaultValue = defaultValue;
      this.type = Type.INT;
      return this;
    }

    public Builder<T> description(@NotNull Locale language, @NotNull String description) {
      this.description = description;
      return this;
    }

    public Builder<T> secret() {
      this.type = Type.SECRET;
      return this;
    }

    public KeyParam<T> build() {
      return  new KeyParam<T>(id, (T) defaultValue, description, type.name);
    }
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

  public @NotNull String getId() {
    return id;
  }

  public @NotNull Parameter asParameter() {
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
