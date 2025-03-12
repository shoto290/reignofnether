package com.solegendary.reignofnether.ability.heroAbilities.villager;

//The lower the guard's health, the more damage, resistance and life regen he gains
//Higher levels increase the amount of damage and resistance gained

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class BattleRagePassive extends HeroAbility {

    public BattleRagePassive(HeroUnit hero, UnitAction action, Level level, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(hero, action, level, cooldownMax, range, radius, canTargetEntities);
    }


}
