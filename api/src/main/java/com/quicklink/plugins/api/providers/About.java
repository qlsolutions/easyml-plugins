package com.quicklink.plugins.api.providers;

import org.jetbrains.annotations.NotNull;

public record About(boolean status, @NotNull String hostId, @NotNull String version) {

}
