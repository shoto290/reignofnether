package com.solegendary.reignofnether.ability.heroAbilities.monster;

//Summons an empowered zombie and skeleton but with limited lifespan
//Higher levels give the units armor and weapons, then enchanted armor and weapons
//Soul Siphon raises their base size/damage/health

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class RaiseDead extends HeroAbility {

    public RaiseDead(HeroUnit hero, UnitAction action, Level level, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(hero, action, level, cooldownMax, range, radius, canTargetEntities);
    }


}
