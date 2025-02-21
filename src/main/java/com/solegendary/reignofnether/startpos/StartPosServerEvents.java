package com.solegendary.reignofnether.startpos;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.blocks.RTSStartBlock;
import com.solegendary.reignofnether.fogofwar.FogOfWarClientEvents;
import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
import com.solegendary.reignofnether.player.PlayerServerEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class StartPosServerEvents {

    public static final int MAX_START_POSES = 16;

    public static ArrayList<StartPos> startPoses = new ArrayList<>();

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent evt) {
        if (evt.getPlacedBlock().getBlock() instanceof RTSStartBlock rtsStartBlock) {
            if (startPoses.size() < MAX_START_POSES) {
                StartPos newStartPos = new StartPos(evt.getPos(), rtsStartBlock.defaultMaterialColor().id);
                startPoses.add(newStartPos);
                StartPosClientboundPacket.addPos(newStartPos);
            } else {
                evt.setCanceled(true);
                for (Player player : PlayerServerEvents.players)
                    if (player.distanceToSqr(Vec3.atCenterOf(evt.getPos())) < 100)
                        player.sendSystemMessage(Component.translatable("Max starting positions reached (16)"));
            }
            if (evt.getLevel() instanceof ServerLevel serverLevel)
                saveResearch(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent evt) {
        if (startPoses.removeIf(sp -> {
            if (sp.pos.equals(evt.getPos())) {
                StartPosClientboundPacket.removePos(evt.getPos());
                return true;
            }
            return false;
        }) && (evt.getLevel() instanceof ServerLevel serverLevel)) {
            saveResearch(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent evt) {
        for (StartPos startPos : startPoses)
            StartPosClientboundPacket.addPos(startPos);
    }

    public static void saveResearch(ServerLevel serverLevel) {
        StartPosSaveData startPosData = StartPosSaveData.getInstance(serverLevel);
        startPosData.startPoses.clear();
        startPosData.startPoses.addAll(startPoses);
        startPosData.save();
        serverLevel.getDataStorage().save();

        ReignOfNether.LOGGER.info("saved " + startPoses.size() + " start positions in serverevents");
    }

    @SubscribeEvent
    public static void loadResearch(ServerStartedEvent evt) {
        ServerLevel level = evt.getServer().getLevel(Level.OVERWORLD);
        if (level != null) {
            StartPosSaveData startPosData = StartPosSaveData.getInstance(level);
            startPoses.clear();
            startPoses.addAll(startPosData.startPoses);
            ReignOfNether.LOGGER.info("loaded " + startPoses.size() + " start positions in serverevents");
        }
    }
}
