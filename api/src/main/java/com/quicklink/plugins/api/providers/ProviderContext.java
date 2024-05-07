package com.quicklink.plugins.api.providers;


import com.quicklink.plugins.api.KeyParam;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProviderContext {

  private final int idApp;
  private final String nameApp;
  private final Map<String, ?> parameters;

  @Nullable private final Long startTs;
  @Nullable private final Long endTs;

  public ProviderContext(int idApp, @NotNull String nameApp, @NotNull Map<String, ?> parameters, @Nullable Long startTs, @Nullable Long endTs) {
    this.idApp = idApp;
    this.nameApp = nameApp;
    this.parameters = parameters;
    this.startTs = startTs;
    this.endTs = endTs;
  }

  public ProviderContext(int idApp, @NotNull String nameApp, @NotNull Map<String, ?> parameters) {
    this(idApp, nameApp, parameters, null, null);
  }

  public int idApp() {
    return idApp;
  }

  public @NotNull String nameApp() {
    return nameApp;
  }

  public @Nullable<T> T param(@NotNull KeyParam<T> key) {
    return (T) parameters.getOrDefault(key.getId(), null);
  }

  public int limit() {
    return (int) parameters.get("limit");
  }

  public Stream<DateRange> dateRangeStream(int field) {
    if (startTs == null || endTs == null) {
      throw new UnsupportedOperationException("Start ts or end ts not set");
    }
    Calendar start = Calendar.getInstance();
    start.setTime(new Date(startTs));
    var iterator = new DateRangeIterator(start, new Date(endTs), limit(), field);
    return StreamUtils.asStream(iterator);
  }

}
