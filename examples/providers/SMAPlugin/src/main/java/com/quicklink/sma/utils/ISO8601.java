package com.quicklink.sma.utils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * ISO8601 - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 03/10/2024
 */
public class ISO8601 {

  public static Instant parse(String s) {
    TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(s);
    Instant i = Instant.from(ta);
    return i;
  }

  public static String format(Instant instant) {

    DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

    return formatter.format(instant);
  }

}
