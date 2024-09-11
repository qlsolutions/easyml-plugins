/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.resend;


import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Locale;

/**
 * Keys - Mail parameters.
 *
 * @author Denis Mehilli
 * @creation 11/09/2024
 */
public final class Keys {

  static Parameter<Double> tolerance_high = Parameter
      .create("tolerance-high", 0D)
      .lang(Locale.ENGLISH, "High Tolerance", "High tolerance threshold for anomaly detection")
      .lang(Locale.ITALIAN, "Tolleranza Alta",
          "Soglia di tolleranza alta per il rilevamento delle anomalie")
      .build();

  static Parameter<Double> tolerance_low = Parameter
      .create("tolerance-low", 0D)
      .lang(Locale.ENGLISH, "Low Tolerance", "Low tolerance threshold for anomaly detection")
      .lang(Locale.ITALIAN, "Tolleranza Bassa",
          "Soglia di tolleranza bassa per il rilevamento delle anomalie")
      .build();

  static Parameter<String> from_name = Parameter
      .create("from-name", "Quicklink EasyML Server")
      .lang(Locale.ENGLISH, "Sender Name", "Name of the email sender")
      .lang(Locale.ITALIAN, "Nome Mittente", "Nome del mittente dell'email")
      .build();

  static Parameter<String> from_address = Parameter
      .create("from-address", "info@qlsol.com")
      .lang(Locale.ENGLISH, "Sender Email", "Email address of the sender")
      .lang(Locale.ITALIAN, "Email Mittente", "Indirizzo email del mittente")
      .build();


  static Parameter<String> to_address = Parameter
      .create("to-address", "")
      .lang(Locale.ENGLISH, "Receiver Email", "Email address of the receiver")
      .lang(Locale.ITALIAN, "Email Destinatario", "Indirizzo email del destinatario")
      .build();


  static Parameter<String> object_start = Parameter
      .create("mail-object-on-anomaly-start",
          "[EasyML] anomaly detected! (score{model}, score={predicted})")
      .lang(Locale.ENGLISH, "Anomaly Start Subject", "Subject line for anomaly detection email")
      .lang(Locale.ITALIAN, "Titolo Anomalia Inizio", "Oggetto dell'email per rilevamento anomalia")
      .build();

  static Parameter<String> content_start = Parameter
      .create("mail-content-on-anomaly-start",
          "<p>Anomaly detected by EasyML! model=<strong>{model}</strong>, date=<strong>{timestamp}</strong>, predicted=<strong>{predicted}</strong>, "
              + "max-predicted=<strong>{max-predicted}</strong>, min-predicted=<strong>{min-predicted}</strong>, observed=<strong>{observed}</strong></p>")
      .lang(Locale.ENGLISH, "Anomaly Start Content",
          "Content of the email when an anomaly is detected")
      .lang(Locale.ITALIAN, "Contenuto Anomalia Inizio",
          "Contenuto dell'email alla rilevazione di un'anomalia")
      .build();

  static Parameter<String> object_end = Parameter
      .create("mail-object-on-anomaly-end",
          "[EasyML] anomaly end! (score{model}, score={predicted})")
      .lang(Locale.ENGLISH, "Anomaly End Subject", "Subject line for anomaly resolution email")
      .lang(Locale.ITALIAN, "Titolo Anomalia Fine",
          "Oggetto dell'email per la risoluzione dell'anomalia")
      .build();

  static Parameter<String> content_end = Parameter
      .create("mail-content-on-anomaly-end",
          "<p>Anomaly end! model=<strong>{model}</strong>, date=<strong>{timestamp}</strong>, predicted=<strong>{predicted}</strong>, "
              + "max-predicted=<strong>{max-predicted}</strong>, min-predicted=<strong>{min-predicted}</strong>, observed=<strong>{observed}</strong></p>")
      .lang(Locale.ENGLISH, "Anomaly End Content",
          "Content of the email when an anomaly is resolved")
      .lang(Locale.ITALIAN, "Contenuto Anomalia Fine",
          "Contenuto dell'email alla risoluzione di un'anomalia")
      .build();

  static Parameter<String> api_key = Parameter
      .create("apiKey", "re_123456789")
      .secret()
      .lang(Locale.ENGLISH, "API Key", "API key obtained from https://resend.com/")
      .lang(Locale.ITALIAN, "Chiave API", "Chiave API ottenuta da https://resend.com/")
      .build();

}
