package com.quicklink.niagaracloud.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NdsApiTokenHolder is used for holding an access token along with details
 * about the token.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiTokenResponse {
  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("expires_in")
  private int expiresIn;

  private String scope;
}
