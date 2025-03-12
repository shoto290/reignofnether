package com.solegendary.reignofnether.ability.heroAbilities.villager;

//Temporarily gain bonus damage, health and movespeed
//While active, the guard's model becomes larger and has a shimmering enchanted effect

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class Avatar extends HeroAbility {

    public Avatar(HeroUnit hero, UnitAction action, Level level, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(hero, action, level, cooldownMax, range, radius, canTargetEntities);
    }


}
