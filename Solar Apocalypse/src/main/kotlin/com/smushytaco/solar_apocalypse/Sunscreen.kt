package com.smushytaco.solar_apocalypse
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
object Sunscreen: MobEffect(MobEffectCategory.BENEFICIAL, 14981690) {
    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int) = true
}