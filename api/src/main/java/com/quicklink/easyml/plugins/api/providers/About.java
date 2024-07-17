/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.easyml.plugins.api.providers;

import org.jetbrains.annotations.Nullable;

/**
 * About - POJO class to check a prover plugin status(online or offline) & version.
 *
 * @author Denis Mehilli
 */
public record About(boolean status, @Nullable String hostId, @Nullable String version) {

}
