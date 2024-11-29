package com.quicklink.sma.utils;

/**
 * Utils - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 08/10/2024
 */
public class Utils {

  public static String toSerieId(String setType, String serie) {
    return setType + "_" + serie;
  }

  public static String setTypeFromSerieId(String serieId) {
    return serieId.split("_")[0];
  }

  public static String serieFromSerieId(String serieId) {
    return serieId.split("_")[1];
  }

  public static String capitalize(String str) {
    if (str == null || str.length() <= 1) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }
}
