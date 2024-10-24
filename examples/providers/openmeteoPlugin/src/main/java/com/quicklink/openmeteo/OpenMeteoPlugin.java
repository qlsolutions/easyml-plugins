/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.openmeteo;

import static com.quicklink.openmeteo.Keys.API_KEY;
import static com.quicklink.openmeteo.Keys.LATITUDE;
import static com.quicklink.openmeteo.Keys.LONGITUDE;

import com.google.gson.Gson;
import com.quicklink.easyml.plugins.api.ParamLang;
import com.quicklink.easyml.plugins.api.providers.About;
import com.quicklink.easyml.plugins.api.providers.ProviderContext;
import com.quicklink.easyml.plugins.api.providers.ProviderPlugin;
import com.quicklink.easyml.plugins.api.providers.Serie;
import com.quicklink.easyml.plugins.api.providers.TimedValue;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * OpenMeteo - Plugin entrypoint.
 *
 * @author Denis Mehilli
 */
public class OpenMeteoPlugin extends ProviderPlugin {

  final Gson gson = new Gson();
  final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
  final List<Serie> serieList = new ArrayList<>();
  private final Pattern regexSeries = Pattern.compile("([a-zA-Z0-9_]+) +\\| +([a-zA-Z,]+)");

  public OpenMeteoPlugin() {
    super("OpenMeteo", "1.0.0",
        5,
        new ParamLang("Limit", "Days limit for request"),
        new ParamLang("Limite", "Limite di giorni per richiesta"),
        LATITUDE, LONGITUDE, API_KEY);
  }

  private static String timestampToYYYYMMGG(Instant instant) {
    return new SimpleDateFormat("yyyy-MM-dd").format(instant);
  }

  @Override
  public void onEnable() {
    this.loadSeries();
  }

  @Override
  public void onCreate(@NotNull ProviderContext ctx) {

  }

  @Override
  public @NotNull Collection<Serie> getSeries(ProviderContext ctx) {
    return serieList;
  }

  @Override
  public @NotNull List<TimedValue> getSerieData(ProviderContext ctx, String serieId,
      @NotNull Instant start,
      Instant end) {
    var latitude = ctx.param(LATITUDE);
    var longitude = ctx.param(LONGITUDE);
    var apiKey = ctx.param(API_KEY);

    return ctx.dateRangeStream(Calendar.DAY_OF_MONTH)
        .flatMap(dateRange -> sendRequest(apiKey, latitude, longitude, serieId, dateRange.start()
            .toInstant(), dateRange.end().toInstant(), false))
        .toList();
  }

  @Override
  public @NotNull About status(ProviderContext ctx) {
    return new About(true, "hostId", "1.0.0");
  }

  @Override
  public @NotNull List<TimedValue> getFutureData(ProviderContext ctx, @NotNull String serieId,
      @NotNull Instant start, @NotNull Instant end) {
    var latitude = ctx.param(LATITUDE);
    var longitude = ctx.param(LONGITUDE);
    var apiKey = ctx.param(API_KEY);

    return sendRequest(apiKey, latitude, longitude, serieId, start, end,
        true).toList();
  }

  private Stream<TimedValue> sendRequest(String apiKey, String latitude, String longitude,
      String serieId, Instant start, Instant end, boolean forecast) {
    getLogger().ifPresent(logger -> logger.info("Sending {} from {} to {}", serieId, start, end));

    String request;
    if (apiKey.isEmpty()) {
      if (forecast) {
        request = String.format(
            "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=%s&start_date=%s&end_date=%s",
            latitude, longitude, serieId, timestampToYYYYMMGG(start), timestampToYYYYMMGG(end));
      } else {
        request = String.format(
            "https://archive-api.open-meteo.com/v1/archive?latitude=%s&longitude=%s&hourly=%s&start_date=%s&end_date=%s",
            latitude, longitude, serieId, timestampToYYYYMMGG(start), timestampToYYYYMMGG(end));
      }
    } else {
      // commercial plan
      if (forecast) {
        request = String.format(
            "https://customer-api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=%s&start_date=%s&end_date=%s",
            latitude, longitude, serieId, timestampToYYYYMMGG(start), timestampToYYYYMMGG(end));
      } else {
        request = String.format(
            "https://customer-archive-api.open-meteo.com/v1/archive?latitude=%s&longitude=%s&hourly=%s&start_date=%s&end_date=%s",
            latitude, longitude, serieId, timestampToYYYYMMGG(start), timestampToYYYYMMGG(end));
      }

      request += "&apikey=" + apiKey;
    }

    var finalRequest = request;
    getLogger().ifPresent(logger -> logger.info("Request to " + finalRequest));

    var response = EasyHttp.get(request);
    if (response.responseCode() > 299) {
      // error
      getLogger().ifPresent(logger -> logger.error("Error request: " + response.response()));

      return Stream.empty();
    }

    var json = gson.fromJson(response.response(), ResponseOpenMeteo.class).hourly();

    List<TimedValue> records = new ArrayList<>();

    var time = json.get("time");
    var data = json.get(serieId);
    if (data != null) {
      for (int i = 0; i < data.size(); i++) {
        var instant = Instant.parse((String) time.get(i));
        records.add(new TimedValue(instant, (double) data.get(i)));
      }
    }
    return records.stream();
  }

  private void loadSeries() {
    String series = """
        temperature_2m                 |  temperature
        relativehumidity_2m            |  humidity
        dewpoint_2m                    |  dewpoint
        apparent_temperature           |  temperature
        precipitation                  |  weather
        rain                           |  weather
        snowfall                       |  weather
        weathercode                    |  weather
        pressure_msl                   |  pressure
        surface_pressure               |  pressure
        cloudcover                     |  weather
        cloudcover_low                 |  weather
        cloudcover_mid                 |  weather
        cloudcover_high                |  weather
        et0_fao_evapotranspiration     |  weather
        vapor_pressure_deficit         |  weather,pressure
        windspeed_10m                  |  wind
        windspeed_100m                 |  wind
        winddirection_10m              |  wind
        winddirection_100m             |  wind
        windgusts_10m                  |  wind
        soil_temperature_0_to_7cm      |  temperature,soil
        soil_temperature_7_to_28cm     |  temperature,soil
        soil_temperature_28_to_100cm   |  temperature,soil
        soil_temperature_100_to_255cm  |  temperature,soil
        soil_moisture_0_to_7cm         |  humidity,soil
        soil_moisture_7_to_28cm        |  humidity,soil
        soil_moisture_28_to_100cm      |  humidity,soil
        soil_moisture_100_to_255cm     |  humidity,soil
        """;
    var matcher = regexSeries.matcher(series);
    while (matcher.find()) {
      var e = matcher.group(1);
      var arrE = Arrays.stream(e.split("_"))
          .map(s -> {
            if (Character.isDigit(s.charAt(0))) {
              return "(" + s + ")";
            } else {
              return Character.toUpperCase(s.charAt(0)) + s.substring(1);
            }
          })
          .collect(Collectors.joining(" "));
      serieList.add(new Serie(e, arrE, matcher.group(2).split(",")));
    }
  }


}