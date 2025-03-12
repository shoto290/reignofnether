package com.solegendary.reignofnether.ability.heroAbilities.monster;

//The necromancer begins to collect the souls of nearby units that die
//Whenever another spell is cast, all souls up to a maximum are consumed to empower that spell
//Higher levels increase the maximum number of souls held
//can be toggled on and off

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class SoulSiphonPassive extends HeroAbility {

    public SoulSiphonPassive(HeroUnit hero, UnitAction action, Level level, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(hero, action, level, cooldownMax, range, radius, canTargetEntities);
    }


}
