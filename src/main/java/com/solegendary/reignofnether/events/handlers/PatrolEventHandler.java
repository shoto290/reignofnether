package com.solegendary.reignofnether.events.handlers;

import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.events.EventConstants;
import com.solegendary.reignofnether.events.exceptions.EventExecutionException;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Random;

public class PatrolEventHandler implements EventHandler {

    private final Random random = new Random();

    @Override
    public void execute(EventEntry entry, ServerPlayer player, BlockPos position) throws EventExecutionException {
        PatrolConfig config = extractPatrolConfig(entry);
        
        EntityType<?> entityType = getValidatedEntityType(config.entityType, entry, player);
        
        @SuppressWarnings("unchecked")
        EntityType<? extends Mob> mobEntityType = (EntityType<? extends Mob>) entityType;
        
        ServerLevel level = player.serverLevel();
        int spawnCount = calculateSpawnCount(config);
        
        ArrayList<Entity> spawnedEntities = UnitServerEvents.spawnMobs(
            mobEntityType, level, position, spawnCount, "");
        
        configureSpawnedEntities(spawnedEntities, config.isAggressive);
        
        sendSpawnMessage(player, spawnedEntities, config);
    }

    @Override
    public boolean canExecute(EventEntry entry, ServerPlayer player) {
        try {
            PatrolConfig config = extractPatrolConfig(entry);
            return isValidEntityType(config.entityType) && config.minCount > 0 && config.maxCount >= config.minCount;
        } catch (EventExecutionException e) {
            return false;
        }
    }

    @Override
    public String getSupportedType() {
        return "PATROL";
    }

    private PatrolConfig extractPatrolConfig(EventEntry entry) throws EventExecutionException {
        Object entityTypeObj = entry.parameters.get("entityType");
        Object minCountObj = entry.parameters.get("minCount");
        Object maxCountObj = entry.parameters.get("maxCount");
        Object isAggressiveObj = entry.parameters.get("isAggressive");
        
        if (!(entityTypeObj instanceof String)) {
            throw new EventExecutionException(entry.description, getSupportedType(), 
                EventConstants.Messages.INVALID_ENTITY_TYPE);
        }
        
        String entityType = (String) entityTypeObj;
        int minCount = extractIntParameter(minCountObj, 1);
        int maxCount = extractIntParameter(maxCountObj, 1);
        boolean isAggressive = extractBooleanParameter(isAggressiveObj, false);
        
        return new PatrolConfig(entityType, minCount, maxCount, isAggressive);
    }

    private EntityType<?> getValidatedEntityType(String entityTypeStr, EventEntry entry, ServerPlayer player) throws EventExecutionException {
        if (!isValidEntityType(entityTypeStr)) {
            throw new EventExecutionException(entry.description, getSupportedType(), 
                String.format(EventConstants.Messages.UNKNOWN_ENTITY_TYPE, entityTypeStr));
        }
        
        ResourceLocation entityLocation = new ResourceLocation(entityTypeStr);
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityLocation);
        
        if (!(entityType.create(player.serverLevel()) instanceof Mob)) {
            throw new EventExecutionException(entry.description, getSupportedType(), 
                String.format(EventConstants.Messages.ENTITY_MUST_BE_MOB, entityTypeStr));
        }
        
        return entityType;
    }

    private boolean isValidEntityType(String entityTypeStr) {
        try {
            ResourceLocation entityLocation = new ResourceLocation(entityTypeStr);
            return ForgeRegistries.ENTITY_TYPES.containsKey(entityLocation);
        } catch (Exception e) {
            return false;
        }
    }

    private int extractIntParameter(Object obj, int defaultValue) {
        return obj instanceof Number ? ((Number) obj).intValue() : defaultValue;
    }

    private boolean extractBooleanParameter(Object obj, boolean defaultValue) {
        return obj instanceof Boolean ? (Boolean) obj : defaultValue;
    }

    private int calculateSpawnCount(PatrolConfig config) {
        return config.minCount + random.nextInt(Math.max(1, config.maxCount - config.minCount + 1));
    }

    private void configureSpawnedEntities(ArrayList<Entity> spawnedEntities, boolean isAggressive) {
        for (Entity entity : spawnedEntities) {
            if (entity instanceof Mob mob) {
                configureMobBehavior(mob, isAggressive);
            }
        }
    }

    private void configureMobBehavior(Mob mob, boolean isAggressive) {
        if (isAggressive) {
            configureAggressiveBehavior(mob);
        } else {
            configurePacificBehavior(mob);
        }
    }

    private void configureAggressiveBehavior(Mob mob) {
        mob.setTarget(null);
        
        if (mob instanceof AttackerUnit) {
            // RTS units handle their own aggression
        } else {
            mob.setPersistenceRequired();
            
            if (mob instanceof PathfinderMob pathfinderMob) {
                pathfinderMob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                    pathfinderMob, LivingEntity.class, true, 
                    target -> target instanceof Unit || target instanceof net.minecraft.world.entity.player.Player));
                pathfinderMob.targetSelector.addGoal(2, new HurtByTargetGoal(pathfinderMob));
            }
        }
    }

    private void configurePacificBehavior(Mob mob) {
        mob.setPersistenceRequired();
        mob.setTarget(null);
    }

    private void sendSpawnMessage(ServerPlayer player, ArrayList<Entity> spawnedEntities, PatrolConfig config) {
        String suffix = config.isAggressive ? 
            EventConstants.Messages.AGGRESSIVE_SUFFIX : 
            EventConstants.Messages.PASSIVE_SUFFIX;
        
        player.sendSystemMessage(Component.literal(String.format(
            EventConstants.Messages.SPAWNED_ENTITIES,
            spawnedEntities.size(),
            config.entityType,
            player.blockPosition().toShortString(),
            suffix
        )));
    }

    private static class PatrolConfig {
        final String entityType;
        final int minCount;
        final int maxCount;
        final boolean isAggressive;

        PatrolConfig(String entityType, int minCount, int maxCount, boolean isAggressive) {
            this.entityType = entityType;
            this.minCount = minCount;
            this.maxCount = maxCount;
            this.isAggressive = isAggressive;
        }
    }
}