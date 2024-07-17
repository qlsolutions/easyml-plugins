/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

/**
 * DateRange - Date range for requesting data.
 *
 * @author Denis Mehilli
 */
public record DateRange(@NotNull Date start, @NotNull Date end) {

}
