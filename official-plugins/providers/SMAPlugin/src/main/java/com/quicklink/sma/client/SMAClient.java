package com.quicklink.sma.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quicklink.sma.Keys;
import com.quicklink.sma.SMAException;
import com.quicklink.sma.SMASetType;
import com.quicklink.sma.client.model.AuthorizeResponse;
import com.quicklink.sma.client.model.DataResponse;
import com.quicklink.sma.client.model.DataResponse.SetValue;
import com.quicklink.sma.client.model.LoginResponse;
import com.quicklink.sma.client.model.PlantsListResponse;
import com.quicklink.sma.client.model.SetValueDeserializer;
import com.quicklink.sma.client.model.SetsResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * SMAClient - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 03/10/2024
 */
public class SMAClient {

  public enum Mode {
    SANDBOX,
    PRODUCTION
  }

  public final UUID providerId;
  @NotNull
  final Logger logger;
  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson;

  private LoginResponse token;
  private Instant expires_in;
  private Instant refresh_expires_in;


  public SMAClient(UUID providerId, @NotNull Logger logger) {
    this.providerId = providerId;
    this.logger = logger;

    // Creare una nuova istanza di Gson con il deserializzatore personalizzato
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(SetValue.class, new SetValueDeserializer());
    gson = gsonBuilder.create();
  }

  private Mode getMode() {
    String mode = Keys.MODE.get(providerId);
    try {
      return Mode.valueOf(mode);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid mode " + mode);
    }
  }

  private void login() {
    RequestBody formBody = new FormBody.Builder()
        .add("grant_type", "client_credentials")
        .add("client_id", Keys.CLIENT_ID.get(providerId))
        .add("client_secret", Keys.CLIENT_SECRET.get(providerId))
        .build();

    var req = new Request.Builder()
        .url(getMode() == Mode.SANDBOX ? "https://sandbox-auth.smaapis.de/oauth2/token"
            : "https://auth.smaapis.de/oauth2/token")
        .post(formBody);
    var build = req.build();
    try (var response = client.newCall(build).execute()) {
      if (response.body() == null) {
        throw new RuntimeException("Body is null");
      }
      String responseBody = response.body().string();
      token = gson.fromJson(responseBody, LoginResponse.class);
      expires_in = Instant.now().plusSeconds(token.expires_in());
      refresh_expires_in = Instant.now().plusSeconds(token.refresh_expires_in());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void refreshToken() {
    RequestBody formBody = new FormBody.Builder()
        .add("grant_type", "refresh_token")
        .add("client_id", Keys.CLIENT_ID.get(providerId))
        .add("client_secret", Keys.CLIENT_SECRET.get(providerId))
        .add("refresh_token", token.refresh_token())
        .build();

    var req = new Request.Builder()
        .url(getMode() == Mode.SANDBOX ? "https://sandbox-auth.smaapis.de/oauth2/token"
            : "https://auth.smaapis.de/oauth2/token")
        .post(formBody);
    var build = req.build();
    try (var response = client.newCall(build).execute()) {
      if (response.body() == null) {
        throw new RuntimeException("Body is null");
      }
      token = gson.fromJson(response.body().string(), LoginResponse.class);
      expires_in = Instant.now().plusSeconds(token.expires_in());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private <T> @NotNull T authenticateAndRequest(Supplier<T> fun) {
    Instant now = Instant.now();

    if (token == null || now.isAfter(refresh_expires_in)) {
      try {
        login();
        logger.info("(DataProvider={}: Logged in", providerId);
      } catch (Exception e) {
        throw new RuntimeException("Login failed", e);
      }
    }

    if(now.isBefore(refresh_expires_in) && now.isAfter(expires_in)) {
      refreshToken();
      logger.info("(DataProvider={}: Refreshed token", providerId);
    }

    return fun.get();
  }


  public @NotNull PlantsListResponse getPlantsList() {
    return authenticateAndRequest(this::getPlantsListInternal);
  }

  private PlantsListResponse getPlantsListInternal() {
    var req = new Request.Builder()
        .url(getMode() == Mode.SANDBOX ? "https://sandbox.smaapis.de/monitoring/v1/plants"
            : "https://monitoring.smaapis.de/v1/plants")
        .addHeader("Authorization", "Bearer " + token.access_token())
        .get();
    var build = req.build();
    try (var response = client.newCall(build).execute()) {
//      System.out.println("Header " + response.request().header("Authorization") );
      if (response.code() == 401) {
        throw new SMAException("Unauthorized");
      }

      if (!response.isSuccessful()) {
        throw new SMAException("Response not successful with code " + response.code());
      }

      if (response.body() == null) {
        throw new RuntimeException("Body is null");
      }
      return gson.fromJson(response.body().string(), PlantsListResponse.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }


  public @NotNull SetsResponse getSets() {
    return authenticateAndRequest(this::getSetsInternal);
  }

  private SetsResponse getSetsInternal() {
    String deviceId = Keys.DEVICE_ID.get(providerId);
    var req = new Request.Builder().url(
            getMode() == Mode.SANDBOX ?
                "https://sandbox.smaapis.de/monitoring/v1/devices/" + deviceId + "/measurements/sets"
                : "https://monitoring.smaapis.de/v1/devices/" + deviceId + "/measurements/sets"
        )
        .addHeader("Authorization", "Bearer " + token.access_token())
        .get();
    var build = req.build();
    try (var response = client.newCall(build).execute()) {
//      System.out.println("Header " + response.request().header("Authorization") );
      if (response.code() == 401) {
        throw new SMAException("Unauthorized");
      }

      if (!response.isSuccessful()) {
        throw new SMAException("Response not successful with code " + response.code());
      }

      if (response.body() == null) {
        throw new RuntimeException(
            "Error getSetsInternal request: '%s'. Details: Body is empty".formatted(
                response.code()));
      }
      return gson.fromJson(response.body().string(), SetsResponse.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }


  public @NotNull DataResponse getData(String serieId,
      SMASetType setType, String fromDate) {
    return authenticateAndRequest(
        () -> getDataInternal(serieId, setType, fromDate));
  }

  private @NotNull DataResponse getDataInternal(String serieId,
      SMASetType setType, String fromDate) {
    String deviceId = Keys.DEVICE_ID.get(providerId);
    var req = new Request.Builder()
        .url(
            getMode() == Mode.SANDBOX ?
                "https://sandbox.smaapis.de/monitoring/v1/devices/%s/measurements/sets/%s/Week?Date=%s".formatted(
                    deviceId,
                    setType.name(),
                    fromDate) :
                "https://monitoring.smaapis.de/v1/devices/%s/measurements/sets/%s/Week?Date=%s".formatted(
                    deviceId,
                    setType.name(),
                    fromDate)

        )
        .addHeader("Authorization", "Bearer " + token.access_token())
        .get();
    var build = req.build();
    logger.info("Sending {} from date {}", serieId, fromDate);

    try (var response = client.newCall(build).execute()) {
//      System.out.println("Header " + response.request().header("Authorization") );
      if (response.code() == 401) {
        throw new RuntimeException("Error request: '401'. Details: Unauthorized");
      }

      if (!response.isSuccessful()) {
        throw new RuntimeException("Error request: '%s'. Details: %s".formatted(response.code(),
            response.body() == null ? "" : response.body().string()));
      }

      if (response.body() == null) {
        throw new RuntimeException("Error getDataInternal request: '%s'. Details: Body is empty".formatted(response.code()));
      }
      return gson.fromJson(response.body().string(), DataResponse.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public @NotNull AuthorizeResponse getStatus() {
    return authenticateAndRequest(this::getStatusInternal);
  }

  private @NotNull AuthorizeResponse getStatusInternal() {

    String email = Keys.EMAIL.get(providerId);

    var req = new Request.Builder()
        .url(
            getMode() == Mode.SANDBOX ?
                "https://sandbox.smaapis.de/oauth2/v2/bc-authorize/" + email :
                "https://async-auth.smaapis.de/oauth2/v2/bc-authorize/" + email

        )
        .addHeader("Authorization", "Bearer " + token.access_token())
        .get();
    var build = req.build();

    try (var response = client.newCall(build).execute()) {
//      System.out.println("Header " + response.request().header("Authorization") );
      if (response.code() == 401) {
        throw new RuntimeException("Error request: '401'. Details: Unauthorized");
      }

      if (!response.isSuccessful()) {
        throw new RuntimeException("Error request: '%s'. Details: %s".formatted(response.code(),
            response.body() == null ? "" : response.body().string()));
      }

      if (response.body() == null) {
        throw new RuntimeException("Error getStatusInternal request: '%s'. Details: Body is empty".formatted(response.code()));
      }
      return gson.fromJson(response.body().string(), AuthorizeResponse.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");


  public @NotNull AuthorizeResponse sendEmail() {
    return authenticateAndRequest(this::sendEmailInternal);
  }

  private @NotNull AuthorizeResponse sendEmailInternal() {
     String email = Keys.EMAIL.get(providerId);

     var json = """
        {
        	"loginHint": "%s"
        }
        """.formatted(email);

    RequestBody body = RequestBody.create(json, JSON); // new
    var req = new Request.Builder()
        .url(getMode() == Mode.SANDBOX ? "https://sandbox.smaapis.de/oauth2/v2/bc-authorize"
            : "https://async-auth.smaapis.de/oauth2/v2/bc-authorize")
        .post(body);
    var build = req.build();
    try (var response = client.newCall(build).execute()) {
      if (response.code() == 401) {
        throw new RuntimeException("Error request: '401'. Details: Unauthorized");
      }

      if (!response.isSuccessful()) {
        throw new RuntimeException("Error request: '%s'. Details: %s".formatted(response.code(),
            response.body() == null ? "" : response.body().string()));
      }

      if (response.body() == null) {
        throw new RuntimeException("Error sendEmail request: '%s'. Details: Body is empty".formatted(response.code()));
      }
      return gson.fromJson(response.body().string(), AuthorizeResponse.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
