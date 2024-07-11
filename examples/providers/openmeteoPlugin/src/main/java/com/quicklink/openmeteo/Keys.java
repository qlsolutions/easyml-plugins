package com.quicklink.openmeteo;


import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Locale;

public final class Keys {

  static Parameter<String> LATITUDE = Parameter
      .create("latitude", "0")
      .lang(Locale.ENGLISH, "latitude", "Location latitude")
      .lang(Locale.ITALIAN, "latitudine", "Latitudine località")
      .build();

  static Parameter<String> LONGITUDE = Parameter
      .create("longitude", "0")
      .lang(Locale.ENGLISH, "longitude", "Location longitude")
      .lang(Locale.ITALIAN, "longitudine", "Longitudine località")
      .build();

  static Parameter<String> API_KEY = Parameter
      .create("apiKey", "")
      .secret()
      .lang(Locale.ENGLISH, "api key", "Key obtained from https://open-meteo.com/en/docs")
      .lang(Locale.ITALIAN, "chiave api", "Chiave ottenuta da https://open-meteo.com/en/docs")
      .build();


}
