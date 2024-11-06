/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.json;

import java.io.InputStream;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;

/**
 * JsonMapper - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 06/11/2024
 */
public interface JsonMapper {

  @NotNull
  default String toJsonString(@NotNull Object obj) {
    throw new NotImplementedException("JsonMapper#toJsonString not implemented");
  }

  @NotNull
  default String toJsonString(@NotNull Object obj, @NotNull Type type) {
    throw new NotImplementedException("JsonMapper#toJsonString not implemented");
  }

  @NotNull
  default InputStream toJsonStream(@NotNull Object obj, @NotNull Type type) {
    throw new NotImplementedException("JsonMapper#toJsonStream not implemented");
  }

  @NotNull
  default <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
    throw new NotImplementedException("JsonMapper#fromJsonString not implemented");
  }

  @NotNull
  default <T> T fromJsonString(@NotNull String json, @NotNull Class<T> targetType) {
    throw new NotImplementedException("JsonMapper#fromJsonString not implemented");
  }

  @NotNull
  default <T> T fromJsonStream(@NotNull InputStream json, @NotNull Type targetType) {
    throw new NotImplementedException("JsonMapper#fromJsonString not implemented");
  }



}
