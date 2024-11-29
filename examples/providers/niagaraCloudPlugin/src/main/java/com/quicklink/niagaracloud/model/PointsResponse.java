package com.quicklink.niagaracloud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmsQueryResponse models the response format from the Entity Model
 * Service (EMS).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsResponse {
  @JsonProperty("_embedded")
  private PointsResults content;

  @JsonProperty("_links")
  private Object links;

  @JsonProperty
  private Page page;
}
