package com.quicklink.niagara.model;

import java.util.List;

public record SerieDetailsModel(List<Data> data) {

  public record Data(Long timestamp, Double value) {

  }
}
