package com.quicklink.plugins.api.providers;


import com.quicklink.plugins.api.KeyParam;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class ProviderContext {

  private final int idApp;
  private final String nameApp;
  private final Map<String, ?> parameters;

  private final long startTs;
  private final long endTs;

  public ProviderContext(int idApp, String nameApp, Map<String, ?> parameters, long startTs, long endTs) {
    this.idApp = idApp;
    this.nameApp = nameApp;
    this.parameters = parameters;
    this.startTs = startTs;
    this.endTs = endTs;
  }

  public ProviderContext(int idApp, String nameApp, Map<String, ?> parameters) {
    this(idApp, nameApp, parameters, -1, -1);
  }

  public int idApp() {
    return idApp;
  }

  public String nameApp() {
    return nameApp;
  }

  public <T> T param(@NotNull KeyParam<T> key) {
    return (T) parameters.get(key.getId());
  }

  public int limit() {
    return (int) parameters.get("limit");
  }

  public Stream<DateRange> dateRangeStream(int field) {
    if (startTs == -1 || endTs == -1) {
      throw new UnsupportedOperationException("Start ts or end ts not set");
    }
    Calendar start = Calendar.getInstance();
    start.setTime(new Date(startTs));
    var iterator = new DateRangeIterator(start, new Date(endTs), limit(), field);
    return StreamUtils.asStream(iterator);
  }

}
