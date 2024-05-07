package com.quicklink.plugins.api.providers;

import org.jetbrains.annotations.Nullable;

public record About(boolean status, @Nullable String hostId, @Nullable String version) {

}
