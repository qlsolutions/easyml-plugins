/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.niagara.model;

import java.util.List;

/**
 * SerieDetailModel
 *
 * @author Denis Mehilli
 */
public record SerieDetailsModel(List<Data> data) {

  public record Data(Long timestamp, Double value) {

  }
}
