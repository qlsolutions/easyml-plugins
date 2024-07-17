/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.openmeteo;


import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Locale;

/**
 * Keys - OpenMeteo parameters.
 *
 * @author Denis Mehilli
 */
public final class Keys {

  static Parameter<String> LATITUDE = Parameter
      .create("latitude", "0")
      .lang(Locale.ENGLISH, "Latitude", "Latitude of the location")
      .lang(Locale.ITALIAN, "Latitudine", "Latitudine della località")
      .build();

  static Parameter<String> LONGITUDE = Parameter
      .create("longitude", "0")
      .lang(Locale.ENGLISH, "Longitude", "Longitude of the location")
      .lang(Locale.ITALIAN, "Longitudine", "Longitudine della località")
      .build();

  static Parameter<String> API_KEY = Parameter
      .create("apiKey", "")
      .secret()
      .lang(Locale.ENGLISH, "API Key", "API key obtained from https://open-meteo.com/en/docs")
      .lang(Locale.ITALIAN, "Chiave API", "Chiave API ottenuta da https://open-meteo.com/en/docs")
      .build();

}
