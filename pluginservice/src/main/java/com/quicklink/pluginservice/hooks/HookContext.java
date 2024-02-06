package com.quicklink.pluginservice.hooks;



import com.quicklink.pluginservice.KeyParam;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class HookContext {

  private final Integer modelId;
  private final Long timestamp;
  private final String status;
  private final Double minPredicted;
  private final Double maxPredicted;
  private final Double observer;

  private final Map<String, ?> parameters;


  public HookContext(
      Integer modelId,
      Long timestamp,
      String status,
      Double minPredicted,
      Double maxPredicted,
      Double observer,
      Map<String, ?> parameters
  ) {
    this.parameters = parameters;
    this.modelId = modelId;
    this.timestamp = timestamp;
    this.status = status;
    this.minPredicted = minPredicted;
    this.maxPredicted = maxPredicted;
    this.observer = observer;
  }

  public Integer getModelId() {
    return modelId;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public String getStatus() {
    return status;
  }

  public Double getMinPredicted() {
    return minPredicted;
  }

  public Double getMaxPredicted() {
    return maxPredicted;
  }

  public Double getObserver() {
    return observer;
  }

  public <T> T param(@NotNull KeyParam<T> key) {
    return (T) parameters.get(key.getId());
  }

}
