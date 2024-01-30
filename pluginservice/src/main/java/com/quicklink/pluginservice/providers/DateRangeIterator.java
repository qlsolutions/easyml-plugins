package com.quicklink.pluginservice.providers;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

class DateRangeIterator implements Iterator<DateRange> {

  private final Calendar curr;
  private final Date endDate;
  private final int limit;
  private final int field;

  private final Calendar prev;
  private boolean hasNext = true;

  DateRangeIterator(Calendar start, Date endDate, int limit, int field) {
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
