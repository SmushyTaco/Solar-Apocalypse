package com.smushytaco.solar_apocalypse.mixin_logic
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.smushytaco.solar_apocalypse.SolarApocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypseClient
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.minecraft.client.Minecraft
import net.minecraft.world.level.Level
import org.joml.Matrix4f
import org.joml.Matrix4fStack
object BigSunLogic {
    val Level.sunSize: Float
        get() {
            var multiplier = 1.0F
            for (sunMultiplierPhase in SolarApocalypse.sunMultiplierPhases) {
                if (isOldEnough(sunMultiplierPhase.day)) {
                    multiplier = sunMultiplierPhase.sunSizeMultiplier
                    break
                }
            }
            if (multiplier == 0.0F) multiplier = 1.0F
            return multiplier
        }
    fun bigSunGenerator(instance: Matrix4fStack, x: Float, y: Float, z: Float, original: Operation<Matrix4f>): Matrix4f {
        val world = Minecraft.getInstance().level ?: return original.call(instance, x, y, z)
        var multiplier = world.sunSize
        if (multiplier == 0.0F) multiplier = 1.0F
        SolarApocalypseClient.currentSunMultiplier = multiplier
        multiplier = SolarApocalypseClient.previousSunMultiplier + (SolarApocalypseClient.currentSunMultiplier - SolarApocalypseClient.previousSunMultiplier) * SolarApocalypseClient.sunTransition
        return original.call(instance, x * multiplier, y, z * multiplier)
    }
}