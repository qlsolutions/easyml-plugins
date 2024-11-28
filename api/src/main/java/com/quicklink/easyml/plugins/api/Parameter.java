/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * Parameter - Plugin's parameters. Supported types: "float64", "integer", "string", "bool".
 *
 * @author Denis Mehilli
 */
public interface Parameter<E> {

  @NotNull String getKey();

  @NotNull E get(@NotNull UUID id);

  @NotNull Type getType();

  @NotNull AccessType getAccessType();

  static StringBuilder create(@NotNull String name, @NotNull String defaultValue) {
    return new StringBuilder(name, defaultValue);
  }

  static Builder<Double> create(@NotNull String name, double defaultValue) {
    return new Builder<>(name, defaultValue).type(Type.float64);
  }

  static Builder<Integer> create(@NotNull String name, int defaultValue) {
    return new Builder<>(name, defaultValue).type(Type.integer);
  }

  static Builder<Boolean> create(@NotNull String name, boolean defaultValue) {
    return new Builder<>(name, defaultValue).type(Type.bool);
  }

  enum Flags {
    select,
  }

  enum AccessType {
    read_write,
    read_only,
    write_only,
    internal
  }

  enum Type {
    float64,
    integer,
    string,
    bool
  }

  class Builder<E> {

    private static final Pattern keyPattern = Pattern.compile("[a-zA-Z-]+");
    private final String id;
    private final E defaultValue;

    private Type type;

    private AccessType accessType = AccessType.read_write;


    private E[] select;
    private final Map<Locale, ParamLang> lang = new LinkedHashMap<>();


    Builder(@NotNull String id, @NotNull E defaultValue) {
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
    Builder<E> type(Type type) {
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
      var param = new ParameterImpl(new ConcurrentHashMap<>(), id, type, (E) defaultValue, accessType, null, lang);

      if (select != null) {
        param.setExtra(new LinkedHashMap<>());
        param.getExtra().put(Flags.select.name(), select);
      }

      return param;
    }

  }

  class StringBuilder extends Builder<String> {

    public StringBuilder(@NotNull String id, @NotNull String defaultValue) {
      super(id, defaultValue);
      type(Type.string);
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
