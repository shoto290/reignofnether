package com.solegendary.reignofnether.ability.heroAbilities.piglin;

//Throws out a pile of food - friendly units automatically pick up this food and take a few seconds to eat it to instantly heal
//Higher levels raise the quality of food thrown
//Greed is Good raises the amount of food thrown

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class FancyFeast extends HeroAbility {

    public FancyFeast(HeroUnit hero, UnitAction action, Level level, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(hero, action, level, cooldownMax, range, radius, canTargetEntities);
    }


}
