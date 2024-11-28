/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.hooks;


import com.quicklink.easyml.plugins.api.Parameter;
import com.quicklink.easyml.plugins.api.ParameterImpl;
import java.time.Instant;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * HookContext - Context received by easmly-app on anomaly detection.
 *
 * @author Denis Mehilli
 */
public class HookContext {

  private final int hookId;
  private final int modelId;
  private final Instant timestamp;
  private final String status;
  private final Double predicted;
  private final Double minPredicted;
  private final Double maxPredicted;
  private final Double observed;

  private final Map<String, ?> parameters;


  public HookContext(
      int hookId,
      @NotNull Integer modelId,
      @NotNull Instant timestamp,
      @NotNull String status,
      @NotNull Double predicted,
      @NotNull Double minPredicted,
      @NotNull Double maxPredicted,
      @NotNull Double observed,
      @NotNull Map<String, ?> parameters
  ) {
    this.hookId = hookId;
    this.modelId = modelId;
    this.timestamp = timestamp;
    this.status = status;
    this.predicted = predicted;
    this.minPredicted = minPredicted;
    this.maxPredicted = maxPredicted;
    this.observed = observed;
    this.parameters = parameters;

  }

  public int getHookId() {
    return hookId;
  }

  public @NotNull Integer getModelId() {
    return modelId;
  }

  public @NotNull Instant getTimestamp() {
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

  public @Nullable <T> T param(@NotNull Parameter<T> key) {
    return (T) parameters.getOrDefault(key.getKey(), null);
  }

  public @NotNull String parseString(@NotNull String s) {
    return s
        .replaceAll("\\{model}", "" + modelId)
        .replaceAll("\\{timestamp}", "" + timestamp.toEpochMilli())
        .replaceAll("\\{anomalyStatus}", status)
        .replaceAll("\\{predicted}", "" + predicted)
        .replaceAll("\\{max-predicted}", "" + maxPredicted)
        .replaceAll("\\{min-predicted}", "" + minPredicted)
        .replaceAll("\\{observed}", "" + observed)
        ;
  }
}
