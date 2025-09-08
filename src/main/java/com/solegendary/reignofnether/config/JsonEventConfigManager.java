package com.solegendary.reignofnether.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.events.EventConstants;
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
    private static JsonEventConfig config;

    public static JsonEventConfig loadOrCreateConfig() {
        Path configDirPath = FMLPaths.CONFIGDIR.get().resolve(EventConstants.Config.CONFIG_DIR);
        Path eventsPath = configDirPath.resolve(EventConstants.Config.EVENTS_FILENAME);
        
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
        defaultConfig.events.putAll(EventDefaults.createDefaultEvents());
        return defaultConfig;
    }
    
    private static void loadEventsConfig(Path configDirPath) throws IOException {
        Path eventsPath = configDirPath.resolve(EventConstants.Config.EVENTS_FILENAME);
        Type type = new TypeToken<Map<String, EventEntry>>(){}.getType();
        try (FileReader reader = new FileReader(eventsPath.toFile())) {
            config = new JsonEventConfig();
            config.events = GSON.fromJson(reader, type);
        }
    }

    public static void saveConfig() {
        if (config == null) return;
        
        Path configDirPath = FMLPaths.CONFIGDIR.get().resolve(EventConstants.Config.CONFIG_DIR);
        
        try {
            Files.createDirectories(configDirPath);
            saveEventsConfig(configDirPath);
            ReignOfNether.LOGGER.info("Saved JSON event configuration to {}", configDirPath);
        } catch (IOException e) {
            ReignOfNether.LOGGER.error("Failed to save JSON event config file: {}", e.getMessage());
        }
    }
    
    private static void saveEventsConfig(Path configDirPath) throws IOException {
        Path eventsPath = configDirPath.resolve(EventConstants.Config.EVENTS_FILENAME);
        try (FileWriter writer = new FileWriter(eventsPath.toFile())) {
            GSON.toJson(config.events, writer);
        }
    }

    public static JsonEventConfig getConfig() {
        return config;
    }
}