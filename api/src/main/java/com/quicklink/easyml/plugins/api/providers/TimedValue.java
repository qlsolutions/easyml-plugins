/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;


import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * TimedValue - A certain value at specific instant.
 *
 * @author Denis Mehilli
 */
public final class TimedValue {

  private @NotNull String time;
  private double value;

  /**
   * Main Constructor
   */
  public TimedValue(@NotNull String time, double value) {
    this.time = time;
    this.value = value;
  }

  public TimedValue(@NotNull Instant time, double value) {
    this(DateTimeFormatter.ISO_INSTANT.format(time), value);
  }

  public TimedValue(long timeMillis, double value) {
    this(Instant.ofEpochMilli(timeMillis), value);
  }

  public @NotNull String getTime() {
    return time;
  }

  public double getValue() {
    return value;
  }

  public void setTime(@NotNull String time) {
    this.time = time;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (TimedValue) obj;
    return Objects.equals(this.time, that.time) &&
        Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, value);
  }

  @Override
  public String toString() {
    return "TimedValue[" +
        "time=" + time + ", " +
        "value=" + value + ']';
  }


}
