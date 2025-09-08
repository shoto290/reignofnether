package com.solegendary.reignofnether.events;

import com.solegendary.reignofnether.config.AutoEventConfig;
import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.config.JsonEventConfig;
import com.solegendary.reignofnether.config.JsonEventConfigManager;
import com.solegendary.reignofnether.events.handlers.EventHandlerFactory;
import com.solegendary.reignofnether.events.handlers.EventHandler;
import com.solegendary.reignofnether.events.exceptions.EventExecutionException;
import com.solegendary.reignofnether.player.PlayerServerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class AutoEventScheduler {
    private static final Random RANDOM = new Random();
    private static final Map<String, Long> lastEventTimes = new ConcurrentHashMap<>();
    private static final EventHandlerFactory handlerFactory = new EventHandlerFactory();
    
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END || evt.level.isClientSide() || evt.level.dimension() != Level.OVERWORLD)
            return;

        ServerLevel level = (ServerLevel) evt.level;
        List<ServerPlayer> allPlayers = level.getServer().getPlayerList().getPlayers();
        
        List<ServerPlayer> rtsPlayers = allPlayers.stream()
            .filter(player -> PlayerServerEvents.isRTSPlayer(player.getName().getString()))
            .toList();
        
        if (rtsPlayers.isEmpty()) return;

        JsonEventConfig config = JsonEventConfigManager.getConfig();
        if (config == null) return;

        long currentTime = System.currentTimeMillis();
        
        for (Map.Entry<String, EventEntry> entry : config.events.entrySet()) {
            String eventName = entry.getKey();
            EventEntry eventEntry = entry.getValue();
            
            if (!eventEntry.isAutoEvent()) continue;
            
            AutoEventConfig autoConfig = eventEntry.autoConfig;
            if (!autoConfig.isEnabledByDefault()) continue;
            
            if (!canEventTrigger(eventName, currentTime, autoConfig, level, rtsPlayers)) continue;
            
            for (ServerPlayer player : rtsPlayers) {
                if (RANDOM.nextDouble() < autoConfig.getRarity() / EventConstants.RARITY_DIVISION_FACTOR) {
                    BlockPos eventPos = findEventPosition(player, autoConfig, level);
                    if (eventPos != null) {
                        executeAutoEvent(eventName, eventEntry, player, eventPos, level);
                        lastEventTimes.put(eventName, currentTime);
                        break;
                    }
                }
            }
        }
    }
    
    private static boolean canEventTrigger(String eventName, long currentTime, AutoEventConfig autoConfig, 
                                         ServerLevel level, List<ServerPlayer> players) {
        if (players.size() < autoConfig.getRequiresPlayers()) return false;
        
        Long lastTime = lastEventTimes.get(eventName);
        if (lastTime != null && (currentTime - lastTime) < autoConfig.getCooldown() * EventConstants.Time.MILLISECONDS_PER_SECOND) return false;
        
        long worldTime = level.getDayTime() % EventConstants.Time.TICKS_PER_DAY;
        int timeMin = autoConfig.getWorldTimeMin();
        int timeMax = autoConfig.getWorldTimeMax();
        
        if (timeMin <= timeMax) {
            if (worldTime < timeMin || worldTime > timeMax) return false;
        } else {
            if (worldTime > timeMax && worldTime < timeMin) return false;
        }
        
        return true;
    }
    
    private static ServerPlayer selectRandomPlayer(List<ServerPlayer> players, AutoEventConfig autoConfig) {
        if (players.isEmpty()) return null;
        return players.get(RANDOM.nextInt(players.size()));
    }
    
    private static BlockPos findEventPosition(ServerPlayer player, AutoEventConfig autoConfig, ServerLevel level) {
        int attempts = 0;
        int maxAttempts = EventConstants.MAX_SPAWN_ATTEMPTS;
        
        while (attempts < maxAttempts) {
            double angle = RANDOM.nextDouble() * 2 * Math.PI;
            int distance = autoConfig.getMinDistance() + 
                          RANDOM.nextInt(autoConfig.getMaxDistance() - autoConfig.getMinDistance() + 1);
            
            int x = (int) (player.getX() + Math.cos(angle) * distance);
            int z = (int) (player.getZ() + Math.sin(angle) * distance);
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            
            BlockPos pos = new BlockPos(x, y, z);
            
            if (isBiomeAllowed(level, pos, autoConfig)) {
                return pos;
            }
            
            attempts++;
        }
        
        return null;
    }
    
    private static boolean isBiomeAllowed(ServerLevel level, BlockPos pos, AutoEventConfig autoConfig) {
        List<String> restrictions = autoConfig.getBiomeRestrictions();
        if (restrictions == null || restrictions.isEmpty()) return true;
        
        Biome biome = level.getBiome(pos).value();
        ResourceLocation biomeLocation = ForgeRegistries.BIOMES.getKey(biome);
        
        if (biomeLocation == null) return false;
        
        return restrictions.contains(biomeLocation.toString());
    }
    
    private static void executeAutoEvent(String eventName, EventEntry eventEntry, ServerPlayer targetPlayer, 
                                       BlockPos eventPos, ServerLevel level) {
        AutoEventConfig autoConfig = eventEntry.autoConfig;
        
        if (autoConfig.isAnnouncement()) {
            String message = String.format(EventConstants.Messages.EVENT_TRIGGERED, 
                eventName, eventPos.getX(), eventPos.getY(), eventPos.getZ());
            level.getServer().getPlayerList().broadcastSystemMessage(
                Component.literal(message), false);
        }
        
        EventHandler handler = handlerFactory.getHandler(eventEntry.type);
        if (handler != null) {
            try {
                BlockPos originalPos = targetPlayer.blockPosition();
                try {
                    targetPlayer.setPos(eventPos.getX(), eventPos.getY(), eventPos.getZ());
                    handler.execute(eventEntry, targetPlayer, eventPos);
                } finally {
                    targetPlayer.setPos(originalPos.getX(), originalPos.getY(), originalPos.getZ());
                }
            } catch (EventExecutionException e) {
                targetPlayer.sendSystemMessage(Component.literal("Auto event failed: " + e.getMessage()));
            }
        }
    }
}