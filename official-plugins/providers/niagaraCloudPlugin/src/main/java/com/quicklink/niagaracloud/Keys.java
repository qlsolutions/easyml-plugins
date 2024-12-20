package com.quicklink.niagaracloud;

import com.quicklink.easyml.plugins.api.Parameter;
import com.quicklink.easyml.plugins.api.Parameter.AccessType;
import java.util.Locale;

/**
 * Keys - NiagaraCloud parameters.
 *
 * @author Denis Mehilli
 */
public final class Keys {

  static Parameter<String> CLIENT_ID = Parameter
      .create("clientId", "enter-your-service-account-client-id-here")
      .access(AccessType.write_only)
      .lang(Locale.ENGLISH, "Client Id", "Client id used by Niagara Cloud")
      .lang(Locale.ITALIAN, "Client Id", "Client id utilizzato da Niagara Cloud")
      .build();

  static Parameter<String> CLIENT_SECRET = Parameter
      .create("clientSecret", "enter-your-service-account-client-secret-here")
      .access(AccessType.write_only)
      .lang(Locale.ENGLISH, "Client Secret", "Client secret used by Niagara Cloud")
      .lang(Locale.ITALIAN, "Client Secret", "Client secret utilizzato da Niagara Cloud")
      .build();

  static Parameter<String> CUSTOMER_ID = Parameter
      .create("customerId", "")
      .lang(Locale.ENGLISH, "Customer Id", "Customer Id of service")
      .lang(Locale.ITALIAN, "Customer Id", "Customer Id del servizio")
      .build();


  static Parameter<String> HOST_ID = Parameter
      .create("hostId", "")
      .lang(Locale.ENGLISH, "Host Id", "Host Id of device")
      .lang(Locale.ITALIAN, "Host Id", "Host Id del dispositivo")
      .build();


}