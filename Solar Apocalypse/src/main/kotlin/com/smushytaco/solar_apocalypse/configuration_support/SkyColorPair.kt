package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse.rgbToInt
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker
data class SkyColorPair(val day: Double = 0.0, @ColorPicker val skyColor: Int = rgbToInt(255, 69, 0)): Comparable<SkyColorPair> {
    override fun compareTo(other: SkyColorPair) = other.day.compareTo(this.day)
}