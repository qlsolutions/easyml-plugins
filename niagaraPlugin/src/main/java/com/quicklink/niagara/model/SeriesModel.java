package com.quicklink.niagara.model;

import java.util.List;

public record SeriesModel(List<SerieModel> series) {

  public record SerieModel(String displayName, String id, List<String> tags) {

  }
}