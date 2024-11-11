package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.SolarApocalypse.shouldHeatLayerDamage
import com.smushytaco.solar_apocalypse.SolarApocalypse.sunscreen
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.network.ClientPlayerEntity
import kotlin.math.ceil
@Environment(EnvType.CLIENT)
object HeatOverlayFadeInAndOutLogic {
    fun ClientPlayerEntity.hookTick(value: Float, time: Double, day: Double): Float {
        val totalTicks = ceil(time * 20).toInt()
        val shouldDisplayOverlay = world.isOldEnough(day) && isAlive && !world.isRaining && !isSpectator && !isCreative && !world.isNight && (world.isSkyVisible(blockPos) || shouldHeatLayerDamage(world)) && !hasStatusEffect(sunscreen)
        if (totalTicks <= 0) return if (shouldDisplayOverlay) 1.0F else 0.0F
        return if (shouldDisplayOverlay) (value + (1.0F / totalTicks)).coerceIn(0.0F, 1.0F) else (value - (1.0F / totalTicks)).coerceIn(0.0F, 1.0F)
    }
}