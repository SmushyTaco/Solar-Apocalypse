package com.smushytaco.solar_apocalypse
import net.minecraft.world.level.Level
object WorldDayCalculation {
    fun Level.isOldEnough(days: Double): Boolean {
        if (!SolarApocalypse.dimensionWhitelist.contains(dimension().identifier().toString())) return false
        val worldAgeInMinecraftDays = this.defaultClockTime / 24000.0
        return days in 0.0..worldAgeInMinecraftDays
    }
}