package com.quicklink.niagara;


import com.quicklink.easyml.plugins.api.KeyParam;

public final class Keys {
  static KeyParam<String> PROTOCOL = KeyParam.of("protocol", "http");
  static KeyParam<String> HOST = KeyParam.of("host", "192.168.1.1");
  static KeyParam<Integer> PORT = KeyParam.of("port", 8080);
  static KeyParam<String> USERNAME = KeyParam.of("username", "energylink_debug");
  static KeyParam<String> PASSWORD = KeyParam.ofSecret("password", "");
}
