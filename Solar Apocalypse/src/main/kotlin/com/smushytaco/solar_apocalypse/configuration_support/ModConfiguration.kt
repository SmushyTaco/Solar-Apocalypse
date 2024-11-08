package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypse.rgbToInt
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
    val enablePhaseOneCustomSkyColor = true
    @ColorPicker
    val phaseOneSkyColor = rgbToInt(255, 165, 0)
    val enablePhaseTwoCustomSkyColor = true
    @ColorPicker
    val phaseTwoSkyColor = rgbToInt(255, 140, 0)
    val enablePhaseThreeCustomSkyColor = true
    @ColorPicker
    val phaseThreeSkyColor = rgbToInt(255, 69, 0)
    val enableCustomSkyLight = true
    val enableHeatLayers = true
    val enableHeatLayersOnBlocks = false
    val heatLayers = listOf(HeatLayer(10.0, 100.0, 3.25F, rgbToInt(255, 50, 0)), HeatLayer(20.0, 60.0, 3.5F, rgbToInt(255, 40, 0)), HeatLayer(30.0, 50.0, 3.75F, rgbToInt(255, 30, 0)), HeatLayer(40.0, 25.0, 4.0F, rgbToInt(255, 30, 0)), HeatLayer(50.0, 0.0, 5.00F, rgbToInt(255, 0, 0)))
    val dimensionWhitelist = listOf("minecraft:overworld")
}