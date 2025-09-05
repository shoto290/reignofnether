package com.solegendary.reignofnether.config;

import java.util.HashMap;
import java.util.Map;

public class JsonConfig {
    public Map<String, JsonResourceCostEntry> units = new HashMap<>();
    public Map<String, JsonResourceCostEntry> buildings = new HashMap<>();
    public Map<String, JsonResourceCostEntry> research = new HashMap<>();
    public Map<String, JsonResourceCostEntry> enchantments = new HashMap<>();

    public JsonConfig() {}

    public JsonResourceCostEntry getEntry(String id) {
        String normalizedId = id.replace("reignofnether.", "").toLowerCase();
        if (units.containsKey(normalizedId)) return units.get(normalizedId);
        if (buildings.containsKey(normalizedId)) return buildings.get(normalizedId);
        if (research.containsKey(normalizedId)) return research.get(normalizedId);
        if (enchantments.containsKey(normalizedId)) return enchantments.get(normalizedId);
        return null;
    }
}