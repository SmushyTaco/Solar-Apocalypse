package com.smushytaco.solar_apocalypse.configuration_support
data class LightningPhase(val day: Double = 0.0, val multiplier: Double = 0.0): Comparable<LightningPhase> {
    override fun compareTo(other: LightningPhase) = other.day.compareTo(this.day)
}