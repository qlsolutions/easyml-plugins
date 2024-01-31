package com.quicklink.niagara;

import com.quicklink.niagara.model.NiagaraAbout;
import com.quicklink.niagara.model.SerieDetailsModel;
import com.quicklink.niagara.model.SeriesModel;
import com.quicklink.niagara.model.request.SerieDetailsBody;
import com.google.gson.Gson;

import com.quicklink.pluginservice.KeyParam;
import com.quicklink.pluginservice.providers.About;
import com.quicklink.pluginservice.providers.ProviderContext;
import com.quicklink.pluginservice.providers.ProviderPlugin;
import com.quicklink.pluginservice.providers.Record;
import com.quicklink.pluginservice.providers.Serie;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NiagaraPlugin extends ProviderPlugin {
  private static final boolean debug = true;

  static KeyParam<String> PROTOCOL = KeyParam.of("protocol", "http");
  static KeyParam<String> HOST = KeyParam.of("host", "192.168.1.1");
  static KeyParam<Integer> PORT = KeyParam.of("port", 8080);
  static KeyParam<String> USERNAME = KeyParam.of("username", "energylink_debug");
  static KeyParam<String> PASSWORD = KeyParam.ofSecret("password", "");

  private Gson gson;
  private Map<Integer, NiagaraAuthClient> cacheAccess;

  public NiagaraPlugin() {
    super("Niagara","1.0.0", 5, "Weeks limit for request",
        PROTOCOL, HOST, PORT, USERNAME, PASSWORD);
  }

  private NiagaraAuthClient renewToken(NiagaraAuthClient client, Supplier<NiagaraAuthClient> updateClient) {
    // is expired?
    if(client.isExpired()) {
      // new login
      client = updateClient.get();
    } else {
      try {
        // renew token
        client.renewAccessReq();
      } catch (Exception e) {
        if(debug) {
          e.printStackTrace();
        }
        // new login if renew fails
        client = updateClient.get();
      }
    }
    return client;
  }

  @Override
  public void onEnable() {
    gson = new Gson();
    cacheAccess = new ConcurrentHashMap<>();
    getLogger().info("Loaded");
  }


  @Override
  public Collection<Serie> getSeries(ProviderContext ctx) {
    String seriesResponse;

    var protocol = ctx.param(PROTOCOL);
    var host = ctx.param(HOST);
    var port = ctx.param(PORT);
    var username = ctx.param(USERNAME);
    var password = ctx.param(PASSWORD);

    var client = cacheAccess.computeIfAbsent(ctx.idApp(), id -> {
      try {
        return NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host, String.valueOf(port), username,
            password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Renew token ---------------------------------------------------------------------------------
    Supplier<NiagaraAuthClient> updateClient = () -> {
      NiagaraAuthClient c;
      try {
        c = NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host, String.valueOf(port), username,
            password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      cacheAccess.put(ctx.idApp(), c);
      return c;
    };

    client = NiagaraPlugin.this.renewToken(client, updateClient);
    // ---------------------------------------------------------------------------------------------

    try {
      seriesResponse = NiagaraAuthClient.sendGet(client, "series");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

//    System.out.println(seriesResponse); Debug response

    SeriesModel seriesModel = gson.fromJson(seriesResponse, SeriesModel.class);
    var list = seriesModel.series().stream()
        .map(serieModel -> new Serie(serieModel.id(), serieModel.displayName(), serieModel.tags()))
        .toList();
    return list;
  }

  @Override
  public List<Record> getSerieData(ProviderContext ctx, String serieId, long startTs, long endTs) {
    var protocol = ctx.param(PROTOCOL);
    var host = ctx.param(HOST);
    var port = ctx.param(PORT);
    var username = ctx.param(USERNAME);
    var password = ctx.param(PASSWORD);

    var client = cacheAccess.computeIfAbsent(ctx.idApp(), id -> {
      try {
        return NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host, String.valueOf(port), username,
            password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Renew token ---------------------------------------------------------------------------------
    Supplier<NiagaraAuthClient> updateClient = () -> {
      NiagaraAuthClient c;
      try {
        c = NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host, String.valueOf(port), username,
            password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      cacheAccess.put(ctx.idApp(), c);
      return c;
    };

    client = NiagaraPlugin.this.renewToken(client, updateClient);

    // ---------------------------------------------------------------------------------------------


    NiagaraAuthClient finalClient = client;
    return ctx.dateRangeStream(Calendar.WEEK_OF_MONTH).flatMap(
            dateRange -> {
              try {
                return sendRequests(finalClient, serieId, dateRange.start().getTime(),
                    dateRange.end().getTime());
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .collect(Collectors.toList());
  }

  @Override
  public About status(ProviderContext ctx) {
    var protocol = ctx.param(PROTOCOL);
    var host = ctx.param(HOST);
    var port = ctx.param(PORT);
    var username = ctx.param(USERNAME);
    var password = ctx.param(PASSWORD);

    try {
      var client = cacheAccess.computeIfAbsent(ctx.idApp(),
          integer -> {
            try {
              return NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host, String.valueOf(port),
                  username,
                  password);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          });


      // Renew token -------------------------------------------------------------------------------
      Supplier<NiagaraAuthClient> updateClient = () -> {
        NiagaraAuthClient c;
        try {
          c = NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host, String.valueOf(port), username,
              password);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        cacheAccess.put(ctx.idApp(), c);
        return c;
      };

      client = NiagaraPlugin.this.renewToken(client, updateClient);

      // -------------------------------------------------------------------------------------------

      var aboutResponse = NiagaraAuthClient.sendGet(client, "about");

      NiagaraAbout niagaraAbout = gson.fromJson(aboutResponse, NiagaraAbout.class);
      return new About(true, niagaraAbout.host_id(), niagaraAbout.version());
    } catch (Exception e) {
      if(debug) {
        e.printStackTrace();
      }
      return new About(false, null, null);
    }
  }


  private Stream<Record> sendRequests(NiagaraAuthClient client, String serieId, long startTs,
      long endTs) throws Exception {
    getLogger().info("Sending {} from {} to {}", serieId, startTs, endTs);

    String serieDataResponse;

//            var body = new SerieDetailsBody(Long.parseLong("1662336000000"), Long.parseLong("1662365505000"), 0L);
    var body = new SerieDetailsBody(startTs, endTs, 0L);
    serieDataResponse = NiagaraAuthClient.sendPost(client, "serie/" + serieId, gson.toJson(body));
//            System.out.println("SerieDataResponse " + serieDataResponse);

    SerieDetailsModel serieDetailsModel = gson.fromJson(serieDataResponse, SerieDetailsModel.class);
//        System.out.println(serieDetailsModel);

    var list = serieDetailsModel.data().stream()
        .map(data -> new Record(data.timestamp(), data.value())).toList();
    return list.stream();
  }
}
