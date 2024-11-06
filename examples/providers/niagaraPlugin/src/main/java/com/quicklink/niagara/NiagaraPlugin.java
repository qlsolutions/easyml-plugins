/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.niagara;

import static com.quicklink.niagara.Keys.HOST;
import static com.quicklink.niagara.Keys.PASSWORD;
import static com.quicklink.niagara.Keys.PORT;
import static com.quicklink.niagara.Keys.PROTOCOL;
import static com.quicklink.niagara.Keys.USERNAME;

import com.google.gson.Gson;
import com.quicklink.easyml.plugins.api.ParamLang;
import com.quicklink.easyml.plugins.api.providers.About;
import com.quicklink.easyml.plugins.api.providers.ProviderContext;
import com.quicklink.easyml.plugins.api.providers.ProviderPlugin;
import com.quicklink.easyml.plugins.api.providers.Serie;
import com.quicklink.easyml.plugins.api.providers.TimedValue;
import com.quicklink.niagara.model.NiagaraAbout;
import com.quicklink.niagara.model.SerieDetailsModel;
import com.quicklink.niagara.model.SerieDetailsModel.Data;
import com.quicklink.niagara.model.SeriesModel;
import com.quicklink.niagara.model.request.SerieDetailsBody;
import java.time.Instant;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/**
 * NiagaraPlugin - Plugin entrypoint.
 *
 * @author Denis Mehilli
 */
public class NiagaraPlugin extends ProviderPlugin {

  private Gson gson;
  private Map<UUID, NiagaraAuthClient> cacheAccess;

  public NiagaraPlugin() {
    super("Niagara", "1.0.0", 5,
        PROTOCOL, HOST, PORT, USERNAME, PASSWORD);
  }

  private NiagaraAuthClient renewToken(NiagaraAuthClient client,
      Supplier<NiagaraAuthClient> updateClient) {
    // is expired?
    if (client.isExpired()) {
      // new login
      client = updateClient.get();
    } else {
      try {
        // renew token
        client.renewAccessReq();
      } catch (Exception e) {
        getLogger().info("Error renewing token", e);
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
  public void onCreate(@NotNull ProviderContext providerContext) {

  }

  @Override
  public @NotNull List<Serie> getSeries(@NotNull ProviderContext ctx) {
    String seriesResponse;

    var protocol = ctx.param(PROTOCOL);
    var host = ctx.param(HOST);
    var port = ctx.param(PORT);
    var username = ctx.param(USERNAME);
    var password = ctx.param(PASSWORD);

    var client = cacheAccess.computeIfAbsent(ctx.providerId(), id -> {
      try {
        return NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host,
            String.valueOf(port), username,
            password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Renew token ---------------------------------------------------------------------------------
    Supplier<NiagaraAuthClient> updateClient = () -> {
      NiagaraAuthClient c;
      try {
        c = NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host,
            String.valueOf(port), username,
            password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      cacheAccess.put(ctx.providerId(), c);
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
    return seriesModel.series().stream()
        .map(serieModel -> new Serie(serieModel.id(), serieModel.displayName(), serieModel.tags()))
        .toList();
  }

  @Override
  public @NotNull LinkedList<TimedValue> getSerieData(ProviderContext ctx, @NotNull String serieId,
      @NotNull Instant startTs,
      @NotNull Instant endTs) {
    var protocol = ctx.param(PROTOCOL);
    var host = ctx.param(HOST);
    var port = ctx.param(PORT);
    var username = ctx.param(USERNAME);
    var password = ctx.param(PASSWORD);

    var client = cacheAccess.computeIfAbsent(ctx.providerId(), id -> {
      try {
        return NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host,
            String.valueOf(port), username,
            password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Renew token ---------------------------------------------------------------------------------
    Supplier<NiagaraAuthClient> updateClient = () -> {
      NiagaraAuthClient c;
      try {
        c = NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host,
            String.valueOf(port), username,
            password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      cacheAccess.put(ctx.providerId(), c);
      return c;
    };

    client = NiagaraPlugin.this.renewToken(client, updateClient);

    // ---------------------------------------------------------------------------------------------

    NiagaraAuthClient finalClient = client;

    try {
      return getData(finalClient, serieId, startTs.toEpochMilli(), endTs.toEpochMilli());
    } catch (Exception e) {
      getLogger().error("Error retrieving data", e);
      return new LinkedList<>();
    }
  }

  @Override
  public @NotNull About status(ProviderContext ctx) {
    var protocol = ctx.param(PROTOCOL);
    var host = ctx.param(HOST);
    var port = ctx.param(PORT);
    var username = ctx.param(USERNAME);
    var password = ctx.param(PASSWORD);

    try {
      var client = cacheAccess.computeIfAbsent(ctx.providerId(),
          integer -> {
            try {
              return NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host,
                  String.valueOf(port),
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
          c = NiagaraAuthClient.parametersCreator(NiagaraPlugin.this, protocol, host,
              String.valueOf(port), username,
              password);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        cacheAccess.put(ctx.providerId(), c);
        return c;
      };

      client = NiagaraPlugin.this.renewToken(client, updateClient);

      // -------------------------------------------------------------------------------------------

      var aboutResponse = NiagaraAuthClient.sendGet(client, "about");

      NiagaraAbout niagaraAbout = gson.fromJson(aboutResponse, NiagaraAbout.class);
      return new About(true, niagaraAbout.host_id(), niagaraAbout.version());
    } catch (Exception e) {
      getLogger().error("Error retrieving status", e);
      return new About(false, null, null);
    }
  }


  private LinkedList<TimedValue> getData(NiagaraAuthClient client, String serieId,
      long startTs,
      long endTs) throws Exception {
    getLogger().info("Sending {} from {} to {}", serieId, startTs, endTs);

    String serieDataResponse;

//            var body = new SerieDetailsBody(Long.parseLong("1662336000000"), Long.parseLong("1662365505000"), 0L);
    var body = new SerieDetailsBody(startTs, endTs, 0L);
    serieDataResponse = NiagaraAuthClient.sendPost(client, "serie/" + serieId, gson.toJson(body));
//            System.out.println("SerieDataResponse " + serieDataResponse);

    SerieDetailsModel serieDetailsModel = gson.fromJson(serieDataResponse, SerieDetailsModel.class);
//        System.out.println(serieDetailsModel);

    LinkedList<TimedValue> records = new LinkedList<>();

    for (Data datum : serieDetailsModel.data()) {
      records.add(new TimedValue(datum.timestamp(), datum.value()));
    }

    return records;
  }
}
