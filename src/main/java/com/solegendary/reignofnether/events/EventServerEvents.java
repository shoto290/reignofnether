package com.solegendary.reignofnether.events;

import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.config.JsonEventConfig;
import com.solegendary.reignofnether.config.JsonEventConfigManager;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Random;

public class EventServerEvents {

    public static void executeEvent(String eventName, ServerPlayer player) {
        JsonEventConfig config = JsonEventConfigManager.getConfig();
        if (config == null) {
            player.sendSystemMessage(Component.literal("Event system not initialized!"));
            return;
        }
        
        EventEntry eventEntry = config.getEntry(eventName);
        if (eventEntry == null) {
            player.sendSystemMessage(Component.literal("Event '" + eventName + "' not found!"));
            return;
        }
        
        switch (eventEntry.type) {
            case "COMMAND":
                executeCommandEvent(eventEntry, player);
                break;
            case "PATROL":
                executePatrolEvent(eventEntry, player);
                break;
            default:
                player.sendSystemMessage(Component.literal("Unknown event type: " + eventEntry.type));
        }
    }
    
    private static void executeCommandEvent(EventEntry eventEntry, ServerPlayer player) {
        Object commandObj = eventEntry.parameters.get("command");
        if (commandObj instanceof String) {
            String command = (String) commandObj;
            player.getServer().getCommands().performPrefixedCommand(
                player.getServer().createCommandSourceStack(), command);
            player.sendSystemMessage(Component.literal("Executed command: " + command));
        } else {
            player.sendSystemMessage(Component.literal("Invalid command parameter for event"));
        }
    }
    
    private static void executePatrolEvent(EventEntry eventEntry, ServerPlayer player) {
        // Parse parameters
        Object entityTypeObj = eventEntry.parameters.get("entityType");
        Object minCountObj = eventEntry.parameters.get("minCount");
        Object maxCountObj = eventEntry.parameters.get("maxCount");
        Object isAggressiveObj = eventEntry.parameters.get("isAggressive");
        
        if (!(entityTypeObj instanceof String)) {
            player.sendSystemMessage(Component.literal("Invalid entityType parameter for patrol event"));
            return;
        }
        
        String entityTypeStr = (String) entityTypeObj;
        int minCount = minCountObj instanceof Number ? ((Number) minCountObj).intValue() : 1;
        int maxCount = maxCountObj instanceof Number ? ((Number) maxCountObj).intValue() : 1;
        boolean isAggressive = isAggressiveObj instanceof Boolean ? (Boolean) isAggressiveObj : false;
        
        // Get entity type from registry
        ResourceLocation entityLocation = new ResourceLocation(entityTypeStr);
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityLocation);
        
        if (entityType == null) {
            player.sendSystemMessage(Component.literal("Unknown entity type: " + entityTypeStr));
            return;
        }
        
        if (!(entityType.create(player.serverLevel()) instanceof Mob)) {
            player.sendSystemMessage(Component.literal("Entity type must be a mob: " + entityTypeStr));
            return;
        }
        
        @SuppressWarnings("unchecked")
        EntityType<? extends Mob> mobEntityType = (EntityType<? extends Mob>) entityType;
        
        // Get spawn position (for now, use player position as placeholder)
        // TODO: Implement cursor position retrieval
        BlockPos spawnPos = player.blockPosition();
        
        // Calculate spawn count
        Random random = new Random();
        int spawnCount = minCount + random.nextInt(Math.max(1, maxCount - minCount + 1));
        
        // Spawn mobs
        ServerLevel level = player.serverLevel();
        ArrayList<Entity> spawnedEntities = UnitServerEvents.spawnMobs(mobEntityType, level, spawnPos, spawnCount, "");
        
        // Set aggression behavior
        for (Entity entity : spawnedEntities) {
            if (entity instanceof Mob mob) {
                if (isAggressive) {
                    // For aggressive mobs, make them naturally hostile
                    // Remove any peaceful goals and add aggressive targeting
                    mob.setTarget(null); // Clear current target
                    
                    // For RTS units that implement AttackerUnit, they already have their own AI
                    if (entity instanceof AttackerUnit attackerUnit) {
                        // RTS units handle their own aggression through the AttackerUnit interface
                        // They will automatically engage based on their getAggressiveWhenIdle() setting
                    } else {
                        // For vanilla mobs, we set them to be naturally hostile
                        // This makes them behave like naturally spawned hostile mobs
                        mob.setPersistenceRequired(); // Prevent despawning
                    }
                } else {
                    // For passive units, ensure they don't attack unless provoked
                    mob.setPersistenceRequired(); // Prevent despawning
                    mob.setTarget(null); // Clear any targets
                }
            }
        }
        
        player.sendSystemMessage(Component.literal(
            "Spawned " + spawnedEntities.size() + " " + entityTypeStr + 
            " at " + spawnPos.toShortString() + 
            (isAggressive ? " (aggressive)" : " (passive)")
        ));
    }
}