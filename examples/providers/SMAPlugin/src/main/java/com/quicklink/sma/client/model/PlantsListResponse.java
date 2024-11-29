package com.quicklink.sma.client.model;

import java.util.List;

/**
 * PlantsListResponse - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 03/10/2024
 */
public record PlantsListResponse(List<Plant> plants) {

  public record Plant(String plantId, String name, String timezone) {}


}
