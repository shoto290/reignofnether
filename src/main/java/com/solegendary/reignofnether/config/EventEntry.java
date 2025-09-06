package com.solegendary.reignofnether.config;

import java.util.HashMap;
import java.util.Map;

public class EventEntry {
    public String type;
    public String description;
    public Map<String, Object> parameters;
    public AutoEventConfig autoConfig;

    public EventEntry() {
        this.parameters = new HashMap<>();
    }

    public EventEntry(String type, String description, Map<String, Object> parameters) {
        this.type = type;
        this.description = description;
        this.parameters = parameters != null ? parameters : new HashMap<>();
    }

    public EventEntry(String type, String description, Map<String, Object> parameters, AutoEventConfig autoConfig) {
        this.type = type;
        this.description = description;
        this.parameters = parameters != null ? parameters : new HashMap<>();
        this.autoConfig = autoConfig;
    }

    public boolean isAutoEvent() {
        return autoConfig != null;
    }
}