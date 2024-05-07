package com.quicklink.plugins.api.hooks;



import com.quicklink.plugins.api.KeyParam;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
      @NotNull Integer modelId,
      @NotNull Long timestamp,
      @NotNull String status,
      @NotNull Double predicted,
      @NotNull Double minPredicted,
      @NotNull Double maxPredicted,
      @NotNull Double observed,
      @NotNull Map<String, ?> parameters
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

  public @NotNull Integer getModelId() {
    return modelId;
  }

  public @NotNull Long getTimestamp() {
    return timestamp;
  }

  public @NotNull String getStatus() {
    return status;
  }

  public @NotNull Double getPredicted() {
    return predicted;
  }

  public @NotNull Double getMinPredicted() {
    return minPredicted;
  }

  public @NotNull Double getMaxPredicted() {
    return maxPredicted;
  }

  public @NotNull Double getObserved() {
    return observed;
  }

  public @Nullable <T> T param(@NotNull KeyParam<T> key) {
    return (T) parameters.getOrDefault(key.getId(), null);
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
