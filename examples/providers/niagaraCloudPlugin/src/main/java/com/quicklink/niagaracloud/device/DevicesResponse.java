package com.quicklink.niagaracloud.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quicklink.niagaracloud.model.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmsQueryResponse models the response format from the Entity Model
 * Service (EMS).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevicesResponse {
  @JsonProperty("_embedded")
  private DevicesResults content;

  @JsonProperty("_links")
  private Object links;

  @JsonProperty
  private Page page;
}
