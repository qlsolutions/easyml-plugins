package com.quicklink.niagara;/*
 *
 *  * Copyright (c) 2015. QuickLink Solutions, Inc. All Rights Reserved.
 *
 */

import com.quicklink.niagara.ScramSha256Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Tridium, Inc.
 * <p>
 * A sample client implementation for performing a scram-sha auth connection to a Niagara 4 and
 * Niagara AX station.
 */
public class NiagaraAuthClient {

  /**
   *
   * @param protocol
   * @param host
   * @param port
   * @param username
   * @param password
   * @param spec
   * @return
   * @throws Exception
   * @author Denis Mehilli
   */
  // // USAGE EXAMPLE: java NiagaraAuthClient https://demo5.qlsol.com easylink qlsol2016
  public static String sendGet(String protocol, String host, String port, String username,
      String password, String spec/*api_v1/plants*/)
      throws Exception {
    NiagaraAuthClient client = parametersCreator(protocol, host, port, username, password);

    // TODO: here you should implement the requests that you want to call
//    demoRequests(client);
    URL plantsUrl = new URL(client.mainUrl, "easyml/" + spec);
//    System.out.println("plantsUrl: " + plantsUrl);
    String response = client.sendGetRequest(plantsUrl);

//    System.out.println("RESPONSE: " + response);

    // ALWAYS call this method in the end
    niagaraClientLogout(client);

    return response;
  }

  /**
   *
   * @param protocol
   * @param host
   * @param port
   * @param username
   * @param password
   * @param spec
   * @param body
   * @return
   * @throws Exception
   * @author Denis MeHILLI
   */
  public static String sendPost(String protocol, String host, String port, String username,
      String password, String spec/*api_v1/plants*/, @Nullable String body)
      throws Exception {
    NiagaraAuthClient client = parametersCreator(protocol, host, port, username, password);

    // TODO: here you should implement the requests that you want to call
//    demoRequests(client);
    URL plantsUrl = new URL(client.mainUrl, "easyml/" + spec);
//    System.out.println("plantsUrl: " + plantsUrl);
    String response = client.sendPostRequest(plantsUrl, body);

//    System.out.println("RESPONSE: " + response);

    // ALWAYS call this method in the end
    niagaraClientLogout(client);

    return response;
  }

  public static NiagaraAuthClient parametersCreator(String protocol, String host, String port,
      String username, String password)
      throws Exception {
    NiagaraParameters niagaraParameters = new Niagara4HeaderParameters();

    /*
     * Here we parse the url into username, password and host. This is necessary since
     * we aren't using a standard http mechanism for user info.
     */
    URL fullHost = new URL(protocol + "://" + host + (port.length() > 0 ? ":" + port : ""));
    URL loginUrl = new URL(fullHost.getProtocol(),
        fullHost.getHost(),
        fullHost.getPort() == -1 ? fullHost.getDefaultPort() : fullHost.getPort(),
        "/" + niagaraParameters.getLoginServletName() + "/");
    URL logoutUrl = new URL(fullHost.getProtocol(),
        fullHost.getHost(),
        fullHost.getPort() == -1 ? fullHost.getDefaultPort() : fullHost.getPort(),
        "/" + niagaraParameters.getLogoutServletName());

    String decodedUsername = URLDecoder.decode(username, "UTF-8");
    String decodedPassword = URLDecoder.decode(password, "UTF-8");

    log("loginUrl : " + loginUrl);
    log("decodedUsername : " + decodedUsername);
    log("decodedPassword : " + decodedPassword);

    try {
      NiagaraAuthClient client = new NiagaraAuthClient(niagaraParameters, fullHost, loginUrl,
          logoutUrl, username, password);
      client.loginHeader();
      log("login successful");
      return client;
    } catch (Exception e) {
      log("failed to log in");
      throw e;
    }
  }

  private static void demoRequests(NiagaraAuthClient client)
      throws Exception {
    System.out.println("\n\n[DEMO REQUESTS]");

    URL plantsUrl = new URL(client.mainUrl, "api_v1/plants");
    System.out.println("plantsUrl: " + plantsUrl);
    String response = client.sendGetRequest(plantsUrl);
    System.out.println("RESPONSE: " + response);
  }

  private static void niagaraClientLogout(NiagaraAuthClient client)
      throws Exception {
    client.logout();
    log("logout successful");
  }

  private NiagaraAuthClient(NiagaraParameters niagaraParameters, URL mainUrl, URL loginUrl,
      URL logoutUrl, String username, String password) {
    this.niagaraParameters = niagaraParameters;
    this.mainUrl = mainUrl;
    this.loginUrl = loginUrl;
    this.logoutUrl = logoutUrl;
    this.username = username;
    this.password = password;
  }

////////////////////////////////////////////////////////////////
// Authentication
////////////////////////////////////////////////////////////////

  /**
   * Logs in to the station using digest authentication.
   *
   * @return A String containing the session cookie.
   * @throws Exception
   */
  public String login() throws Exception {
    try {
      // Create the connection
      ScramSha256Client scramClient = new ScramSha256Client(username, password);

      // client-first-message
      String clientFirstMessage = scramClient.createClientFirstMessage();

      // server-first-message
      String message = "clientFirstMessage=" + clientFirstMessage;
      String serverFirstMessage = sendScramMessage(CMD_CLIENT_FIRST_MESSAGE, message);

      // client-final-message
      String clientFinalMessage = scramClient.createClientFinalMessage(serverFirstMessage);

      // server-final-message
      message = "clientFinalMessage=" + clientFinalMessage;
      String serverFinalMessage = sendScramMessage(CMD_CLIENT_FINAL_MESSAGE, message);

      // validate
      scramClient.processServerFinalMessage(serverFinalMessage);
      sendGetRequest(loginUrl);
    } catch (Exception e) {
      //#ifdef DEBUG
      if (debugFlag) {
        e.printStackTrace();
      }
      //#endif
      throw new AuthenticationException();
    }

    return sessionId;
  }

  public String loginHeader() throws Exception {
    try {
      AuthMessage message = new AuthMessage();
      message.setScheme("HELLO");
      message.setParameter("username", Base64.getUrlEncoder().withoutPadding()
          .encodeToString(username.getBytes(StandardCharsets.UTF_8)));
      Map<String, List<String>> headers = sendAuthMessage(message);
      AuthMessage respMessage = AuthMessage.decodeFromString(
          headers.get("WWW-Authenticate").get(0));
      if (!respMessage.getScheme().equalsIgnoreCase("SCRAM")
          || !"SHA-256".equalsIgnoreCase(respMessage.getParameter("hash"))) {
        throw new AuthenticationException(
            "Server does not support SCRAM-SHA-256 header authentication");
      }

      String handshakeToken = respMessage.getParameter("handshakeToken");

      // Create the connection
      ScramSha256Client scramClient = new ScramSha256Client(username, password);

      // client-first-message
      String clientFirstMessage = scramClient.createClientFirstMessage();
      message = new AuthMessage();
      message.setScheme("SCRAM");
      message.setParameter("data", Base64.getUrlEncoder().withoutPadding()
          .encodeToString(clientFirstMessage.getBytes(StandardCharsets.UTF_8)));
      message.setParameter("handshakeToken", handshakeToken);

      // server-first-message
      headers = sendAuthMessage(message);
      respMessage = AuthMessage.decodeFromString(headers.get("WWW-Authenticate").get(0));
      String serverFirstMessage = new String(
          Base64.getUrlDecoder().decode(respMessage.getParameter("data")), StandardCharsets.UTF_8);

      // client-final-message
      String clientFinalMessage = scramClient.createClientFinalMessage(serverFirstMessage);
      message = new AuthMessage();
      message.setScheme("SCRAM");
      message.setParameter("data", Base64.getUrlEncoder().withoutPadding()
          .encodeToString(clientFinalMessage.getBytes(StandardCharsets.UTF_8)));
      message.setParameter("handshakeToken", handshakeToken);

      // server-final-message
      headers = sendAuthMessage(message);

      // Server responds with header "Authentication-Info: authToken=x, hash=x, data=x" on succesfull authentication.
      // authToken is your session ID and must be included in a "BEARER" authentication message on subsequent requests.
      respMessage = AuthMessage.decodeFromString(
          "BEARER " + headers.get("Authentication-Info").get(0));
      sessionId = respMessage.getParameter("authToken");

      // validate
      String serverFinalMessage = new String(
          Base64.getUrlDecoder().decode(respMessage.getParameter("data")), StandardCharsets.UTF_8);
      scramClient.processServerFinalMessage(serverFinalMessage);
      sendGetRequest(loginUrl);
    } catch (Exception e) {
      //#ifdef DEBUG
      if (debugFlag) {
        e.printStackTrace();
      }
      //#endif
      throw new AuthenticationException();
    }
    return sessionId;
  }

  /**
   * Sends a get request to the server.
   *
   * @param url The URL of the resource we want to access
   * @throws Exception
   */
  public String sendGetRequest(URL url) throws Exception {
    HttpURLConnection connection = null;

    try {
      connection = (HttpURLConnection) url.openConnection();

      connection.setDoInput(true);

      if (niagaraParameters instanceof Niagara4HeaderParameters) {
        if (sessionId != null) {
          AuthMessage message = new AuthMessage();
          message.setScheme("BEARER");
          message.setParameter("authToken", sessionId);
          connection.addRequestProperty("Authorization", message.encodeToString());
        }
      } else {
        connection.addRequestProperty("Cookie",
            niagaraParameters.getUserCookieName() + "=" + username);
        if (sessionId != null) {
          connection.addRequestProperty("Cookie",
              niagaraParameters.getSessionCookieName() + "=" + sessionId);
        }
      }
      connection.connect();

      StringBuilder builder = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
        String line;
        while ((line = reader.readLine()) != null) {
          builder.append(line + "\n");
        }
      }

      String response = builder.toString();
      Matcher match = CSRF_TOKEN_PATTERN.matcher(response);
      if (match.find()) {
        String csrfTokenElement = match.group(0);
        match = VALUE_PATTERN.matcher(csrfTokenElement);
        if (match.find() && match.groupCount() >= 1) {
          csrfToken = match.group(1);
        }
      }

      return response;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  /**
   * Sends a post request to the server.
   *
   * @param url The URL of the resource we want to access
   * @throws Exception
   * @author Denis Mehilli
   */
  public String sendPostRequest(URL url, @Nullable String body) throws Exception {
    HttpURLConnection connection = null;

    try {
      connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("POST");
      connection.setDoInput(true);

      if (niagaraParameters instanceof Niagara4HeaderParameters) {
        if (sessionId != null) {
          AuthMessage message = new AuthMessage();
          message.setScheme("BEARER");
          message.setParameter("authToken", sessionId);
          connection.addRequestProperty("Authorization", message.encodeToString());
        }
      } else {
        connection.addRequestProperty("Cookie",
            niagaraParameters.getUserCookieName() + "=" + username);
        if (sessionId != null) {
          connection.addRequestProperty("Cookie",
              niagaraParameters.getSessionCookieName() + "=" + sessionId);
        }
      }

      if (body != null) {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
      }

      connection.connect();

      StringBuilder builder = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
        String line;
        while ((line = reader.readLine()) != null) {
          builder.append(line + "\n");
        }
      }

      String response = builder.toString();
      Matcher match = CSRF_TOKEN_PATTERN.matcher(response);
      if (match.find()) {
        String csrfTokenElement = match.group(0);
        match = VALUE_PATTERN.matcher(csrfTokenElement);
        if (match.find() && match.groupCount() >= 1) {
          csrfToken = match.group(1);
        }
      }

      return response;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  /**
   * Logs the user out of the station.
   *
   * @throws Exception
   */
  public void logout() throws Exception {
    String url = logoutUrl.toString();
    if (csrfToken != null) {
      url = url + "?csrfToken=" + csrfToken;
    }
    sendGetRequest(new URL(url));
  }

  /**
   * Sends a SCRAM-SHA message to the server.
   *
   * @param command The SCRAM-SHA command
   * @param message The client message
   * @return A String containing the server response message
   * @throws Exception
   */
  private String sendScramMessage(String command, String message)
      throws Exception {
    HttpURLConnection connection = null;

    try {
      String serverMessage = null;

      // Create the connection
      connection = (HttpURLConnection) loginUrl.openConnection();

      String request = "action=" + command + "&" + message;

      // Set the headers
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");

      // you can set this header to whatever you wish
      connection.setRequestProperty("User-Agent", USER_AGENT);

      // these header fields are REQUIRED
      connection.setRequestProperty("Content-Type", "application/x-niagara-login-support");
      connection.setRequestProperty("Content-Length", Integer.toString(request.length()));
      connection.addRequestProperty("Cookie",
          niagaraParameters.getUserCookieName() + "=" + username);

      // make sure you save the sessionId for subsequent requests for the same session
      if (sessionId != null) {
        connection.addRequestProperty("Cookie",
            niagaraParameters.getSessionCookieName() + "=" + sessionId);
      }

      // Make the POST request
      try (OutputStream out = connection.getOutputStream()) {
        //#ifdef DEBUG
        log("sending request to server: " + request);
        //#endif
        out.write(request.getBytes(StandardCharsets.UTF_8));
        out.flush();
      }

      // Set the session Cookie we got from the server
      // make sure you save the sessionId for subsequent requests for the same session
      List<String> cookieHeaders = connection.getHeaderFields().get("Set-Cookie");
      if (cookieHeaders != null) {
        for (String cookie : cookieHeaders) {
          if (cookie != null && cookie.startsWith(niagaraParameters.getSessionCookieName())) {
            sessionId = (cookie.split(";"))[0].trim();
            sessionId = sessionId.split("=")[1];
            System.out.println("*** sessionid: " + sessionId);
            break;
          }
        }
      }

      // Check the response code
      int status = connection.getResponseCode();
      //#ifdef DEBUG
      log("status code from the remote server = " + status);
      //#endif
      if (status != HttpURLConnection.HTTP_OK) {
        throw new AuthenticationException();
      }

      // Read the response
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
        serverMessage = reader.readLine();
      }

      return serverMessage;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private Map<String, List<String>> sendAuthMessage(AuthMessage message)
      throws Exception {
    HttpURLConnection connection = null;

    try {
      // Create the connection
      connection = (HttpURLConnection) loginUrl.openConnection();

      // Set the headers
      connection.setRequestProperty("User-Agent", USER_AGENT);
      connection.setRequestProperty("Authorization", message.encodeToString());

      //#ifdef DEBUG
      log("sending request to server: " + message.encodeToString());
      //#endif

      // Check the response code
      int status = connection.getResponseCode();
      //#ifdef DEBUG
      log("status code from the remote server = " + status);
      //#endif
      if (status != HttpURLConnection.HTTP_OK && status != HttpURLConnection.HTTP_UNAUTHORIZED) {
        throw new AuthenticationException();
      }

      return connection.getHeaderFields();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  // private static final void usage()
  // {
  //   System.err.println("usage: java NiagaraAuthClient http[s]://<username>:<password>@<host>[:<port>] [client_type]");
  //   System.err.println("\n  client_type can be \"ax\" \"n4\" or \"n4header\". Default is \"n4header\".");
  //   System.err.println("  n4header refers to the header authentication mechanism added in Niagara 4.4.");
  //   System.err.println("  This mechanism is not supported in N4 stations prior to 4.4. \"n4\" client_type");
  //   System.err.println("  should be used for pre-4.4 stations.");
  //   System.exit(-1);
  // }

  private static final void log(String msg) {
    if (debugFlag) {
      System.err.println("[NiagaraAuthClient] " + msg);
    }
  }

  private static abstract class NiagaraParameters {

    public abstract String getSessionCookieName();

    public abstract String getLoginServletName();

    public abstract String getLogoutServletName();

    public abstract String getUserCookieName();
  }

//   private static class NiagaraAXParameters
//     extends NiagaraParameters
//   {
//     public String getSessionCookieName() { return "niagara_session"; }
//     public String getLoginServletName() { return "login"; }
//     public String getLogoutServletName() { return "logout"; }
//     public String getUserCookieName() { return "niagara_userid"; }
//   }

//   private static class Niagara4Parameters
//     extends NiagaraParameters
//   {
//     public String getSessionCookieName() { return "JSESSIONID"; }
//     public String getLoginServletName() { return "j_security_check"; }
//     public String getLogoutServletName() { return "logout"; }
//     public String getUserCookieName() { return "niagara_userid"; }
//   }

  private static class Niagara4HeaderParameters
      extends NiagaraParameters {

    public String getSessionCookieName() {
      return null;
    }

    // login is allowed at any URI using the header authentication mechanism
    public String getLoginServletName() {
      return "ord/station:";
    }

    public String getLogoutServletName() {
      return "logout";
    }

    public String getUserCookieName() {
      return null;
    }
  }

  private static class AuthMessage {

    public static AuthMessage decodeFromString(String message) {
      AuthMessage auth = new AuthMessage();
      int space = message.indexOf(" ");
      if (space >= 0) {
        auth.setScheme(message.substring(0, space));
        String params = message.substring(space + 1);
        StringTokenizer tokenizer = new StringTokenizer(params, ",");
        while (tokenizer.hasMoreElements()) {
          String token = tokenizer.nextToken();
          int equal = token.indexOf("=");
          if (equal <= -1) {
            throw new IllegalArgumentException("parameter missing '='");
          }
          String key = token.substring(0, equal).trim();
          String value = token.substring(equal + 1).trim();
          if (auth.getParameter(key) != null) {
            throw new IllegalArgumentException("duplicate parameter");
          }
          auth.setParameter(key, value);
        }
      } else {
        auth.setScheme(message);
      }
      return auth;
    }

    public String encodeToString() {
      StringBuilder builder = new StringBuilder();
      builder.append(scheme);
      boolean firstEntry = true;
      for (Entry<String, String> entry : params.entrySet()) {
        if (firstEntry) {
          firstEntry = false;
          builder.append(" ");
        } else {
          builder.append(", ");
        }
        builder.append(entry.getKey());
        builder.append("=");
        builder.append(entry.getValue());
      }
      return builder.toString();
    }

    public void setScheme(String scheme) {
      this.scheme = scheme;
    }

    public String getScheme() {
      return scheme;
    }

    public void setParameter(String key, String value) {
      params.put(key, value);
    }

    public String getParameter(String key) {
      return params.get(key);
    }

    private String scheme;
    private Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  }

  private NiagaraParameters niagaraParameters;
  private URL mainUrl;
  private URL loginUrl;
  private URL logoutUrl;
  private String username;
  private String password;
  private String sessionId;
  private String csrfToken;

  public static boolean debugFlag = false;

  private static final String USER_AGENT = "ScramSha EasyML Niagara Auth Client/1.2";
  private static final String CMD_CLIENT_FIRST_MESSAGE = "sendClientFirstMessage";
  private static final String CMD_CLIENT_FINAL_MESSAGE = "sendClientFinalMessage";
  private static final Pattern CSRF_TOKEN_PATTERN = Pattern.compile(
      "<input [^<>]*id=['\"]csrfToken['\"][^<>]*>");
  private static final Pattern VALUE_PATTERN = Pattern.compile("value=['\"]([^\"']*)['\"]");
}