package com.quicklink.niagaracloud.history;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TsdbQueryResponse models the response from the Data Egress Service for
 * telemetry data queries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
  @JsonProperty
  private String systemGuid;

  @JsonProperty
  private int recordLimit;

  @JsonProperty
  private List<PointData> pointDetails;
}
