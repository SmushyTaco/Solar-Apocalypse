package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypse.shouldHeatLayerDamage
import com.smushytaco.solar_apocalypse.SolarApocalypse.sunscreen
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.network.ClientPlayerEntity
import kotlin.math.ceil
@Environment(EnvType.CLIENT)
object HeatOverlayFadeInAndOutLogic {
    fun ClientPlayerEntity.hookTick(overlayOpacity: Float): Float {
        val totalTicks = ceil(config.heatOverlayFadeTime * 20).toInt()
        val shouldDisplayOverlay = world.isOldEnough(config.phaseThreeDay) && isAlive && !world.isRaining && !isSpectator && !isCreative && !world.isNight && (world.isSkyVisible(blockPos) || this.shouldHeatLayerDamage(world)) && !hasStatusEffect(sunscreen)
        if (totalTicks <= 0) return if (shouldDisplayOverlay) 1.0F else 0.0F
        return if (shouldDisplayOverlay) (overlayOpacity + (1.0F / totalTicks)).coerceIn(0.0F, 1.0F) else (overlayOpacity - (1.0F / totalTicks)).coerceIn(0.0F, 1.0F)
    }
}