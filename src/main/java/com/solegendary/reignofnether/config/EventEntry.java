package com.solegendary.reignofnether.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EventEntry {
    public final String type;
    public final String description;
    public final Map<String, Object> parameters;
    public final AutoEventConfig autoConfig;

    public EventEntry() {
        this.type = null;
        this.description = null;
        this.parameters = Collections.unmodifiableMap(new HashMap<>());
        this.autoConfig = null;
    }

    public EventEntry(String type, String description, Map<String, Object> parameters) {
        this.type = type;
        this.description = description;
        this.parameters = parameters != null ? 
            Collections.unmodifiableMap(new HashMap<>(parameters)) : 
            Collections.unmodifiableMap(new HashMap<>());
        this.autoConfig = null;
    }

    public EventEntry(String type, String description, Map<String, Object> parameters, AutoEventConfig autoConfig) {
        this.type = type;
        this.description = description;
        this.parameters = parameters != null ? 
            Collections.unmodifiableMap(new HashMap<>(parameters)) : 
            Collections.unmodifiableMap(new HashMap<>());
        this.autoConfig = autoConfig;
    }

    public boolean isAutoEvent() {
        return autoConfig != null;
    }
}