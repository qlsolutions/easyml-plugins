package com.quicklink.openmeteo;

import com.google.gson.Gson;

import com.quicklink.pluginservice.KeyParam;
import com.quicklink.pluginservice.providers.About;
import com.quicklink.pluginservice.providers.ProviderContext;
import com.quicklink.pluginservice.providers.ProviderPlugin;
import com.quicklink.pluginservice.providers.Record;
import com.quicklink.pluginservice.providers.Serie;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenMeteoPlugin extends ProviderPlugin {


  static KeyParam<String> LATITUDE = KeyParam.of("latitude", "0");
  static KeyParam<String> LONGITUDE = KeyParam.of("longitude", "0");
  static KeyParam<String> API_KEY = KeyParam.ofSecret("apiKey", "",
      "Key obtained from https://open-meteo.com/en/docs");

  final Gson gson = new Gson();
  final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
  final List<Serie> serieList = new ArrayList<>();

  public OpenMeteoPlugin() {
    super("OpenMeteo","1.0.0", 5, "Days limit for request", LATITUDE, LONGITUDE, API_KEY);
  }

  @Override
  public void onEnable() {
    this.loadSeries();
  }


  @Override
  public Collection<Serie> getSeries(ProviderContext ctx) {
    return serieList;
  }

  @Override
  public List<Record> getSerieData(ProviderContext ctx, String serieId, long startTs, long endTs) {
    var latitude = ctx.param(LATITUDE);
    var longitude = ctx.param(LONGITUDE);
    var apiKey = ctx.param(API_KEY);

    var list = ctx.dateRangeStream(Calendar.DAY_OF_MONTH)
        .flatMap(dateRange -> sendRequest(apiKey, latitude, longitude, serieId, dateRange.start(),
            dateRange.end(), false))
        .toList();

    return list;
  }


  private static String timestampToYYYYMMGG(Date date) {
    return new SimpleDateFormat("yyyy-MM-dd").format(date);
  }

  @Override
  public About status(ProviderContext ctx) {
    return new About(true, "hostId", "1.0.0");
  }

  @Override
  public List<Record> getFutureData(ProviderContext ctx, String serieId, long startTs, long endTs) {
    var latitude = ctx.param(LATITUDE);
    var longitude = ctx.param(LONGITUDE);
    var apiKey = ctx.param(API_KEY);

    return sendRequest(apiKey, latitude, longitude, serieId, new Date(startTs), new Date(endTs),
        true).toList();
  }

  private Stream<Record> sendRequest(String apiKey, String latitude, String longitude,
      String serieId, Date start, Date end, boolean forecast) {
    getLogger().info("Sending {} from {} to {}", serieId, start, end);

    String request;
    if (apiKey.isEmpty()) {
      if(forecast) {
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
      if(forecast) {
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

    getLogger().info("Request to " + request);
    var response = EasyHttp.get(request);
    if (response.responseCode() > 299) {
      // error
      getLogger().error("Error request: " + response.response());
      return Stream.empty();
    }

    var json = gson.fromJson(response.response(), ResponseOpenMeteo.class).hourly();

    List<Record> records = new ArrayList<>();
    var zoneId = ZoneId.systemDefault();

    var time = json.get("time");
    var data = json.get(serieId);
    if (data != null) {
      for (int i = 0; i < data.size(); i++) {
        var dateTimestamp = LocalDateTime.parse((String) time.get(i), formatter);
        records.add(new Record(dateTimestamp.atZone(zoneId).toEpochSecond(), (Double) data.get(i)));
      }
    }
    return records.stream().map(record -> new Record(record.timestamp() * 1000, record.value()));
  }


  private final Pattern regexSeries = Pattern.compile("([a-zA-Z0-9_]+) +\\| +([a-zA-Z,]+)");

  private void loadSeries() {
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

  private final String series = """
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


}