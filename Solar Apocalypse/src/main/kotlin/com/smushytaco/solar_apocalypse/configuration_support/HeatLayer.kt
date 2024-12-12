package com.smushytaco.solar_apocalypse.configuration_support
data class HeatLayer(val day: Double = 0.0, val layer: Double = 0.0): Comparable<HeatLayer> {
    override fun compareTo(other: HeatLayer) = other.day.compareTo(this.day)
}