package com.solegendary.reignofnether.ability;

import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public abstract class HeroAbility extends Ability {

    // can be ranked up when the hero levels up
    // requires a HeroUnit to be passed

    public final HeroUnit hero;
    public int rank = 0; // 0 == not learnt

    public HeroAbility(HeroUnit hero, UnitAction action, Level level, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(action, level, cooldownMax, range, radius, canTargetEntities);
        this.hero = hero;
    }
}
