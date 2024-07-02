package com.quicklink.niagara;


import com.quicklink.easyml.plugins.api.KeyParam;

public final class Keys {
  static KeyParam<String> PROTOCOL = KeyParam.of("protocol", "https");
  static KeyParam<String> HOST = KeyParam.of("host", "192.168.1.1");
  static KeyParam<Integer> PORT = KeyParam.of("port", 443);
  static KeyParam<String> USERNAME = KeyParam.of("username", "admin");
  static KeyParam<String> PASSWORD = KeyParam.ofSecret("password", "");
}
