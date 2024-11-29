package com.quicklink.niagaracloud;

import static com.quicklink.niagaracloud.Keys.*;

import com.quicklink.easyml.plugins.api.EasyML;
import com.quicklink.easyml.plugins.api.providers.Serie;
import com.quicklink.easyml.plugins.api.providers.TimedValue;
import com.quicklink.niagaracloud.device.Device;
import com.quicklink.niagaracloud.device.DevicesResponse;
import com.quicklink.niagaracloud.auth.ApiTokenResponse;
import com.quicklink.niagaracloud.model.PointsResponse;
import com.quicklink.niagaracloud.model.Point;
import com.quicklink.niagaracloud.model.Tag;
import com.quicklink.niagaracloud.history.HistoryRecord;
import com.quicklink.niagaracloud.history.HistoryResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * NiagaraCloudClient - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 27/11/2024
 */
public class NiagaraCloudClient {

  public static final String AMPERSAND = "&";
  public static final String EQUALS = "=";


  // Simple HTTP Client used for all requests
  private final HttpClient httpClient = HttpClient.newBuilder().build();

  private ApiTokenResponse tokenHolder;
  private Instant expireTokenInstant;
  private String systemGuid;

  private final UUID providerId;

  public NiagaraCloudClient(@NotNull UUID providerId) {
    this.providerId = providerId;
  }

  private void checkExpiration() throws Exception {
    if (expireTokenInstant == null || expireTokenInstant.isBefore(Instant.now())) {
      loadAccessToken();
      findSystemGuid();
    }
  }


  /**
   * Get an access token from the NCS OAuth endpoint.
   *
   * @return the access token
   * @throws Exception if an error occurs
   */
  private void loadAccessToken() throws Exception {

    // Use form data for authentication type and scope
    Map<Object, Object> requestData = new HashMap<>();
    requestData.put("grant_type", "client_credentials");
    requestData.put("scope", "ncp:read");

    // Build the HTTP Request
    HttpRequest tokenRequest = HttpRequest.newBuilder()
        // Use NCS authentication endpoint from configuration file
        .uri(URI.create("https://auth.pingone.com/2bb83e4b-dd73-47d3-984b-6e601302d766/as/token"))

        // Serialize request data into JSON string
        .POST(toFormData(requestData))

        // Include content type for the form data
        .header("Content-Type", "application/x-www-form-urlencoded")

        // Include the authorization header
        .header("Authorization", getAuthHeader(CLIENT_ID.get(providerId), CLIENT_SECRET.get(providerId)))
        .build();

    // Send HTTP request and handle response
    HttpResponse<String> response = httpClient.send(tokenRequest,
        BodyHandlers.ofString());
    tokenHolder = EasyML.getJsonMapper().fromJsonString(response.body(), ApiTokenResponse.class);
    expireTokenInstant = Instant.now().plusSeconds(tokenHolder.getExpiresIn());
  }

  private void findSystemGuid() throws Exception {

    int page = 0;
    DevicesResponse devicesResponse;

    boolean found = false;
    do {

      HttpRequest systemGuidRequest = HttpRequest.newBuilder()
          .uri(URI.create(
              "https://www.niagara-cloud.com/api/v2/management/organizations/" + CUSTOMER_ID.get(providerId)
                  + "/devices?size=20&page=" + page))

          .GET()

          .header("Content-Type", "application/json")

          .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
          .build();

      HttpResponse<String> response = httpClient.send(systemGuidRequest,
          BodyHandlers.ofString());
      devicesResponse = EasyML.getJsonMapper()
          .fromJsonString(response.body(), DevicesResponse.class);

      for (Device device : devicesResponse.getContent().getDevices()) {

        if (device.getHostId().equals(HOST_ID.get(providerId))) {
          systemGuid = device.getSystemGuid();
          found = true;
          break;
        }
      }

      page++;

    } while (devicesResponse.getPage().getTotalPages() > page);

    if (!found) {
      throw new Exception("not found systemGuid of hostId " + HOST_ID.get(providerId));
    }

  }

  public List<Serie> getSeries() throws Exception {
    checkExpiration();

    List<Serie> series = new ArrayList<>();

    int page = 0;
    PointsResponse pointsResponse;

    do {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(
              "https://www.niagara-cloud.com/api/v1/entitymodel/customers/" + CUSTOMER_ID.get(providerId)
                  + "/tagValues?page=" + page + "&size=200&count=true&sortBy=id&sortDir=asc"))

          .POST(BodyPublishers.ofString(
              "{\"systemGuid\":\"%s\",\"searchType\":\"tagValue\",\"searchItems\":[\"n:type=history:HistoryConfig\"],\"comparisonType\":\"any\"}".formatted(
                  systemGuid)))

          .header("Content-Type", "application/json")

          .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
          .build();

      HttpResponse<String> response = httpClient.send(request,
          BodyHandlers.ofString());
      pointsResponse = EasyML.getJsonMapper()
          .fromJsonString(response.body(), PointsResponse.class);

      for (Point point : pointsResponse.getContent().getPoints()) {

        Tag historyTag = point.getTags().stream().filter(tag -> tag.getTagId().equals("n:history"))
            .findFirst().orElse(null);

        series.add(
            new Serie(
                point.getCloudId(),
                historyTag != null ?
                    historyTag.getTagValue().substring(1).replace("/", "-") :
                    point.getName(),
                point.getTags().stream().map(Tag::getTagId).collect(Collectors.toList())
            )
        );

      }

      page++;

    } while (pointsResponse.getPage().getTotalPages() > page);

    return series;

  }

  public LinkedList<TimedValue> getSerieData(@NotNull String serieId, @NotNull String startTime, @NotNull String endTime) throws Exception {
    checkExpiration();

    LinkedList<TimedValue> serieData = new LinkedList<>();
    ;

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://www.niagara-cloud.com/api/v1/egress/telemetry"))

        .POST(BodyPublishers.ofString(
            "{\"systemGuid\":\"%s\",\"cloudId\":[\"%s\"],\"recordLimit\":\"50000\",\"startTime\":\"%s\",\"endTime\":\"%s\"}"
                .formatted(systemGuid, serieId, startTime, endTime)))

        .header("Content-Type", "application/json")

        .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
        .build();

    HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
    HistoryResponse historyResponse = EasyML.getJsonMapper().fromJsonString(response.body(), HistoryResponse.class);


    if(historyResponse.getPointDetails().isEmpty()) {
//      throw new Exception("No point available for serieId(cloudId) " + serieId);
      return serieData;
    }

    double value;
    for (HistoryRecord historyRecord : historyResponse.getPointDetails().get(0).getHistoryRecords()) {

      if(historyRecord.getValue() instanceof Double) {
        value = (Double) historyRecord.getValue();
      } else if(historyRecord.getValue() instanceof Number) {
        value = ((Number) historyRecord.getValue()).doubleValue();
      } else {
        continue;
      }

      serieData.add(new TimedValue(historyRecord.getTime(), value));

    }
    return serieData;

  }




  private static String getAuthHeader(String username, String password) {
    String toEncode = username + ':' + password;
    return "Basic " + Base64.getEncoder().encodeToString(toEncode.getBytes());
  }

  /**
   * Encode the mapped data into an HTTP form data format.
   *
   * @param dataMap the list of properties
   * @return the encoded form data
   */
  private static BodyPublisher toFormData(Map<Object, Object> dataMap) {
    StringBuilder builder = new StringBuilder();
    dataMap.forEach((key, value) -> {
      if (!builder.isEmpty()) {
        builder.append(AMPERSAND);
      }
      builder.append(URLEncoder.encode(key.toString(), StandardCharsets.UTF_8))
          .append(EQUALS)
          .append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
    });
    return BodyPublishers.ofString(builder.toString());
  }

}
