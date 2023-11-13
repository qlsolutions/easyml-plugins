package com.quicklink.niagara;

import javax.naming.AuthenticationException;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * Created by Tridium, Inc.
 * <p>
 * A sample client implementation for performing a scram-sha auth connection to a Niagara 4 and
 * Niagara AX station.
 */
public class AuthClientExample {

//    public static void main(String[] args) {
//        try {
//            var seriesResponse = AuthClientExample.runGet("http", "about", "192.168.25.242",
//                    8088, "energylink_debug", "qlsol2016");
//            System.out.println(seriesResponse);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }

  public static String runGet(String protocol, String endpoint, String host, int port, String user,
      String psw) throws Exception {
    //        String firstArg = "https://energylink_debug:qlsol2016@192.168.25.242:8443";

    String firstArg = "%s://%s:%s@%s:%d".formatted(protocol, user, psw, host, port);

    NiagaraParameters niagaraParameters = new Niagara4Parameters();

    /*
     * Here we parse the url into username, password and host. This is necessary since
     * we aren't using a standard http mechanism for user info.
     */
    URL fullHost = new URL(firstArg);
    URL loginUrl = new URL(fullHost.getProtocol(), fullHost.getHost(),
        fullHost.getPort() == -1 ? fullHost.getDefaultPort() : fullHost.getPort(),
        "/" + niagaraParameters.getLoginServletName() + "/");
    URL logoutUrl = new URL(fullHost.getProtocol(), fullHost.getHost(),
        fullHost.getPort() == -1 ? fullHost.getDefaultPort() : fullHost.getPort(),
        "/" + niagaraParameters.getLogoutServletName() + "/");

    String[] userInfo = fullHost.getUserInfo().split(":");

    String username = URLDecoder.decode(userInfo[0], "UTF-8");
    String password = URLDecoder.decode(userInfo[1], "UTF-8");

    String auth = "Basic ZGVtbzpMMW5rUXUxY2s=";

    AuthClientExample client = new AuthClientExample(niagaraParameters, loginUrl, logoutUrl,
        username, password);
    URL getUrl = new URL(fullHost, "/easyml/" + endpoint);
    // URL getUrl = new URL(fullHost, "/api_v1/plants");

    return client.sendBasicGetRequest(getUrl, auth);
  }

  public static String runPost(String protocol, String endpoint, String host, int port, String body,
      String user, String psw) throws Exception {
//        String firstArg = "https://energylink_debug:qlsol2016@192.168.25.242:8443";
    String firstArg = "%s://%s:%s@%s:%d".formatted(protocol, user, psw, host, port);

    NiagaraParameters niagaraParameters = new Niagara4Parameters();

    /*
     * Here we parse the url into username, password and host. This is necessary since
     * we aren't using a standard http mechanism for user info.
     */
    URL fullHost = new URL(firstArg);
    URL loginUrl = new URL(fullHost.getProtocol(), fullHost.getHost(),
        fullHost.getPort() == -1 ? fullHost.getDefaultPort() : fullHost.getPort(),
        "/" + niagaraParameters.getLoginServletName() + "/");
    URL logoutUrl = new URL(fullHost.getProtocol(), fullHost.getHost(),
        fullHost.getPort() == -1 ? fullHost.getDefaultPort() : fullHost.getPort(),
        "/" + niagaraParameters.getLogoutServletName() + "/");

    String[] userInfo = fullHost.getUserInfo().split(":");

    String username = URLDecoder.decode(userInfo[0], "UTF-8");
    String password = URLDecoder.decode(userInfo[1], "UTF-8");

    String auth = "Basic ZGVtbzpMMW5rUXUxY2s=";

    AuthClientExample client = new AuthClientExample(niagaraParameters, loginUrl, logoutUrl,
        username, password);
    URL getUrl = new URL(fullHost, "/easyml/" + endpoint);
    // URL getUrl = new URL(fullHost, "/api_v1/plants");

    return client.sendBasicPostRequest(getUrl, auth, body);
  }

  private AuthClientExample(NiagaraParameters niagaraParameters, URL loginUrl, URL logoutUrl,
      String username, String password) {
    this.niagaraParameters = niagaraParameters;
    this.loginUrl = loginUrl;
    this.logoutUrl = logoutUrl;
    this.username = username;
    this.password = password;

    userCookie = niagaraParameters.getUserCookieName() + "=" + username;
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

      //sendGetRequest(loginUrl);
    } catch (Exception e) {
      //#ifdef DEBUG
      if (debugFlag) {
        e.printStackTrace();
      }
      //#endif
      throw new AuthenticationException();
    }

    return sessionCookie;
  }

  /**
   * Sends a get request to the server.
   *
   * @param url The URL of the resource we want to access
   * @throws Exception
   */
  public void sendGetRequest(URL url) throws Exception {
    HttpURLConnection connection = null;

    try {
      connection = (HttpURLConnection) url.openConnection();

      /*
       * WARNING!!! the call to relaxHostChecking is for demonstration purposes only, please
       * DO NOT use in a production like environment
       */
      if (connection instanceof HttpsURLConnection) {
        TrustModifier.relaxHostChecking((HttpsURLConnection) connection);
      }

      connection.setDoInput(true);
      connection.addRequestProperty("Cookie", userCookie);
      if (sessionCookie != null) {
        connection.addRequestProperty("Cookie", sessionCookie);
      }
      connection.connect();
      InputStream in = new BufferedInputStream(connection.getInputStream());
      byte[] buf = new byte[1024];
      while (in.read(buf) != -1) {
      }
      ;
      log("GET Request result: " + new String(buf, "UTF-8"));
      return;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  /**
   * Sends a get request to the server.
   *
   * @param url The URL of the resource we want to access
   * @throws Exception
   */
  public String sendBasicGetRequest(URL url, String authentication) throws Exception {
    HttpURLConnection connection = null;

    try {
      connection = (HttpURLConnection) url.openConnection();

      /*
       * WARNING!!! the call to relaxHostChecking is for demonstration purposes only, please
       * DO NOT use in a production like environment
       */
      if (connection instanceof HttpsURLConnection) {
        TrustModifier.relaxHostChecking((HttpsURLConnection) connection);
      }

      connection.setDoInput(true);
      connection.addRequestProperty("Authorization", authentication);
      connection.connect();

//            System.out.println("STATUS: " + connection.getResponseCode());

      var br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String temp;
      while ((temp = br.readLine()) != null) {
        response.append(temp);
      }

      return response.toString();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  /**
   * Sends a get request to the server.
   *
   * @param url The URL of the resource we want to access
   * @throws Exception
   * @author unldenis
   */
  public String sendBasicPostRequest(URL url, String authentication, String requestBody)
      throws Exception {
    HttpURLConnection connection = null;

    try {
      connection = (HttpURLConnection) url.openConnection();

      /*
       * WARNING!!! the call to relaxHostChecking is for demonstration purposes only, please
       * DO NOT use in a production like environment
       */
      if (connection instanceof HttpsURLConnection) {
        TrustModifier.relaxHostChecking((HttpsURLConnection) connection);
      }

      connection.setRequestMethod("POST");
      connection.setDoInput(true);
      connection.addRequestProperty("Authorization", authentication);

      if (requestBody != null) {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes());
        outputStream.flush();
        outputStream.close();
      }

      connection.connect();

//            System.out.println("STATUS: " + connection.getResponseCode());

      var br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String temp;
      while ((temp = br.readLine()) != null) {
        response.append(temp);
      }

      return response.toString();
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
    HttpURLConnection connection = null;

    try {
      connection = (HttpURLConnection) logoutUrl.openConnection();

      System.out.println("LOGOUT URL: " + logoutUrl);
      System.out.println("connection class: " + connection.getClass());

      /*
       * WARNING!!! the call to relaxHostChecking is for demonstration purposes only, please
       * DO NOT use in a production like environment
       */
      if (connection instanceof HttpsURLConnection) {
        TrustModifier.relaxHostChecking((HttpsURLConnection) connection);
      }

//      System.out.println("         headers: " + connection.getHeaderFields().size());
      Object[] heads = connection.getHeaderFields().keySet().toArray();
//      for (int i = 0; i < heads.length; ++i)
//        System.out.println(heads[i] + " : " + connection.getHeaderFields().get(heads[i]));
//      System.out.println(" body " + connection.getRequestMethod() + " || " + ((InputStream)connection.getContent()));

      connection.connect();

      InputStream in = new BufferedInputStream(connection.getInputStream());
      byte[] buf = new byte[1024];
      int i = 0;
      while (in.read(buf) != -1) {
        i++;
      }
      ;

      byte[] buf2 = new byte[i];
      for (int j = 0; j < i; j++) {
        buf2[j] = buf[j];
      }

      System.out.println("         i: " + i);
      System.out.println("result URL 1: " + bytesToHex(buf2));
      System.out.println("result URL 1: " + new String(buf));
      System.out.println("result URL 2: " + Base64.getEncoder().encodeToString(buf2));

      return;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }

    return new String(hexChars);
  }

  /**
   * Sends a SCRAM-SHA message to the server.
   *
   * @param command The SCRAM-SHA command
   * @param message The client message
   * @return A String containing the server response message
   * @throws Exception
   */
  private String sendScramMessage(String command, String message) throws Exception {
    HttpURLConnection connection = null;
    Exception exception = null;

    try {
      String serverMessage = null;

      // Create the connection
      connection = (HttpURLConnection) loginUrl.openConnection();

      /*
       * WARNING!!! the call to relaxHostChecking is for demonstration purposes only, please
       * DO NOT use in a production like environment
       */
      if (connection instanceof HttpsURLConnection) {
        TrustModifier.relaxHostChecking((HttpsURLConnection) connection);
      }

      String request = "action=" + command + "&" + message;

      // Set the headers
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");

      // you can set this header to whatever you wish
      connection.setRequestProperty("User-Agent", USER_AGENT);

      // these header fields are REQUIRED
      connection.setRequestProperty("Content-Type", "application/x-niagara-login-support");
      connection.setRequestProperty("Content-Length", Integer.toString(request.length()));
      connection.addRequestProperty("Cookie", userCookie);

      // make sure you save the sessionCookie for subsequent requests for the same session
      if (sessionCookie != null) {
        connection.addRequestProperty("Cookie", sessionCookie);
      }

      // Make the POST request
      OutputStream out = connection.getOutputStream();
      //#ifdef DEBUG
      log("sending request to server: " + request);
      //#endif
      out.write(request.getBytes());
      out.flush();

      // Set the session Cookie we got from the server
      // make sure you save the sessionCookie for subsequent requests for the same session
      String cookie = connection.getHeaderField("Set-Cookie");
      if (cookie != null && cookie.startsWith(niagaraParameters.getSessionCookieName())) {
        sessionCookie = (cookie.split(";"))[0].trim();
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
      InputStream in = connection.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      serverMessage = reader.readLine();

      return serverMessage;
    } catch (Exception e) {
      exception = e;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    throw exception;
  }

  private static final void usage() {
    System.err.println(
        "usage: java com.github.unldenis.test.AuthClientExample http[s]://<username>:<password>@<host>[:<port>] [axclient=true]");
    System.err.println(
        "\n  if axclient=true is provided, then assume connection is to an axclient station and adjust");
    System.err.println(
        "  the connection parameters for AX, otherwise assume connection is to a Niagara 4 station.");
    System.exit(-1);
  }

  /*
   * WARNING!!! The TrustModifier class is used to disable certificate validation and hostname verification
   * when using TLS. It's use here is for demonstration purposes only and should not be used in production
   * code.
   */

  private static class TrustModifier {

    private static final TrustingHostnameVerifier TRUSTING_HOSTNAME_VERIFIER = new TrustingHostnameVerifier();
    private static SSLSocketFactory factory;

    /**
     * Call this with any HttpURLConnection, and it will modify the trust settings if it is an HTTPS
     * connection.
     */
    public static void relaxHostChecking(HttpURLConnection conn)
        throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

      if (conn instanceof HttpsURLConnection) {
        HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
        SSLSocketFactory factory = prepFactory(httpsConnection);
        httpsConnection.setSSLSocketFactory(factory);
        httpsConnection.setHostnameVerifier(TRUSTING_HOSTNAME_VERIFIER);
      }
    }

    static synchronized SSLSocketFactory prepFactory(HttpsURLConnection httpsConnection)
        throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

      if (factory == null) {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[]{new AlwaysTrustManager()}, null);
        factory = ctx.getSocketFactory();
      }
      return factory;
    }

    private static final class TrustingHostnameVerifier implements HostnameVerifier {

      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    }

    private static class AlwaysTrustManager implements X509TrustManager {

      public void checkClientTrusted(X509Certificate[] arg0, String arg1)
          throws CertificateException {
      }

      public void checkServerTrusted(X509Certificate[] arg0, String arg1)
          throws CertificateException {
      }

      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }
    }

  }

  private static final void log(String msg) {
    if (debugFlag) {
      System.err.println("[com.github.unldenis.test.AuthClientExample] " + msg);
    }
  }

  private static abstract class NiagaraParameters {

    public abstract String getSessionCookieName();

    public abstract String getLoginServletName();

    public abstract String getLogoutServletName();

    public abstract String getUserCookieName();
  }

  private static class NiagaraAXParameters extends NiagaraParameters {

    public String getSessionCookieName() {
      return "niagara_session";
    }

    public String getLoginServletName() {
      return "login";
    }

    public String getLogoutServletName() {
      return "logout";
    }

    public String getUserCookieName() {
      return "niagara_userid";
    }
  }

  private static class Niagara4Parameters extends NiagaraParameters {

    public String getSessionCookieName() {
      return "JSESSIONID";
    }

    public String getLoginServletName() {
      return "j_security_check";
    }

    public String getLogoutServletName() {
      return "logout";
    }

    public String getUserCookieName() {
      return "niagara_userid";
    }
  }

  private NiagaraParameters niagaraParameters;
  private URL loginUrl;
  private URL logoutUrl;
  private String username;
  private String password;
  private String sessionCookie;
  private String userCookie;

  public static boolean debugFlag = true;

  private static final String USER_AGENT = "ScramSha Auth Client Example/1.2";
  private static final String CMD_CLIENT_FIRST_MESSAGE = "sendClientFirstMessage";
  private static final String CMD_CLIENT_FINAL_MESSAGE = "sendClientFinalMessage";
}
