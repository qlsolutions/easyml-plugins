/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.niagara.model;

import java.util.List;

/**
 * SeriesModel
 *
 * @author Denis Mehilli
 */
public record SeriesModel(List<SerieModel> series) {

  public record SerieModel(String displayName, String id, List<String> tags) {

  }
}