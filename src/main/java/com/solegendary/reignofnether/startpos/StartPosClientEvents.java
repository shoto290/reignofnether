package com.solegendary.reignofnether.startpos;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.buildings.monsters.Mausoleum;
import com.solegendary.reignofnether.building.buildings.piglins.CentralPortal;
import com.solegendary.reignofnether.building.buildings.villagers.TownCentre;
import com.solegendary.reignofnether.fogofwar.FogOfWarClientEvents;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
import com.solegendary.reignofnether.research.researchItems.ResearchAdvancedPortals;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class StartPosClientEvents {

    public static ArrayList<StartPos> startPoses = new ArrayList<>();
    public static int startPosIndex = -1;
    public static boolean reserved = false;
    public static Faction selectedFaction = Faction.NONE;

    public static Button getPositionsButton() {
        return new Button("Starting Positions",
                14,
                getIcon(),
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/icon_frame.png"),
                Keybindings.stop,
                () -> false,
                () -> startPoses.isEmpty(),
                () -> true,
                () -> cycleStartBlock(true),
                () -> cycleStartBlock(false),
                List.of(
                        fcs(I18n.get("startpos.reignofnether.positions_button.tooltip1"), true),
                        fcs(I18n.get("startpos.reignofnether.positions_button.tooltip2")),
                        fcs(I18n.get("startpos.reignofnether.positions_button.tooltip3",
                                startPoses.stream().filter(sp -> sp.reserved).toList().size(), startPoses.size()))
                )
        );
    }

    public static Button getStartButton() {
        return new Button("Start Game",
                14,
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/tick.png"),
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/icon_frame.png"),
                null,
                () -> false,
                () -> startPoses.isEmpty(),
                () -> startPoses.stream().filter(sp -> sp.reserved).toList().size() > 0,
                null,
                null,
                List.of(
                        fcs(I18n.get("startpos.reignofnether.start_button.tooltip1"), true),
                        fcs(I18n.get("startpos.reignofnether.start_button.tooltip2",
                                startPoses.stream().filter(sp -> sp.reserved).toList().size()))
                )
        );
    }

    public static StartPos getPos() {
        if (startPosIndex < 0 || startPosIndex >= startPoses.size()) {
            return null;
        } else {
            return startPoses.get(startPosIndex);
        }
    }

    private static ResourceLocation getIcon() {
        if (getPos() == null)
            return new ResourceLocation(ReignOfNether.MOD_ID, "textures/block/rts_start_block_white.png");
        else
            return getPos().getIcon();
    }

    private static void cycleStartBlock(boolean forward) {
        if (forward) {
            startPosIndex += 1;
            if (startPosIndex >= startPoses.size())
                startPosIndex = 0;
        } else {
            startPosIndex -= 1;
            if (startPosIndex < 0)
                startPosIndex = startPoses.size() - 1;
        }
        if (!startPoses.isEmpty()) {
            StartPos startPos = startPoses.get(startPosIndex);
            OrthoviewClientEvents.centreCameraOnPos(startPos.pos);
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent evt) {
        if (evt.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
            return;
        if (!OrthoviewClientEvents.isEnabled() || getPos() == null || selectedFaction == Faction.NONE)
            return;

        switch (selectedFaction) {
            case VILLAGERS -> BuildingClientEvents.setBuildingToPlace(TownCentre.class);
            case MONSTERS -> BuildingClientEvents.setBuildingToPlace(Mausoleum.class);
            case PIGLINS -> BuildingClientEvents.setBuildingToPlace(CentralPortal.class);
        }
        BuildingClientEvents.drawBuildingToPlace(evt.getPoseStack(), getPos().pos);
        BuildingClientEvents.setBuildingToPlace(null);
    }

    private static final Minecraft MC = Minecraft.getInstance();

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut evt) {
        // LOG OUT FROM SERVER WORLD ONLY
        if (MC.player != null && evt.getPlayer() != null && evt.getPlayer().getId() == MC.player.getId())
            startPoses.clear();
    }

    @SubscribeEvent
    public static void onPlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent evt) {
        // LOG OUT FROM SINGLEPLAYER WORLD ONLY
        if (MC.player != null && evt.getEntity().getId() == MC.player.getId())
            startPoses.clear();
    }
}
