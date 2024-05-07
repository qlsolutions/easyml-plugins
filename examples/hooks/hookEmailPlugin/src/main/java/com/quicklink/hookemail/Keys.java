package com.quicklink.hookemail;


import com.quicklink.plugins.api.KeyParam;

public final class Keys {

  static KeyParam<Double> tolerance_high = KeyParam.of("tolerance-high", 0D);
  static KeyParam<Double> tolerance_low = KeyParam.of("tolerance-low", 0D);
  static KeyParam<String> from_name = KeyParam.of("From-name", "Quicklink EasyML Server");
  static KeyParam<String> from_address = KeyParam.of("From-address", "easyml@qlsol.com");
  static KeyParam<String> to_name = KeyParam.of("To-name", "Info Quicklink");
  static KeyParam<String> to_address = KeyParam.of("To-address", "info@qlsol.com");

  static KeyParam<String> object_start = KeyParam.of("Mail-object-on-anomaly-start", "[EasyML] anomaly detected! (score{model}, score={predicted})");
  static KeyParam<String> content_start = KeyParam.of("Mail-content-on-anomaly-start",
      "<p>Anomaly detected by EasyML! model=<strong>{model}</strong>, date=<strong>{timestamp}</strong>, predicted=<strong>{predicted}</strong>, "
          + "max-predicted=<strong>{max-predicted}</strong>, min-predicted=<strong>{min-predicted}</strong>, observed=<strong>{observed}</strong></p>");

  static KeyParam<String> object_end = KeyParam.of("Mail-object-on-anomaly-end", "[EasyML] anomaly end! (score{model}, score={predicted})");
  static KeyParam<String> content_end = KeyParam.of("Mail-content-on-anomaly-end",
      "<p>Anomaly end! model=<strong>{model}</strong>, date=<strong>{timestamp}</strong>, predicted=<strong>{predicted}</strong>, "
          + "max-predicted=<strong>{max-predicted}</strong>, min-predicted=<strong>{min-predicted}</strong>, observed=<strong>{observed}</strong></p>");

  static KeyParam<String> smtp_host = KeyParam.of("SMTP-Host", "smtp.gmail.com");
  static KeyParam<Integer> smtp_port = KeyParam.of("SMTP-Port", 25);
  static KeyParam<String> smtp_username = KeyParam.of("SMTP-Username", "sender@gmail.com");
  static KeyParam<String> smtp_password = KeyParam.ofSecret("SMTP-Password", "");
  static KeyParam<String> smtp_transport_strategy = KeyParam.of("SMTP-TransportStrategy", "SMTP_TLS", "Options: SMTP/SMTP_TLS/SMTPS/SMTP_OAUTH2");
}
