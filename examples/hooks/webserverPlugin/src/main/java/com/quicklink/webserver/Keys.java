/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.webserver;


import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Locale;

/**
 * Keys - WebServer parameters.
 *
 * @author Denis Mehilli
 */
public final class Keys {

  static Parameter<Double> tolerance_high = Parameter
      .create("tolerance-high", 0D)
      .lang(Locale.ENGLISH, "high tolerance", "High tolerance level")
      .lang(Locale.ITALIAN, "tolleranza alta", "Livello di tolleranza alto")
      .build();

  static Parameter<Double> tolerance_low = Parameter
      .create("tolerance-low", 0D)
      .lang(Locale.ENGLISH, "low tolerance", "Low tolerance level")
      .lang(Locale.ITALIAN, "tolleranza bassa", "Livello di tolleranza basso")
      .build();

  static Parameter<String> addr = Parameter
      .create("addr", "173.125.1.21")
      .lang(Locale.ENGLISH, "address", "Server address")
      .lang(Locale.ITALIAN, "indirizzo", "Indirizzo del server")
      .build();

  static Parameter<String> protocol = Parameter
      .create("protocol", "HTTP")
      .select("HTTP", "HTTPS")
      .lang(Locale.ENGLISH, "protocol", "Protocol type")
      .lang(Locale.ITALIAN, "protocollo", "Tipo di protocollo")
      .build();

  static Parameter<String> template_to_send = Parameter
      .create("template-to-send", """
          {
              "modelId": {model},
              "timestamp": {timestamp},
              "status": {anomalyStatus},
              "score": {predicted},
              "max-predicted": {max-predicted},
              "min-predicted": {min-predicted},
              "observed": {observed},
          }
          """)
      .lang(Locale.ENGLISH, "template to send", "Template to send to the server")
      .lang(Locale.ITALIAN, "modello da inviare", "Modello da inviare al server")
      .build();

  static Parameter<String> requestMethod = Parameter
      .create("request-method", "POST")
      .select("GET", "POST", "PUT", "PATCH", "OPTIONS", "HEAD", "DELETE")
      .lang(Locale.ENGLISH, "request method",
          "HTTP request method.")
      .lang(Locale.ITALIAN, "metodo richiesta",
          "Metodo di richiesta HTTP.")
      .build();

  static Parameter<String> authenticationType = Parameter
      .create("authentication-type", "NONE")
      .select("NONE", "BASIC", "BEARER_TOKEN")
      .lang(Locale.ENGLISH, "authentication type",
          "Type of authentication.")
      .lang(Locale.ITALIAN, "tipo di autenticazione",
          "Tipo di autenticazione.")
      .build();

  static Parameter<String> username = Parameter
      .create("username", "")
      .lang(Locale.ENGLISH, "username", "Username for authentication")
      .lang(Locale.ITALIAN, "nome utente", "Nome utente per l'autenticazione")
      .build();

  static Parameter<String> password = Parameter
      .create("password", "")
      .lang(Locale.ENGLISH, "password", "Password for authentication")
      .lang(Locale.ITALIAN, "password", "Password per l'autenticazione")
      .secret()
      .build();

}
