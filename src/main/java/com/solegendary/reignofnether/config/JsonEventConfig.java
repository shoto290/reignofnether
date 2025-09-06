package com.solegendary.reignofnether.config;

import java.util.HashMap;
import java.util.Map;

public class JsonEventConfig {
    public Map<String, EventEntry> events = new HashMap<>();

    public JsonEventConfig() {}

    public EventEntry getEntry(String id) {
        String normalizedId = id.replace("reignofnether.", "").toLowerCase();
        return events.get(normalizedId);
    }
}