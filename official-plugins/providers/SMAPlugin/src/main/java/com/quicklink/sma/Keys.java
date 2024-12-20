package com.quicklink.sma;

import com.quicklink.easyml.plugins.api.Parameter;
import com.quicklink.easyml.plugins.api.Parameter.AccessType;
import com.quicklink.sma.client.SMAClient;
import com.quicklink.sma.client.SMAClient.Mode;
import java.util.Arrays;
import java.util.Locale;

/**
 * Keys - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 03/10/2024
 */
public class Keys {

  public static Parameter<String> CLIENT_ID = Parameter
      .create("clientId", "")
      .access(AccessType.write_only)
      .lang(Locale.ENGLISH, "Client Id", "MUST be set according to Id provided by SMA")
      .lang(Locale.ITALIAN, "Client Id", "Id fornito da SMA")
      .build();
  public static Parameter<String> CLIENT_SECRET = Parameter
      .create("clientSecret", "")
      .access(AccessType.write_only)
      .lang(Locale.ENGLISH, "Client secret", "MUST be set according to secret provided by SMA")
      .lang(Locale.ITALIAN, "Client secret", "Secret fornito da SMA")
      .build();

  public static Parameter<String> PLANT_ID = Parameter
      .create("plantId", "")
      .lang(Locale.ENGLISH, "Plant Id", "Plant whose series you want")
      .lang(Locale.ITALIAN, "Id Impianto", "Impianto di cui si vogliono le serie")
      .build();

  public static Parameter<String> DEVICE_ID = Parameter
      .create("deviceId", "")
      .lang(Locale.ENGLISH, "Device Id", "Device whose series you want")
      .lang(Locale.ITALIAN, "Id Dispositivo", "Dispositivo di cui si vogliono le serie")
      .build();

  public  static Parameter<String> MODE = Parameter
      .create("mode", Mode.PRODUCTION.name())
      .select(Arrays.stream(Mode.values()).map(Enum::name).toArray(String[]::new))
      .lang(Locale.ENGLISH, "Mode", "Environment Mode")
      .lang(Locale.ITALIAN, "Modo", "Modo")
      .build();

  public static Parameter<String> EMAIL = Parameter
      .create("email", "apiTestUser@apiSandbox.com")
      .lang(Locale.ENGLISH, "Email", "Login Hint")
      .lang(Locale.ITALIAN, "Email", "Suggerimento Login")
      .build();


}
