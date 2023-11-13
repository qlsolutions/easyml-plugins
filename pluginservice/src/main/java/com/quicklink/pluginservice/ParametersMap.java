package com.quicklink.pluginservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ParametersMap {

  final List<Parameter> parameters;

  private ParametersMap(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    public BuilderLimit limit(int limit, String description) {
      return new BuilderLimit(limit, description);
    }

    public BuilderLimit nolimit() {
      return new BuilderLimit(0, "");
    }
  }

  public static final class BuilderLimit {

    private final int limit;
    private final String limitDescription;

    private static final Pattern keyPattern = Pattern.compile("[a-zA-Z]+");
    final List<Parameter> parameters = new ArrayList<>();

    public BuilderLimit(int limit, String limitDescription) {
      this.limit = limit;
      this.limitDescription = limitDescription;

      parameters.add(0, new Parameter("limit", "int", limit, limitDescription));
    }

    public BuilderLimit param(String key, String v, String description) {
      isValidKey(key);
      parameters.add(new Parameter(key, "string", v, description));
      return this;
    }

    public BuilderLimit param(String key, String v) {
      param(key, v, "");
      return this;
    }

    public BuilderLimit param(String key, int v, String description) {
      isValidKey(key);
      parameters.add(new Parameter(key, "int", v, description));
      return this;
    }

    public BuilderLimit param(String key, int v) {
      param(key, v, "");
      return this;
    }

    public BuilderLimit param(String key, double v, String description) {
      isValidKey(key);
      parameters.add(new Parameter(key, "float64", v, description));
      return this;
    }

    public BuilderLimit param(String key, double v) {
      param(key, v, "");
      return this;
    }

    public BuilderLimit secret(String key, String v, String description) {
      isValidKey(key);
      parameters.add(new Parameter(key, "secret", v, description));
      return this;
    }

    public BuilderLimit secret(String key, String v) {
      secret(key, v, "");
      return this;
    }

    private void isValidKey(String key) {
      if (!keyPattern.matcher(key).matches()) {
        throw new InvalidParameterException("Invalid parameter key '%s'".formatted(key));
      }
    }

    public ParametersMap build() {
      return new ParametersMap(parameters);
    }
  }

  public static class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String message) {
      super(message);
    }
  }

  public record Parameter(String key, String type, Object defaultValue,
                          String description) implements Serializable {

    @Override
    protected Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
}
