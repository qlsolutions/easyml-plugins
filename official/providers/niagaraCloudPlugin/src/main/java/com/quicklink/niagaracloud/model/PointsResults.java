package com.quicklink.niagaracloud.model;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmsQueryResults models the results of the query of the Entity Model
 * Service (EMS).  The result is a list of point objects.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsResults {
  @JsonProperty("pointModels")
  private List<Point> points = new ArrayList<>();
}
