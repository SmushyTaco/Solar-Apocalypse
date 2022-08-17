package com.smushytaco.solar_apocalypse;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class Sunscreen extends MobEffect {
    public static final Sunscreen INSTANCE = new Sunscreen();

    protected Sunscreen() {
        super(MobEffectCategory.BENEFICIAL, 14981690);
    }

    @Override
    public boolean isDurationEffectTick(int a, int b) {
        return true;
    }
}
