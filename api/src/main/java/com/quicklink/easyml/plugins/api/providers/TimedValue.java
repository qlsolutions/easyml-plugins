/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;


import java.time.Instant;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * TimedValue - A certain value at specific instant.
 *
 * @author Denis Mehilli
 */
public final class TimedValue {

  private long millis;
  private double value;

  /**
   *
   */
  public TimedValue(long millis, double value) {
    this.millis = millis;
    this.value = value;
  }

  public TimedValue(@NotNull Instant instant, double value) {
    this(instant.toEpochMilli(), value);
  }

  public long getMillis() {
    return millis;
  }

  public double getValue() {
    return value;
  }

  public void setMillis(long millis) {
    this.millis = millis;
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
    return this.millis == that.millis &&
        Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(millis, value);
  }

  @Override
  public String toString() {
    return "TimedValue[" +
        "millis=" + millis + ", " +
        "value=" + value + ']';
  }


}
