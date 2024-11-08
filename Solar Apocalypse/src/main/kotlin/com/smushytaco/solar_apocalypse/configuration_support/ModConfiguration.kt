package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler
@Config(name = SolarApocalypse.MOD_ID)
class ModConfiguration: ConfigData {
    val phaseOneDay = 3.0
    val phaseOneSunSizeMultiplier = 1.5F
    val phaseTwoDay = 5.0
    val phaseTwoSunSizeMultiplier = 2.0F
    val phaseThreeDay = 7.0
    val phaseThreeSunSizeMultiplier = 3.0F
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    val coarseDirtTurnsToSandPhase = Phases.NONE
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    val cobbledAndCrackedStonesTurnsToLavaPhase = Phases.NONE
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    val cropGrowthSlowDownPhase = Phases.PHASE_TWO
    val cropGrowthSlowDownMultiplier = 3
    val solarFireDamageMultiplier = 5.0F
    val shouldPhaseOneHaveCustomSkyColor = true
    @ColorPicker
    val phaseOneSkyColor = SolarApocalypse.rgbToInt(255, 165, 0)
    val shouldPhaseTwoHaveCustomSkyColor = true
    @ColorPicker
    val phaseTwoSkyColor = SolarApocalypse.rgbToInt(255, 140, 0)
    val shouldPhaseThreeHaveCustomSkyColor = true
    @ColorPicker
    val phaseThreeSkyColor = SolarApocalypse.rgbToInt(255, 69, 0)
    val dimensionWhitelist = listOf("minecraft:overworld")
}