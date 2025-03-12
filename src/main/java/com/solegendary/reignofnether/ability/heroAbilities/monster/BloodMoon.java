package com.solegendary.reignofnether.ability.heroAbilities.monster;

//Forces day to night under a blood red moon for the entire world temporarily
//Raises the movement and attack speed of all of your units while active (other monster players' units are unaffected)
//Soul Siphon extends the duration

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class BloodMoon extends HeroAbility {

    public BloodMoon(HeroUnit hero, UnitAction action, Level level, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(hero, action, level, cooldownMax, range, radius, canTargetEntities);
    }


}
