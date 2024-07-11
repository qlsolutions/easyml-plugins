package com.quicklink.niagara;


import com.quicklink.easyml.plugins.api.Parameter;
import java.util.Locale;

public final class Keys {
  static Parameter<String> PROTOCOL = Parameter
      .create("protocol", "https")
      .lang(Locale.ENGLISH, "protocol", "Niagara interface protocol")
      .lang(Locale.ITALIAN, "protocollo", "Interfaccia Niagara protocollo")
      .build();
  static Parameter<String> HOST = Parameter
      .create("host", "192.168.1.1")
      .lang(Locale.ENGLISH, "host", "Niagara address")
      .lang(Locale.ITALIAN, "indirizzo", "Indirizzo Niagara")
      .build();
  static Parameter<Integer> PORT = Parameter
      .create("port", 443)
      .lang(Locale.ENGLISH, "port", "Niagara interface port")
      .lang(Locale.ITALIAN, "porta", "Porta Niagara")
      .build();
  static Parameter<String> USERNAME = Parameter
      .create("username", "admin")
      .lang(Locale.ENGLISH, "username", "Niagara BASIC username")
      .lang(Locale.ITALIAN, "username", "Username Niagara")
      .build();
  static Parameter<String> PASSWORD = Parameter
      .create("password", "")
      .secret()
      .lang(Locale.ENGLISH, "password", "Niagara BASIC password")
      .lang(Locale.ITALIAN, "password", "Password Niagara")
      .build();
}
