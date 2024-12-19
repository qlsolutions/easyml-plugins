package com.quicklink.sma.client.model;

import java.util.List;
import java.util.Map;

/**
 * SetsValuesResponse - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 03/10/2024
 */
public record DataResponse(List<SetValue> set) {

  public record SetValue(String time, Map<String, Double> unknown) {

  }
}
