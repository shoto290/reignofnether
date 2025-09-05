package com.solegendary.reignofnether.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solegendary.reignofnether.ReignOfNether;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILENAME = "reignofnether-config.json";
    private static JsonConfig config;

    public static JsonConfig loadOrCreateConfig() {
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILENAME);
        
        if (Files.exists(configPath)) {
            try (FileReader reader = new FileReader(configPath.toFile())) {
                config = GSON.fromJson(reader, JsonConfig.class);
                ReignOfNether.LOGGER.info("Loaded JSON configuration from {}", configPath);
                return config;
            } catch (IOException e) {
                ReignOfNether.LOGGER.error("Failed to read JSON config file: {}", e.getMessage());
            }
        }

        config = createDefaultConfig();
        saveConfig();
        return config;
    }

    private static JsonConfig createDefaultConfig() {
        JsonConfig defaultConfig = new JsonConfig();
        
        // Units
        defaultConfig.units.put("creeper", new JsonResourceCostEntry(50, 0, 100, 35, 2));
        defaultConfig.units.put("zombie", new JsonResourceCostEntry(75, 0, 0, 18, 1));
        defaultConfig.units.put("zombie_villager", new JsonResourceCostEntry(50, 0, 0, 15, 1));
        defaultConfig.units.put("skeleton", new JsonResourceCostEntry(50, 45, 0, 18, 1));
        defaultConfig.units.put("stray", new JsonResourceCostEntry(50, 45, 0, 18, 1));
        defaultConfig.units.put("husk", new JsonResourceCostEntry(75, 0, 0, 18, 1));
        defaultConfig.units.put("drowned", new JsonResourceCostEntry(75, 0, 0, 18, 1));
        defaultConfig.units.put("spider", new JsonResourceCostEntry(90, 25, 25, 25, 2));
        defaultConfig.units.put("poison_spider", new JsonResourceCostEntry(90, 25, 25, 25, 2));
        defaultConfig.units.put("slime", new JsonResourceCostEntry(40, 40, 40, 25, 2));
        defaultConfig.units.put("warden", new JsonResourceCostEntry(275, 0, 125, 50, 5));
        defaultConfig.units.put("zombie_piglin", new JsonResourceCostEntry(0, 0, 0, 10, 1));
        defaultConfig.units.put("zoglin", new JsonResourceCostEntry(0, 0, 0, 10, 2));
        defaultConfig.units.put("necromancer", new JsonResourceCostEntry(0, 0, 0, 30, 5));
        
        // Villagers
        defaultConfig.units.put("villager", new JsonResourceCostEntry(50, 0, 0, 15, 1));
        defaultConfig.units.put("militia", new JsonResourceCostEntry(50, 0, 0, 15, 1));
        defaultConfig.units.put("iron_golem", new JsonResourceCostEntry(0, 50, 250, 45, 4));
        defaultConfig.units.put("pillager", new JsonResourceCostEntry(120, 80, 0, 32, 3));
        defaultConfig.units.put("vindicator", new JsonResourceCostEntry(170, 0, 0, 32, 3));
        defaultConfig.units.put("witch", new JsonResourceCostEntry(90, 90, 90, 35, 3));
        defaultConfig.units.put("evoker", new JsonResourceCostEntry(150, 0, 120, 35, 3));
        defaultConfig.units.put("ravager", new JsonResourceCostEntry(400, 50, 150, 60, 7));
        defaultConfig.units.put("royal_guard", new JsonResourceCostEntry(0, 0, 0, 30, 5));
        
        // Piglins
        defaultConfig.units.put("grunt", new JsonResourceCostEntry(50, 0, 0, 15, 1));
        defaultConfig.units.put("brute", new JsonResourceCostEntry(120, 0, 0, 25, 2));
        defaultConfig.units.put("headhunter", new JsonResourceCostEntry(90, 60, 0, 25, 2));
        defaultConfig.units.put("hoglin", new JsonResourceCostEntry(150, 0, 75, 35, 3));
        defaultConfig.units.put("blaze", new JsonResourceCostEntry(40, 40, 100, 30, 2));
        defaultConfig.units.put("wither_skeleton", new JsonResourceCostEntry(200, 0, 125, 40, 4));
        defaultConfig.units.put("ghast", new JsonResourceCostEntry(100, 100, 250, 60, 6));
        defaultConfig.units.put("magma_cube", new JsonResourceCostEntry(40, 40, 40, 25, 2));
        defaultConfig.units.put("piglin_merchant", new JsonResourceCostEntry(0, 0, 0, 30, 5));
        
        // Neutral
        defaultConfig.units.put("enderman", new JsonResourceCostEntry(75, 75, 75, 35, 3));
        defaultConfig.units.put("polar_bear", new JsonResourceCostEntry(250, 0, 0, 40, 4));
        defaultConfig.units.put("grizzly_bear", new JsonResourceCostEntry(250, 0, 0, 40, 4));
        defaultConfig.units.put("panda", new JsonResourceCostEntry(250, 0, 0, 40, 4));
        defaultConfig.units.put("wolf", new JsonResourceCostEntry(120, 0, 0, 25, 2));
        defaultConfig.units.put("llama", new JsonResourceCostEntry(180, 0, 0, 25, 2));
        
        defaultConfig.units.put("hero_base_revive_cost", new JsonResourceCostEntry(100, 0, 0, 30, 5));
        defaultConfig.units.put("hero_extra_revive_cost_per_level", new JsonResourceCostEntry(50, 0, 0, 5, 0));
        
        // Buildings
        defaultConfig.buildings.put("beacon", new JsonResourceCostEntry(0, 1000, 1000, 0, 0));
        defaultConfig.buildings.put("stockpile", new JsonResourceCostEntry(0, 75, 0, 0, 0));
        defaultConfig.buildings.put("oak_bridge", new JsonResourceCostEntry(0, 100, 0, 0, 0));
        defaultConfig.buildings.put("spruce_bridge", new JsonResourceCostEntry(0, 100, 0, 0, 0));
        defaultConfig.buildings.put("blackstone_bridge", new JsonResourceCostEntry(0, 0, 100, 0, 0));
        
        // Monster Buildings
        defaultConfig.buildings.put("mausoleum", new JsonResourceCostEntry(0, 350, 250, 0, 10));
        defaultConfig.buildings.put("haunted_house", new JsonResourceCostEntry(0, 100, 0, 0, 10));
        defaultConfig.buildings.put("pumpkin_farm", new JsonResourceCostEntry(0, 200, 0, 0, 0));
        defaultConfig.buildings.put("sculk_catalyst", new JsonResourceCostEntry(0, 125, 0, 0, 0));
        defaultConfig.buildings.put("graveyard", new JsonResourceCostEntry(0, 150, 0, 0, 0));
        defaultConfig.buildings.put("spider_lair", new JsonResourceCostEntry(0, 150, 75, 0, 0));
        defaultConfig.buildings.put("dungeon", new JsonResourceCostEntry(0, 150, 75, 0, 0));
        defaultConfig.buildings.put("laboratory", new JsonResourceCostEntry(0, 250, 150, 0, 0));
        defaultConfig.buildings.put("dark_watchtower", new JsonResourceCostEntry(0, 100, 75, 0, 0));
        defaultConfig.buildings.put("slime_pit", new JsonResourceCostEntry(0, 175, 125, 0, 0));
        defaultConfig.buildings.put("stronghold", new JsonResourceCostEntry(0, 400, 300, 0, 0));
        defaultConfig.buildings.put("altar_of_darkness", new JsonResourceCostEntry(0, 125, 50, 0, 0));
        
        // Villager Buildings
        defaultConfig.buildings.put("town_centre", new JsonResourceCostEntry(0, 350, 250, 0, 10));
        defaultConfig.buildings.put("villager_house", new JsonResourceCostEntry(0, 100, 0, 0, 10));
        defaultConfig.buildings.put("wheat_farm", new JsonResourceCostEntry(0, 150, 0, 0, 0));
        defaultConfig.buildings.put("barracks", new JsonResourceCostEntry(0, 150, 0, 0, 0));
        defaultConfig.buildings.put("blacksmith", new JsonResourceCostEntry(0, 100, 300, 0, 0));
        defaultConfig.buildings.put("arcane_tower", new JsonResourceCostEntry(0, 200, 100, 0, 0));
        defaultConfig.buildings.put("library", new JsonResourceCostEntry(0, 300, 100, 0, 0));
        defaultConfig.buildings.put("watchtower", new JsonResourceCostEntry(0, 100, 75, 0, 0));
        defaultConfig.buildings.put("castle", new JsonResourceCostEntry(0, 400, 300, 0, 0));
        defaultConfig.buildings.put("iron_golem_building", new JsonResourceCostEntry(0, 50, 250, 0, 0));
        defaultConfig.buildings.put("shrine_of_prosperity", new JsonResourceCostEntry(0, 125, 50, 0, 0));
        
        // Piglin Buildings
        defaultConfig.buildings.put("central_portal", new JsonResourceCostEntry(0, 350, 250, 0, 10));
        defaultConfig.buildings.put("basic_portal", new JsonResourceCostEntry(0, 75, 0, 0, 0));
        defaultConfig.buildings.put("civilian_portal", new JsonResourceCostEntry(0, 75, 0, 0, 15));
        defaultConfig.buildings.put("netherwart_farm", new JsonResourceCostEntry(0, 150, 0, 0, 0));
        defaultConfig.buildings.put("bastion", new JsonResourceCostEntry(0, 150, 100, 0, 0));
        defaultConfig.buildings.put("hoglin_stables", new JsonResourceCostEntry(0, 250, 0, 0, 0));
        defaultConfig.buildings.put("flame_sanctuary", new JsonResourceCostEntry(0, 300, 150, 0, 0));
        defaultConfig.buildings.put("wither_shrine", new JsonResourceCostEntry(0, 350, 200, 0, 0));
        defaultConfig.buildings.put("basalt_springs", new JsonResourceCostEntry(0, 200, 200, 0, 0));
        defaultConfig.buildings.put("fortress", new JsonResourceCostEntry(0, 400, 300, 0, 0));
        defaultConfig.buildings.put("infernal_portal", new JsonResourceCostEntry(0, 125, 50, 0, 0));
        
        // Research (sample - add all research items)
        defaultConfig.research.put("golem_smithing", new JsonResourceCostEntry(0, 150, 200, 90, 0));
        defaultConfig.research.put("militia_bows", new JsonResourceCostEntry(250, 500, 0, 160, 0));
        defaultConfig.research.put("lab_lightning_rod", new JsonResourceCostEntry(0, 0, 400, 120, 0));
        // Add more research items...
        
        // Enchantments
        defaultConfig.enchantments.put("maiming", new JsonResourceCostEntry(0, 20, 30, 0, 0));
        defaultConfig.enchantments.put("quick_charge", new JsonResourceCostEntry(0, 40, 20, 0, 0));
        defaultConfig.enchantments.put("sharpness", new JsonResourceCostEntry(0, 40, 60, 0, 0));
        defaultConfig.enchantments.put("multishot", new JsonResourceCostEntry(0, 70, 35, 0, 0));
        defaultConfig.enchantments.put("vigor", new JsonResourceCostEntry(0, 60, 60, 0, 0));
        
        return defaultConfig;
    }


    public static void saveConfig() {
        if (config == null) return;
        
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILENAME);
        
        try (FileWriter writer = new FileWriter(configPath.toFile())) {
            GSON.toJson(config, writer);
            ReignOfNether.LOGGER.info("Saved JSON configuration to {}", configPath);
        } catch (IOException e) {
            ReignOfNether.LOGGER.error("Failed to save JSON config file: {}", e.getMessage());
        }
    }

    public static JsonConfig getConfig() {
        return config;
    }
}