package com.quicklink.plugins.api.providers;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

public record DateRange(@NotNull Date start, @NotNull Date end) {

}
