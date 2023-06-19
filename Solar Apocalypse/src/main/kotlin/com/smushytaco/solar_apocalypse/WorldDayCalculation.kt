package com.smushytaco.solar_apocalypse
import net.minecraft.world.World
object WorldDayCalculation {
    @JvmStatic fun World.isOldEnough(days: Double): Boolean {
        if (!SolarApocalypse.config.dimensionWhitelist.contains(registryKey.value.toString())) return false
        val worldAgeInMinecraftDays = this.timeOfDay / 24000.0
        return days in 0.0..worldAgeInMinecraftDays
    }
}