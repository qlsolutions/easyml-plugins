/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.openmeteo;

import java.util.List;
import java.util.Map;

/**
 * ResponseOpenMeteo - OpenMeteo response POJO.
 *
 * @author Denis Mehilli
 */
public record ResponseOpenMeteo(Map<String, List<?>> hourly) {

//    public record Hourly(List<String> time, List<Double> rain) {}
}