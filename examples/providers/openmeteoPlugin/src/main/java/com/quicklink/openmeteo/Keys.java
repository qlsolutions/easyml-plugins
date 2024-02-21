package com.quicklink.openmeteo;

import com.quicklink.pluginservice.KeyParam;

public final class Keys {
  static KeyParam<String> LATITUDE = KeyParam.of("latitude", "0");
  static KeyParam<String> LONGITUDE = KeyParam.of("longitude", "0");
  static KeyParam<String> API_KEY = KeyParam.ofSecret("apiKey", "",
      "Key obtained from https://open-meteo.com/en/docs");

}
