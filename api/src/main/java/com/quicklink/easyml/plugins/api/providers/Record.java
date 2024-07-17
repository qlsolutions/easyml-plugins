/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;

import org.jetbrains.annotations.NotNull;

/**
 * Record - A certain value at specific instant.
 *
 * @author Denis Mehilli
 */
public record Record(@NotNull Long timestamp, @NotNull Double value) {

}
