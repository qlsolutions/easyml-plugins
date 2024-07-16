package com.quicklink.niagara;


import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Locale;

public final class Keys {
  static Parameter<String> PROTOCOL = Parameter
      .create("protocol", "https")
      .lang(Locale.ENGLISH, "Protocol", "Protocol used by the Niagara interface")
      .lang(Locale.ITALIAN, "Protocollo", "Protocollo utilizzato dall'interfaccia Niagara")
      .build();

  static Parameter<String> HOST = Parameter
      .create("host", "192.168.1.1")
      .lang(Locale.ENGLISH, "Host", "Address of the Niagara system")
      .lang(Locale.ITALIAN, "Indirizzo", "Indirizzo del sistema Niagara")
      .build();

  static Parameter<Integer> PORT = Parameter
      .create("port", 443)
      .lang(Locale.ENGLISH, "Port", "Port number for the Niagara interface")
      .lang(Locale.ITALIAN, "Porta", "Numero di porta per l'interfaccia Niagara")
      .build();

  static Parameter<String> USERNAME = Parameter
      .create("username", "admin")
      .lang(Locale.ENGLISH, "Username", "Username for Niagara BASIC authentication")
      .lang(Locale.ITALIAN, "Nome utente", "Nome utente per l'autenticazione BASIC di Niagara")
      .build();

  static Parameter<String> PASSWORD = Parameter
      .create("password", "")
      .secret()
      .lang(Locale.ENGLISH, "Password", "Password for Niagara BASIC authentication")
      .lang(Locale.ITALIAN, "Password", "Password per l'autenticazione BASIC di Niagara")
      .build();

}
