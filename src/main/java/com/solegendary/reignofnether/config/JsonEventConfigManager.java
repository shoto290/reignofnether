package com.solegendary.reignofnether.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.solegendary.reignofnether.ReignOfNether;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class JsonEventConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_DIR = "reignofnether";
    private static final String EVENTS_FILENAME = "events.json";
    private static JsonEventConfig config;

    public static JsonEventConfig loadOrCreateConfig() {
        Path configDirPath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_DIR);
        Path eventsPath = configDirPath.resolve(EVENTS_FILENAME);
        
        if (Files.exists(eventsPath)) {
            try {
                loadEventsConfig(configDirPath);
                ReignOfNether.LOGGER.info("Loaded JSON event configuration from {}", eventsPath);
                return config;
            } catch (IOException e) {
                ReignOfNether.LOGGER.error("Failed to read JSON event config file: {}", e.getMessage());
            }
        }

        config = createDefaultConfig();
        saveConfig();
        return config;
    }

    public static JsonEventConfig createDefaultConfig() {
        JsonEventConfig defaultConfig = new JsonEventConfig();
        
        Map<String, Object> pingParameters = new HashMap<>();
        pingParameters.put("command", "say PING");
        
        Map<String, Object> wolfPatrolParameters = new HashMap<>();
        wolfPatrolParameters.put("entityType", "minecraft:wolf");
        wolfPatrolParameters.put("minCount", 2);
        wolfPatrolParameters.put("maxCount", 5);
        wolfPatrolParameters.put("isAggressive", true);
        
        Map<String, Object> zombiePatrolParameters = new HashMap<>();
        zombiePatrolParameters.put("entityType", "minecraft:zombie");
        zombiePatrolParameters.put("minCount", 3);
        zombiePatrolParameters.put("maxCount", 7);
        zombiePatrolParameters.put("isAggressive", true);
        
        Map<String, Object> peacefulPatrolParameters = new HashMap<>();
        peacefulPatrolParameters.put("entityType", "minecraft:cow");
        peacefulPatrolParameters.put("minCount", 1);
        peacefulPatrolParameters.put("maxCount", 3);
        peacefulPatrolParameters.put("isAggressive", false);
        
        defaultConfig.events.put("ping", new EventEntry("COMMAND", "Debug ping event", pingParameters));
        defaultConfig.events.put("wolf_patrol", new EventEntry("PATROL", "Spawn aggressive wolf patrol", wolfPatrolParameters));
        defaultConfig.events.put("zombie_horde", new EventEntry("PATROL", "Spawn zombie horde", zombiePatrolParameters));
        defaultConfig.events.put("peaceful_animals", new EventEntry("PATROL", "Spawn peaceful animals", peacefulPatrolParameters));
        
        addAutoEvents(defaultConfig);
        
        return defaultConfig;
    }
    
    private static void addAutoEvents(JsonEventConfig config) {
        Map<String, Object> wolfPatrolParams = new HashMap<>();
        wolfPatrolParams.put("subType", "PATROL");
        wolfPatrolParams.put("entityType", "minecraft:wolf");
        wolfPatrolParams.put("minCount", 2);
        wolfPatrolParams.put("maxCount", 5);
        wolfPatrolParams.put("isAggressive", true);
        
        AutoEventConfig wolfPatrolAutoConfig = new AutoEventConfig(
            10,
            1.0,
            true,
            20,
            100,
            1,
            null,
            null,
            null,
            true
        );
        
        config.events.put("wolf_patrol", new EventEntry("AUTO", "Spawn aggressive wolf patrol", 
                                                        wolfPatrolParams, wolfPatrolAutoConfig));
    }
    
    private static void loadEventsConfig(Path configDirPath) throws IOException {
        Path eventsPath = configDirPath.resolve(EVENTS_FILENAME);
        Type type = new TypeToken<Map<String, EventEntry>>(){}.getType();
        try (FileReader reader = new FileReader(eventsPath.toFile())) {
            config = new JsonEventConfig();
            config.events = GSON.fromJson(reader, type);
        }
    }

    public static void saveConfig() {
        if (config == null) return;
        
        Path configDirPath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_DIR);
        
        try {
            Files.createDirectories(configDirPath);
            saveEventsConfig(configDirPath);
            ReignOfNether.LOGGER.info("Saved JSON event configuration to {}", configDirPath);
        } catch (IOException e) {
            ReignOfNether.LOGGER.error("Failed to save JSON event config file: {}", e.getMessage());
        }
    }
    
    private static void saveEventsConfig(Path configDirPath) throws IOException {
        Path eventsPath = configDirPath.resolve(EVENTS_FILENAME);
        try (FileWriter writer = new FileWriter(eventsPath.toFile())) {
            GSON.toJson(config.events, writer);
        }
    }

    public static JsonEventConfig getConfig() {
        return config;
    }
}