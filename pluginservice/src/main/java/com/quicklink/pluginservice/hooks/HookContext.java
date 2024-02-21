package com.quicklink.pluginservice.hooks;



import com.quicklink.pluginservice.KeyParam;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class HookContext {

  private final Integer modelId;
  private final Long timestamp;
  private final String status;
  private final Double predicted;
  private final Double minPredicted;
  private final Double maxPredicted;
  private final Double observed;

  private final Map<String, ?> parameters;


  public HookContext(
      Integer modelId,
      Long timestamp,
      String status,
      Double predicted,
      Double minPredicted,
      Double maxPredicted,
      Double observed,
      Map<String, ?> parameters
  ) {
    this.modelId = modelId;
    this.timestamp = timestamp;
    this.status = status;
    this.predicted = predicted;
    this.minPredicted = minPredicted;
    this.maxPredicted = maxPredicted;
    this.observed = observed;
    this.parameters = parameters;

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

  public Double getPredicted() {
    return predicted;
  }

  public Double getMinPredicted() {
    return minPredicted;
  }

  public Double getMaxPredicted() {
    return maxPredicted;
  }

  public Double getObserved() {
    return observed;
  }

  public <T> T param(@NotNull KeyParam<T> key) {
    return (T) parameters.get(key.getId());
  }

  public @NotNull String parseString(@NotNull String s) {
    return s
      .replaceAll("\\{model}", "" + modelId)
      .replaceAll("\\{timestamp}", "" + timestamp)
      .replaceAll("\\{anomalyStatus}", status)
      .replaceAll("\\{predicted}", "" + predicted)
      .replaceAll("\\{max-predicted}", "" + maxPredicted)
      .replaceAll("\\{min-predicted}","" + minPredicted)
      .replaceAll("\\{observed}", "" + observed)
      ;
  }
}
