package com.solegendary.reignofnether.events;

public final class EventConstants {
    
    private EventConstants() {}

    public static final int MAX_SPAWN_ATTEMPTS = 20;
    public static final double RARITY_DIVISION_FACTOR = 20.0;
    
    public static final class Defaults {
        private Defaults() {}
        
        public static final int COOLDOWN_SECONDS = 300;
        public static final double RARITY = 0.001;
        public static final boolean ANNOUNCEMENT = true;
        public static final int MIN_DISTANCE = 50;
        public static final int MAX_DISTANCE = 150;
        public static final int REQUIRES_PLAYERS = 1;
        public static final int WORLD_TIME_MIN = 0;
        public static final int WORLD_TIME_MAX = 24000;
        public static final boolean ENABLED_BY_DEFAULT = true;
    }
    
    public static final class Messages {
        private Messages() {}
        
        public static final String EVENT_SYSTEM_NOT_INITIALIZED = "Event system not initialized!";
        public static final String EVENT_NOT_FOUND = "Event '%s' not found!";
        public static final String UNKNOWN_EVENT_TYPE = "Unknown event type: %s";
        public static final String NO_PERMISSION = "You don't have permission to use this command (OP required)";
        public static final String INVALID_COMMAND_PARAMETER = "Invalid command parameter for event";
        public static final String INVALID_ENTITY_TYPE = "Invalid entityType parameter for patrol event";
        public static final String UNKNOWN_ENTITY_TYPE = "Unknown entity type: %s";
        public static final String ENTITY_MUST_BE_MOB = "Entity type must be a mob: %s";
        public static final String AUTO_EVENT_MISSING_SUBTYPE = "Auto event missing subType parameter";
        public static final String UNKNOWN_AUTO_EVENT_SUBTYPE = "Unknown auto event subType: %s";
        public static final String EXECUTED_COMMAND = "Executed command: %s";
        public static final String SPAWNED_ENTITIES = "Spawned %d %s at %s%s";
        public static final String AGGRESSIVE_SUFFIX = " (aggressive)";
        public static final String PASSIVE_SUFFIX = " (passive)";
        public static final String EVENT_TRIGGERED = "Event '%s' triggered at %d, %d, %d";
    }
    
    public static final class Time {
        private Time() {}
        
        public static final long MILLISECONDS_PER_SECOND = 1000L;
        public static final int TICKS_PER_DAY = 24000;
    }
    
    public static final class Config {
        private Config() {}
        
        public static final String CONFIG_DIR = "reignofnether";
        public static final String EVENTS_FILENAME = "events.json";
    }
}