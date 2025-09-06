package com.solegendary.reignofnether.events;

import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.config.JsonEventConfig;
import com.solegendary.reignofnether.config.JsonEventConfigManager;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.levelgen.Heightmap;
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
        
        BlockPos playerPos = player.blockPosition();
        ServerLevel level = player.serverLevel();
        
        int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, playerPos.getX(), playerPos.getZ());
        BlockPos spawnPos = new BlockPos(playerPos.getX(), groundY, playerPos.getZ());
        
        Random random = new Random();
        int spawnCount = minCount + random.nextInt(Math.max(1, maxCount - minCount + 1));
        
        ArrayList<Entity> spawnedEntities = UnitServerEvents.spawnMobs(mobEntityType, level, spawnPos, spawnCount, "");
        
        for (Entity entity : spawnedEntities) {
            if (entity instanceof Mob mob) {
                if (isAggressive) {
                    mob.setTarget(null);
                    
                    if (entity instanceof AttackerUnit attackerUnit) {
                        // RTS units handle their own aggression through the AttackerUnit interface
                        // They will automatically engage based on their getAggressiveWhenIdle() setting
                    } else {
                        // For vanilla mobs, add aggressive AI goals to attack RTS units and players
                        mob.setPersistenceRequired(); // Prevent despawning
                        
                        // Add targeting goals to make them actively seek and attack RTS units
                        // Only add goals if this mob is a PathfinderMob (most vanilla hostile mobs are)
                        if (mob instanceof PathfinderMob pathfinderMob) {
                            pathfinderMob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(pathfinderMob, LivingEntity.class, 
                                true, target -> target instanceof Unit || target instanceof net.minecraft.world.entity.player.Player));
                            pathfinderMob.targetSelector.addGoal(2, new HurtByTargetGoal(pathfinderMob));
                        }
                    }
                } else {
                    mob.setPersistenceRequired(); 
                    mob.setTarget(null); 
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