package com.solegendary.reignofnether.survival.spawners;

import com.solegendary.reignofnether.building.buildings.piglins.Portal;
import com.solegendary.reignofnether.player.PlayerServerEvents;
import com.solegendary.reignofnether.registrars.EntityRegistrar;
import com.solegendary.reignofnether.research.ResearchServerEvents;
import com.solegendary.reignofnether.research.researchItems.ResearchBruteShields;
import com.solegendary.reignofnether.research.researchItems.ResearchCubeMagma;
import com.solegendary.reignofnether.research.researchItems.ResearchHeavyTridents;
import com.solegendary.reignofnether.research.researchItems.ResearchSoulFireballs;
import com.solegendary.reignofnether.survival.Wave;
import com.solegendary.reignofnether.unit.units.piglins.BruteUnit;
import com.solegendary.reignofnether.unit.units.piglins.HeadhunterUnit;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.*;

import static com.solegendary.reignofnether.survival.SurvivalServerEvents.*;

public class PiglinWaveSpawner {

    private static final Random random = new Random();

    private static final Map<Integer, List<EntityType<? extends Mob>>> PIGLIN_UNITS = new HashMap<>();

    static {
        PIGLIN_UNITS.put(1, List.of(
                EntityRegistrar.BRUTE_UNIT.get(),
                EntityRegistrar.HEADHUNTER_UNIT.get()
        ));
        PIGLIN_UNITS.put(2, List.of(
                EntityRegistrar.BRUTE_UNIT.get(),
                EntityRegistrar.HEADHUNTER_UNIT.get(),
                EntityRegistrar.HOGLIN_UNIT.get(),
                EntityRegistrar.MAGMA_CUBE_UNIT.get()
        ));
        PIGLIN_UNITS.put(3, List.of(
                EntityRegistrar.BRUTE_UNIT.get(),
                EntityRegistrar.HEADHUNTER_UNIT.get(),
                EntityRegistrar.MAGMA_CUBE_UNIT.get(),
                EntityRegistrar.BLAZE_UNIT.get(),
                EntityRegistrar.HOGLIN_UNIT.get()
                // + 50% chance of Hoglin riders
        ));
        PIGLIN_UNITS.put(4, List.of(
                EntityRegistrar.BRUTE_UNIT.get(),
                EntityRegistrar.HEADHUNTER_UNIT.get(),
                EntityRegistrar.MAGMA_CUBE_UNIT.get(),
                EntityRegistrar.BLAZE_UNIT.get(),
                EntityRegistrar.HOGLIN_UNIT.get(),
                EntityRegistrar.WITHER_SKELETON_UNIT.get()
                // + shields and heavy tridents
                // piglins gain chest gold armour
        ));
        PIGLIN_UNITS.put(5, List.of(
                EntityRegistrar.BRUTE_UNIT.get(),
                EntityRegistrar.HEADHUNTER_UNIT.get(),
                EntityRegistrar.MAGMA_CUBE_UNIT.get(),
                EntityRegistrar.BLAZE_UNIT.get(),
                EntityRegistrar.HOGLIN_UNIT.get(),
                EntityRegistrar.WITHER_SKELETON_UNIT.get(),
                EntityRegistrar.GHAST_UNIT.get()
                // piglins gain full gold armour
        ));
        PIGLIN_UNITS.put(6, List.of(
                EntityRegistrar.BRUTE_UNIT.get(),
                EntityRegistrar.HEADHUNTER_UNIT.get(),
                EntityRegistrar.MAGMA_CUBE_UNIT.get(),
                EntityRegistrar.BLAZE_UNIT.get(),
                EntityRegistrar.HOGLIN_UNIT.get(),
                EntityRegistrar.WITHER_SKELETON_UNIT.get(),
                EntityRegistrar.GHAST_UNIT.get()
                // piglins gain protection-enchanted gold armour
                // soul fireballs
                // cube magma
        ));
    }

    public static EntityType<? extends Mob> getRandomUnitOfTier(int tier) {
        List<EntityType<? extends Mob>> units = PIGLIN_UNITS.get(tier);
        return units.get(random.nextInt(units.size()));
    }

    public static void checkAndApplyArmour(LivingEntity entity, int tier) {
        if (entity instanceof HeadhunterUnit || entity instanceof BruteUnit) {
            if (tier >= 4)
                entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
            if (tier >= 5) {
                entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
                entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
            }
            if (tier >= 6) {
                entity.getItemBySlot(EquipmentSlot.CHEST).enchant(Enchantments.ALL_DAMAGE_PROTECTION, 1);
                entity.getItemBySlot(EquipmentSlot.LEGS).enchant(Enchantments.ALL_DAMAGE_PROTECTION, 1);
                entity.getItemBySlot(EquipmentSlot.FEET).enchant(Enchantments.ALL_DAMAGE_PROTECTION, 1);
            }
        }
    }

    public static void checkAndApplyUpgrades(int tier) {
        if (tier >= 4 && !ResearchServerEvents.playerHasResearch(ENEMY_OWNER_NAME, ResearchBruteShields.itemName))
            ResearchServerEvents.addResearch(ENEMY_OWNER_NAME, ResearchBruteShields.itemName);
        if (tier >= 4 && !ResearchServerEvents.playerHasResearch(ENEMY_OWNER_NAME, ResearchHeavyTridents.itemName))
            ResearchServerEvents.addResearch(ENEMY_OWNER_NAME, ResearchHeavyTridents.itemName);
        if (tier >= 6 && !ResearchServerEvents.playerHasResearch(ENEMY_OWNER_NAME, ResearchSoulFireballs.itemName))
            ResearchServerEvents.addResearch(ENEMY_OWNER_NAME, ResearchSoulFireballs.itemName);
        if (tier >= 6 && !ResearchServerEvents.playerHasResearch(ENEMY_OWNER_NAME, ResearchCubeMagma.itemName))
            ResearchServerEvents.addResearch(ENEMY_OWNER_NAME, ResearchCubeMagma.itemName);
    }

    // spawn portals which spawn half of the wave immediately, and trickle in constantly
    public static void spawnPiglinWave(ServerLevel level, Wave wave) {
        checkAndApplyUpgrades(wave.highestUnitTier);

        int numPortals = wave.getNumPortals();
        int failedPortalPlacements = 0;
        ArrayList<BlockPos> portalBps = new ArrayList<>();

        for (int i = 0; i < numPortals; i++) {

            BlockPos spawnBp = null;
            int attempts = 0;
            boolean tooCloseToAnotherPortal;

            do {
                tooCloseToAnotherPortal = false;
                List<BlockPos> spawnBps = WaveSpawner.getValidSpawnPoints(1, level, false, 8);
                if (!spawnBps.isEmpty())
                    spawnBp = spawnBps.get(0);
                attempts += 1;

                for (BlockPos bp : portalBps)
                    if (spawnBp != null && bp.distSqr(spawnBp) < 25)
                        tooCloseToAnotherPortal = true;
                for (BlockPos bp : portals.stream().map(p -> p.portal.originPos).toList())
                    if (spawnBp != null && bp.distSqr(spawnBp) < 25)
                        tooCloseToAnotherPortal = true;

            } while((spawnBp == null || tooCloseToAnotherPortal) && attempts < 100);

            if (spawnBp != null) {
                portalBps.add(spawnBp);
                WaveSpawner.spawnBuilding(Portal.buildingName, new BlockPos(spawnBp).above());
            } else
                failedPortalPlacements += 1;
        }
        if (failedPortalPlacements > 0)
            PlayerServerEvents.sendMessageToAllPlayers("Failed to spawn " + failedPortalPlacements + " portals!");

        lastFaction = Faction.PIGLINS;
    }
}
