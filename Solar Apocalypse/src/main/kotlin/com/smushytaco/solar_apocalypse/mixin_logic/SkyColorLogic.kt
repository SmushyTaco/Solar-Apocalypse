package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.SolarApocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
@Environment(EnvType.CLIENT)
object SkyColorLogic {
    fun skyColor(originalColor: Int?): Int? {
        val world = MinecraftClient.getInstance().world ?: return originalColor
        for (heatLayer in SolarApocalypse.heatLayers) if (heatLayer.enableCustomSkyColor && world.isOldEnough(heatLayer.day)) return heatLayer.skyColor
        return when {
            config.enablePhaseTwoCustomSkyColor && world.isOldEnough(config.phaseTwoDay) -> config.phaseTwoSkyColor
            config.enablePhaseOneCustomSkyColor && world.isOldEnough(config.phaseOneDay) -> config.phaseOneSkyColor
            else -> originalColor
        }
    }
}