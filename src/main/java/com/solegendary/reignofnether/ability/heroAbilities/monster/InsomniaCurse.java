package com.solegendary.reignofnether.ability.heroAbilities.monster;

//Curses an enemy to be chased by a temporary phantom summon, has multiple charges
//Higher levels raise number of charges and lowers cooldown
//Soul Siphon raises the size of the phantoms

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class InsomniaCurse extends HeroAbility {

    public InsomniaCurse(HeroUnit hero, UnitAction action, Level level, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(hero, action, level, cooldownMax, range, radius, canTargetEntities);
    }


}
