package com.quicklink.easyml.plugins.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Parameter<T> {


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


  private @NotNull String key;
  private @NotNull String type;
  private @NotNull T defaultValue;

  @JsonIgnore
  private Map<Locale, ParamLang> lang = null;


  public Parameter(
      @NotNull String key,
      @NotNull String type,
      @NotNull T defaultValue
  ) {
    this.key = key;
    this.type = type;
    this.defaultValue = defaultValue;
  }

  @AvailableSince(value = "0.1.0rc3")
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

  public @Nullable Map<Locale, ParamLang> lang() {
    return lang;
  }

  public Parameter<T> key(@NotNull String key) {
    this.key = key;
    return this;
  }

  public Parameter<T> type(@NotNull String type) {
    this.type = type;
    return this;
  }

  public Parameter<T> defaultValue(@NotNull T defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  @Nullable Parameter<T> lang(@NotNull  Map<Locale, ParamLang> lang) {
    this.lang = lang;
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
        Objects.equals(this.defaultValue, that.defaultValue);
  }
  @Override
  public int hashCode() {
    return Objects.hash(key, type, defaultValue);
  }

  @Override
  public String toString() {
    return "Parameter[" +
        "key=" + key + ", " +
        "type=" + type + ", " +
        "defaultValue=" + defaultValue + ']';
  }


  @AvailableSince(value = "0.1.0rc3")
  public static <T> Builder<T> create(@NotNull String name) {
    return new Builder<T>(name);
  }

  public static class Builder<T> {

    private static final Pattern keyPattern = Pattern.compile("[a-zA-Z-]+");

    private static void isValidKey(String key) {
      if (!keyPattern.matcher(key).matches()) {
        throw new IllegalStateException("Invalid parameter key '%s'".formatted(key));
      }
    }


    private final String id;
    private Object defaultValue;
    private Type type;

    private Map<Locale, ParamLang> lang = null;

    public Builder(@NotNull String id) {
      this.id = id;
      isValidKey(id);
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

    public Builder<T> lang(@NotNull Locale language, @NotNull String title, @NotNull String description) {
      this.lang.put(language, new ParamLang(title, description));
      return this;
    }

    public Builder<T> secret() {
      this.type = Type.SECRET;
      return this;
    }

    public Parameter<T> build() {
      var param = new Parameter<>(id, type.name(), (T) defaultValue);
      param.lang(lang);
      return param;
    }
  }
}