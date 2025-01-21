package com.solegendary.reignofnether.building.buildings.neutral;

import com.solegendary.reignofnether.building.*;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
import com.solegendary.reignofnether.tutorial.TutorialStage;
import com.solegendary.reignofnether.unit.units.neutral.EndermanProd;
import com.solegendary.reignofnether.unit.units.villagers.PillagerProd;
import com.solegendary.reignofnether.unit.units.villagers.VindicatorProd;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.solegendary.reignofnether.building.BuildingUtils.getAbsoluteBlockData;

public class Beacon extends ProductionBuilding {

    public final static String buildingName = "Beacon";
    public final static String structureName = "beacon_t0";
    public final static String structureNameT1 = "beacon_t1";
    public final static String structureNameT2 = "beacon_t2";
    public final static String structureNameT3 = "beacon_t3";
    public final static String structureNameT4 = "beacon_t4";
    public final static ResourceCost cost = ResourceCost.Building(0,0,0,0);

    public final static int MAX_UPGRADE_LEVEL = 4;

    public Beacon(Level level, BlockPos originPos, Rotation rotation, String ownerName) {
        super(level, originPos, rotation, ownerName, getAbsoluteBlockData(getRelativeBlockData(level), level, originPos, rotation), false);
        this.name = buildingName;
        this.ownerName = ownerName;
        this.portraitBlock = Blocks.BEACON;
        this.icon = new ResourceLocation("minecraft", "textures/item/nether_star.png");

        this.foodCost = cost.food;
        this.woodCost = cost.wood;
        this.oreCost = cost.ore;
        this.popSupply = cost.population;

        this.startingBlockTypes.add(Blocks.STONE_BRICKS);

        this.explodeChance = 0.2f;

        /*
        TODO: upgrade
        if (level.isClientSide())
            this.productionButtons = Arrays.asList(
                    EndermanProd.getStartButton(this, Keybindings.keyQ)
            );
         */
    }

    public void changeStructure(String newStructureName) {
        ArrayList<BuildingBlock> newBlocks = BuildingBlockData.getBuildingBlocks(newStructureName, this.getLevel());
        this.blocks = getAbsoluteBlockData(newBlocks, this.getLevel(), originPos, rotation);
        super.refreshBlocks();
    }

    public Faction getFaction() {return Faction.NONE;}

    public static ArrayList<BuildingBlock> getRelativeBlockData(LevelAccessor level) {
        return BuildingBlockData.getBuildingBlocks(structureName, level);
    }

    public static AbilityButton getBuildButton(Keybinding hotkey) {
        return new AbilityButton(
                buildingName,
                new ResourceLocation("minecraft", "textures/item/nether_star.png"),
                hotkey,
                () -> BuildingClientEvents.getBuildingToPlace() == Beacon.class,
                () -> false,
                () -> true,
                () -> BuildingClientEvents.setBuildingToPlace(Beacon.class),
                null,
                List.of(
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.beacon"), Style.EMPTY.withBold(true)),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.beacon.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.neutral.reignofnether.beacon.tooltip2"), Style.EMPTY)
                ),
                null
        );
    }

    public int getUpgradeLevel() {
        for (BuildingBlock block : blocks) {
            if (block.getBlockState().getBlock() == Blocks.DIAMOND_BLOCK)
                return 4;
            if (block.getBlockState().getBlock() == Blocks.EMERALD_BLOCK)
                return 3;
            if (block.getBlockState().getBlock() == Blocks.GOLD_BLOCK)
                return 2;
            if (block.getBlockState().getBlock() == Blocks.IRON_BLOCK)
                return 1;
        }
        return 0;
    }

    public boolean isUpgraded() {
        return getUpgradeLevel() > 0;
    }
}
