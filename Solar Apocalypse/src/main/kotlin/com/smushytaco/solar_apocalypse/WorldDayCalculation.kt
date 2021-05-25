package com.smushytaco.solar_apocalypse
import net.minecraft.world.World
object WorldDayCalculation {
    @JvmStatic fun World.isOldEnough(days: Double): Boolean {
        val worldAgeInMinecraftDays = this.timeOfDay / 24000.0
        return if (days < 0.0) false else worldAgeInMinecraftDays >= days
    }
}