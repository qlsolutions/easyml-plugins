/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

/**
 * DateRangeIterator - Utility class for iterate on data ranges.
 *
 * @author Denis Mehilli
 */
class DateRangeIterator implements Iterator<DateRange> {

  private final Calendar curr;
  private final Date endDate;
  private final int limit;
  private final int field;

  private final Calendar prev;
  private boolean hasNext = true;

  DateRangeIterator(@NotNull Calendar start, @NotNull Date endDate, int limit, int field) {
    this.curr = start;
    this.endDate = endDate;
    this.limit = limit;
    this.field = field;
    this.prev = (Calendar) curr.clone();
  }

  @Override
  public boolean hasNext() {
    return hasNext;
  }

  @Override
  public DateRange next() {
    if (limit == 0) {
      hasNext = false;
      return new DateRange(curr.getTime(), endDate);
    }

    curr.add(field, limit);
    if (curr.getTime().before(endDate)) {
      var range = new DateRange(prev.getTime(), curr.getTime());
      prev.setTime(range.end());
      return range;
    } else {
      hasNext = false;
      return new DateRange(prev.getTime(), endDate);
    }
  }
}
