/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.hookemail;


import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Locale;

/**
 * Keys - Mail parameters.
 *
 * @author Denis Mehilli
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

  static Parameter<String> to_name = Parameter
      .create("to-name", "Info Quicklink")
      .lang(Locale.ENGLISH, "Receiver Name", "Name of the email receiver")
      .lang(Locale.ITALIAN, "Nome Destinatario", "Nome del destinatario dell'email")
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

  static Parameter<String> smtp_host = Parameter
      .create("SMTP-Host", "smtp.gmail.com")
      .lang(Locale.ENGLISH, "SMTP Host", "SMTP server host")
      .lang(Locale.ITALIAN, "Host SMTP", "Host del server SMTP")
      .build();

  static Parameter<Integer> smtp_port = Parameter
      .create("SMTP-Port", 25)
      .lang(Locale.ENGLISH, "SMTP Port", "SMTP server port")
      .lang(Locale.ITALIAN, "Porta SMTP", "Porta del server SMTP")
      .build();

  static Parameter<String> smtp_username = Parameter
      .create("SMTP-Username", "sender@gmail.com")
      .lang(Locale.ENGLISH, "SMTP Username", "SMTP server username")
      .lang(Locale.ITALIAN, "Username SMTP", "Nome utente del server SMTP")
      .build();

  static Parameter<String> smtp_password = Parameter
      .create("SMTP-Password", "")
      .secret()
      .lang(Locale.ENGLISH, "SMTP Password", "SMTP server password")
      .lang(Locale.ITALIAN, "Password SMTP", "Password del server SMTP")
      .build();

  static Parameter<String> smtp_transport_strategy = Parameter
      .create("SMTP-TransportStrategy", "SMTP_TLS")
      .select("SMTP", "SMTP_TLS", "SMTPS", "SMTP_OAUTH2")
      .lang(Locale.ENGLISH, "SMTP Transport Strategy", "")
      .lang(Locale.ITALIAN, "Strategia di Trasporto SMTP", "")
      .build();

}
