/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parameter - Plugin's parameters. Supported types: "float64", "int", "string", "bool".
 * Secret types are sensible data(like password/api-key) which will be encrypted in database.
 *
 * @author Denis Mehilli
 */
@JsonInclude(Include.NON_NULL)
public final class Parameter<T> {

  public enum Flags {
    SELECT,
  }

  public enum AccessType {
    ALL,
    READ,
    WRITE,
    INTERNAL
  }

  public static StringBuilder create(@NotNull String name, @NotNull String defaultValue) {
    return new StringBuilder(name, defaultValue);
  }

  public static Builder<Double> create(@NotNull String name, double defaultValue) {
    return new Builder<>(name, defaultValue).type(DOUBLE_TYPE);
  }

  public static Builder<Integer> create(@NotNull String name, int defaultValue) {
    return new Builder<>(name, defaultValue).type(INT_TYPE);
  }

  public static Builder<Boolean> create(@NotNull String name, boolean defaultValue) {
    return new Builder<>(name, defaultValue).type(BOOL_TYPE);
  }


  @Internal
  public static <E> Parameter<E> unsafeParameter(@NotNull String key, @NotNull String type,
      @NotNull E defaultValue, @NotNull AccessType accessType) {
    return new Parameter<>(key, type, defaultValue, accessType);
  }

  public static final String DOUBLE_TYPE = "float64";
  public static final String INT_TYPE = "int";
  public static final String STRING_TYPE = "string";
  public static final String BOOL_TYPE = "bool";

  @JsonProperty("key")
  private String key;

  @JsonProperty("type")
  private String type;

  @JsonProperty("defaultValue")
  private Object defaultValue;

  @JsonProperty("accessType")
  private AccessType accessType;

  @JsonProperty("extra")
  private Map<String, Object> extra;

  @JsonIgnore
  private Map<Locale, ParamLang> lang = null;


  @Internal
  public Parameter() {
  }

  private Parameter(
      @NotNull String key,
      @NotNull String type,
      @NotNull T defaultValue,
      @NotNull AccessType accessType
  ) {
    this.key = key;
    this.type = type;
    this.defaultValue = defaultValue;
    this.accessType = accessType;
  }

  @JsonIgnore
  public boolean isSecret() {
     return type.equals(STRING_TYPE) && accessType == AccessType.WRITE;
  }

  public @NotNull String key() {
    return key;
  }

  public @NotNull String type() {
    return type;
  }

  public @NotNull T defaultValue() {
    return (T) defaultValue;
  }

  public @NotNull AccessType accessType() {
    return accessType;
  }

  public @Nullable Map<Locale, ParamLang> lang() {
    return lang;
  }

  public @Nullable Map<Flags, Object> extra() {
    if (extra == null) {
      return null;
    }

    Map<Flags, Object> flags = new LinkedHashMap<>();

    extra.forEach((k, v) -> {
      flags.put(Flags.valueOf(k), v);
    });
    return flags;
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

  Parameter<T> lang(@NotNull Map<Locale, ParamLang> lang) {
    this.lang = lang;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Parameter<?> parameter = (Parameter<?>) o;
    return Objects.equals(key, parameter.key) && Objects.equals(type, parameter.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, type);
  }

  @Override
  public String toString() {
    return "Parameter{" +
        "key='" + key + '\'' +
        ", type='" + type + '\'' +
        ", defaultValue=" + defaultValue +
        ", accessType=" + accessType +
        ", extra=" + extra +
        ", lang=" + lang +
        '}';
  }

  public static class Builder<E> {

    private static final Pattern keyPattern = Pattern.compile("[a-zA-Z-]+");
    private final String id;
    private final E defaultValue;

    private String type;

    private AccessType accessType = AccessType.ALL;


    private E[] select;
    private final Map<Locale, ParamLang> lang = new LinkedHashMap<>();



    public Builder(@NotNull String id, @NotNull E defaultValue) {
      this.id = id;
      this.defaultValue = defaultValue;

      isValidKey(id);
    }

    private static void isValidKey(String key) {
      if (!keyPattern.matcher(key).matches()) {
        throw new IllegalStateException("Invalid parameter key '%s'".formatted(key));
      }
    }

    @Internal
    Builder<E> type(String type) {
      this.type = type;
      return this;
    }

    public Builder<E> lang(@NotNull Locale language, @NotNull String title,
        @NotNull String description) {
      this.lang.put(language, new ParamLang(title, description));
      return this;
    }

    public Builder<E> select(@NotNull E... values) {
      this.select = values;
      return this;
    }

    public Builder<E> access(@NotNull AccessType accessType) {
      this.accessType = accessType;
      return this;
    }

    public Parameter<E> build() {
      var param = new Parameter<>(id, type, (E) defaultValue, accessType);
      param.lang(lang);


      if (select != null) {
        param.extra = new LinkedHashMap<>();
        param.extra.put(Flags.SELECT.name(), select);
      }

      return param;
    }

  }

  public static class StringBuilder extends Builder<String> {

    public StringBuilder(@NotNull String id, @NotNull String defaultValue) {
      super(id, defaultValue);
      type(STRING_TYPE);
    }

    @Override
    public StringBuilder lang(@NotNull Locale language, @NotNull String title,
        @NotNull String description) {
      return (StringBuilder) super.lang(language, title, description);
    }

    @Override
    public StringBuilder select(@NotNull String... values) {
      return (StringBuilder) super.select(values);
    }

    @Override
    public StringBuilder access(@NotNull AccessType accessType) {
      return (StringBuilder) super.access(accessType);
    }
  }

}