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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Internal
public final class ParameterImpl implements Parameter {

  @JsonIgnore
  private Map<UUID, Object> current_values = null;

  private String key;
  private Type type;
  @JsonProperty("defaultValue")
  private Object defaultValue;
  private AccessType accessType;
  Map<String, Object> extra;

  @JsonIgnore
  private Map<Locale, ParamLang> lang = null;

  public @Nullable Map<Flags, Object> parseExtra() {
    if (extra == null) {
      return null;
    }

    Map<Flags, Object> flags = new LinkedHashMap<>();

    extra.forEach((k, v) -> {
      flags.put(Flags.valueOf(k), v);
    });
    return flags;
  }

  @NotNull
  @Override
  public Object get(@NotNull UUID id) {
    if(current_values == null) {
      throw new RuntimeException("parameter '%s' not created from Parameter.builder".formatted(key));
    }
    if(!current_values.containsKey(id)) {
      throw new RuntimeException("parameter '%s' not initialized".formatted(key));
    }
    return current_values.get(id);
  }

  public void updateValue(@NotNull UUID id, @NotNull Object value) {
    current_values.put(id, value);
  }

}