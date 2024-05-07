package com.quicklink.plugins.api.providers;

import java.util.Arrays;
import java.util.List;

public record Serie(String id, String diplayName, List<String> tags) {

  public Serie(String id, String diplayName, String... tags) {
    this(id, diplayName, Arrays.asList(tags));
  }
}
