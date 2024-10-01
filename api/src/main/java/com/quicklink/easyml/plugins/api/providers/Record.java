/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Record - A certain value at specific instant.
 *
 * @author Denis Mehilli
 */
public final class Record {

  private @NotNull Long timestamp;
  private final @NotNull Double value;

  /**
   *
   */
  public Record(@NotNull Long timestamp, @NotNull Double value) {
    this.timestamp = timestamp;
    this.value = value;
  }

  public @NotNull Long timestamp() {
    return timestamp;
  }

  public @NotNull Double value() {
    return value;
  }

  public void timestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (Record) obj;
    return Objects.equals(this.timestamp, that.timestamp) &&
        Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, value);
  }

  @Override
  public String toString() {
    return "Record[" +
        "timestamp=" + timestamp + ", " +
        "value=" + value + ']';
  }


}
