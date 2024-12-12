package com.smushytaco.solar_apocalypse.configuration_support
data class SunMultiplierPair(val day: Double = 0.0, val sunSizeMultiplier: Float = 3.0F): Comparable<SunMultiplierPair> {
    override fun compareTo(other: SunMultiplierPair) = other.day.compareTo(this.day)
}