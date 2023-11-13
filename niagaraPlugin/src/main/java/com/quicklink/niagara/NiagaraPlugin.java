package com.quicklink.niagara;

import com.quicklink.niagara.model.NiagaraAbout;
import com.quicklink.niagara.model.SerieDetailsModel;
import com.quicklink.niagara.model.SeriesModel;
import com.quicklink.niagara.model.request.SerieDetailsBody;
import com.google.gson.Gson;
import com.quicklink.pluginservice.*;
import com.quicklink.pluginservice.Record;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NiagaraPlugin extends Plugin {

  private Gson gson;

  @Override
  public void onEnable() {
    gson = new Gson();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    getLogger().info("Loaded");
  }

  @Override
  public ParametersMap getDefaultParameters() {
//        var map = new ParametersMap();
//        map.put("protocol", "http");
//        map.put("host", "192.168.1.1");
//        map.put("port", 8080);
//        map.put("username", "energylink_debug");
//        map.putPassword("password", "");
//

    return ParametersMap.builder()
        .limit(5, "Weeks limit for request")
        .param("protocol", "http")
        .param("host", "192.168.1.1")
        .param("port", 8080)
        .param("username", "energylink_debug")
        .secret("password", "")
        .build();

  }

  @Override
  public Collection<Serie> getSeries(Context ctx) {
    String seriesResponse;
    try {
      seriesResponse = AuthClientExample.runGet(ctx.param("protocol"), "series", ctx.param("host"),
          ctx.param("port"), ctx.param("username"), ctx.param("password"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

//    System.out.println(seriesResponse); Debug response

    SeriesModel seriesModel = gson.fromJson(seriesResponse, SeriesModel.class);
    var list = seriesModel.series()
        .stream()
        .map(serieModel -> new Serie(serieModel.id(), serieModel.displayName(), serieModel.tags()))
        .toList();
    return list;
  }

  @Override
  public List<Record> getSerieData(Context ctx, String serieId, long startTs, long endTs) {
    String protocol = ctx.param("protocol");
    String host = ctx.param("host");
    int port = ctx.param("port");
    String username = ctx.param("username");
    String password = ctx.param("password");

    return ctx.dateRangeStream(Calendar.WEEK_OF_MONTH)
        .flatMap(dateRange -> sendRequests(protocol, host, port, username, password, serieId,
            dateRange.start().getTime(), dateRange.end().getTime()))
        .collect(Collectors.toList());
  }

  @Override
  public About status(Context ctx) {
    try {
      var aboutResponse = AuthClientExample.runGet(ctx.param("protocol"), "about",
          ctx.param("host"),
          ctx.param("port"), ctx.param("username"), ctx.param("password"));

      NiagaraAbout niagaraAbout = gson.fromJson(aboutResponse, NiagaraAbout.class);
      return new About(true, niagaraAbout.host_id(), niagaraAbout.version());
    } catch (Exception e) {
      return new About(false, null, null);
    }
  }


  private Stream<Record> sendRequests(String protocol, String host, int port, String username,
      String password, String serieId, long startTs, long endTs) {
    getLogger().info("Sending {} from {} to {}", serieId, startTs, endTs);

    String serieDataResponse;
    try {
//            var body = new SerieDetailsBody(Long.parseLong("1662336000000"), Long.parseLong("1662365505000"), 0L);
      var body = new SerieDetailsBody(startTs, endTs, 0L);
      serieDataResponse = AuthClientExample.runPost(protocol, "serie/" + serieId,
          host, port, gson.toJson(body), username,
          password);
//            System.out.println("SerieDataResponse " + serieDataResponse);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    SerieDetailsModel serieDetailsModel = gson.fromJson(serieDataResponse, SerieDetailsModel.class);
//        System.out.println(serieDetailsModel);

    var list = serieDetailsModel.data()
        .stream()
        .map(data -> new Record(data.timestamp(), data.value()))
        .toList();
    return list.stream();
  }
}
