package com.smushytaco.solar_apocalypse.configuration_support
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker
data class HeatLayer(val day: Double = 0.0, val layer: Double = 0.0, val sunSizeMultiplier: Float? = null, @ColorPicker val skyColor: Int? = null) : Comparable<HeatLayer> {
    override fun compareTo(other: HeatLayer) = other.day.compareTo(this.day)
}