package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse.rgbToInt
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker
data class HeatLayer(val day: Double = 0.0, val layer: Double = 0.0, val sunSizeMultiplier: Float = 3.0F, val enableCustomSkyColor: Boolean = true, @ColorPicker val skyColor: Int = rgbToInt(255, 69, 0)): Comparable<HeatLayer> {
    override fun compareTo(other: HeatLayer) = other.day.compareTo(this.day)
}