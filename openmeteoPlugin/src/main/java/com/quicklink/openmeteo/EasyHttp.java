package com.quicklink.openmeteo;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class EasyHttp {

  private final HttpURLConnection conn;

  private Integer responseCode = null;
  private String response = null;


  public EasyHttp(HttpURLConnection conn) {
    this.conn = conn;
  }


  public static Builder get() {
    return new Builder().method("GET");
  }

  public static EasyHttp get(String url) {
    var builder = get().url(url);
    return builder.build();
  }

  public static Builder post() {
    return new Builder().method("POST");
  }

  public static EasyHttp post(String url) {
    var builder = post().url(url);
    return builder.build();
  }

  public static EasyHttp post(String url, String json) {
    var builder = post()
        .url(url)
        .payload(json)
        .header("Accept", "application/json")
        .header("Content-Type", "application/json");

    return builder.build();
  }


  public static Builder put() {
    return new Builder().method("PUT");
  }

  public static EasyHttp put(String url) {
    var builder = put().url(url);
    return builder.build();
  }

  public int responseCode() {
    if (responseCode != null) {
      return responseCode;
    }
    try {
      return responseCode = conn.getResponseCode();
    } catch (IOException e) {
      throw new EasyHttpException(e);
    }
  }

  public String response() {
    if (response != null) {
      return response;
    }
    try {
      var in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String inputLine;
      StringBuilder content = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();
      return response = content.toString();
    } catch (IOException e) {
      throw new EasyHttpException(e);
    }
  }

  public static final class Builder {

    private String url;
    private String method;
    private Map<String, String> reqHeaders = null;
    private Integer connectTimeout, readTimeout;

    private Map<String, String> parameters = null;
    private byte[] payload;

    private boolean outputFlag = false;

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder method(String method) {
      this.method = method;
      return this;
    }

    public Builder header(String key, String value) {
      if (reqHeaders == null) {
        reqHeaders = new LinkedHashMap<>();
      }
      this.reqHeaders.put(key, value);
      return this;
    }

    public Builder connectTimeout(int timeout) {
      this.connectTimeout = timeout;
      return this;
    }

    public Builder readTimeout(int timeout) {
      this.readTimeout = timeout;
      return this;
    }

    public Builder parameter(String key, String value) {
      outputFlag = true;
      if (parameters == null) {
        parameters = new LinkedHashMap<>();
      }
      this.parameters.put(key, value);
      return this;
    }

    public Builder payload(String payload) {
      outputFlag = true;

      this.payload = payload.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public EasyHttp build() throws EasyHttpException {
      try {
        URL url = new URL(this.url);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(method);

        if (outputFlag) {
          con.setDoOutput(true);
        }

        if (reqHeaders != null) {
          for (var entry : this.reqHeaders.entrySet()) {
            con.setRequestProperty(entry.getKey(), entry.getValue());
          }
        }

        if (connectTimeout != null) {
          con.setConnectTimeout(connectTimeout);
        }

        if (readTimeout != null) {
          con.setReadTimeout(readTimeout);
        }

        if (parameters != null) {
          DataOutputStream out = new DataOutputStream(con.getOutputStream());
          out.writeBytes(getParamsString(parameters));
          out.flush();
          out.close();
        }

        if (payload != null && Objects.equals(method, "POST")) {
          OutputStream stream = con.getOutputStream();
          stream.write(payload);
        }

        return new EasyHttp(con);
      } catch (IOException e) {
        throw new EasyHttpException(e);
      }
    }


    private String getParamsString(Map<String, String> params)
        throws UnsupportedEncodingException {
      StringBuilder result = new StringBuilder();

      for (Map.Entry<String, String> entry : params.entrySet()) {
        result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
        result.append("=");
        result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        result.append("&");
      }

      String resultString = result.toString();
      return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
  }

  public static final class EasyHttpException extends RuntimeException {

    public EasyHttpException(Throwable cause) {
      super(cause);
    }
  }


}
