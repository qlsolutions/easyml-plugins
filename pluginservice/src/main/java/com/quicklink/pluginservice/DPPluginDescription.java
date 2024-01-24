package com.quicklink.pluginservice;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public record DPPluginDescription(String name, String version, String main) {

}
