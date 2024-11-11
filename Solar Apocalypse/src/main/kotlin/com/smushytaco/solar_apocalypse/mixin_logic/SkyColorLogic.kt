package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.minecraft.client.MinecraftClient
object SkyColorLogic {
    val Int.skyColor: Int
        get() {
            val world = MinecraftClient.getInstance().world ?: return this
            val heatLayers = config.heatLayers.sorted()
            for (heatLayer in heatLayers) if (heatLayer.enableCustomSkyColor && world.isOldEnough(heatLayer.day)) return heatLayer.skyColor
            return when {
                config.enablePhaseTwoCustomSkyColor && world.isOldEnough(config.phaseTwoDay) -> config.phaseTwoSkyColor
                config.enablePhaseOneCustomSkyColor && world.isOldEnough(config.phaseOneDay) -> config.phaseOneSkyColor
                else -> this
            }
        }
}