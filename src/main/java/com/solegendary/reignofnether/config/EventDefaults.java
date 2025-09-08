package com.solegendary.reignofnether.config;

import java.util.HashMap;
import java.util.Map;

public final class EventDefaults {
    
    private EventDefaults() {}

    public static Map<String, EventEntry> createDefaultEvents() {
        Map<String, EventEntry> events = new HashMap<>();
        
        events.put("ping", createPingEvent());
        events.put("wolf_patrol", createWolfPatrolEvent());
        events.put("zombie_horde", createZombieHordeEvent());
        events.put("peaceful_animals", createPeacefulAnimalsEvent());
        
        return events;
    }

    private static EventEntry createPingEvent() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("command", "say PING");
        return new EventEntry("COMMAND", "Debug ping event", parameters);
    }

    private static EventEntry createWolfPatrolEvent() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("subType", "PATROL");
        parameters.put("entityType", "minecraft:wolf");
        parameters.put("minCount", 2);
        parameters.put("maxCount", 5);
        parameters.put("isAggressive", true);
        
        AutoEventConfig autoConfig = new AutoEventConfig.Builder()
            .cooldown(10)
            .rarity(1.0)
            .announcement(true)
            .minDistance(20)
            .maxDistance(100)
            .requiresPlayers(1)
            .enabledByDefault(true)
            .build();
        
        return new EventEntry("AUTO", "Spawn aggressive wolf patrol", parameters, autoConfig);
    }

    private static EventEntry createZombieHordeEvent() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("entityType", "minecraft:zombie");
        parameters.put("minCount", 3);
        parameters.put("maxCount", 7);
        parameters.put("isAggressive", true);
        
        return new EventEntry("PATROL", "Spawn zombie horde", parameters);
    }

    private static EventEntry createPeacefulAnimalsEvent() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("entityType", "minecraft:cow");
        parameters.put("minCount", 1);
        parameters.put("maxCount", 3);
        parameters.put("isAggressive", false);
        
        return new EventEntry("PATROL", "Spawn peaceful animals", parameters);
    }
}