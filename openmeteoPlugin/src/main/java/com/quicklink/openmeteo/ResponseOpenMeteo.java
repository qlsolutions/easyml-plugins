package com.quicklink.openmeteo;

import java.util.List;
import java.util.Map;

public record ResponseOpenMeteo(Map<String, List<?>> hourly) {

//    public record Hourly(List<String> time, List<Double> rain) {}
}