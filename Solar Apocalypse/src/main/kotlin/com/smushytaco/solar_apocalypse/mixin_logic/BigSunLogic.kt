package com.smushytaco.solar_apocalypse.mixin_logic
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.smushytaco.solar_apocalypse.SolarApocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypseClient
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumer
import net.minecraft.world.World
import org.joml.Matrix4f
object BigSunLogic {
    val World.sunSize: Float
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
    fun bigSunGenerator(instance: VertexConsumer, matrix: Matrix4f, x: Float, y: Float, z: Float, original: Operation<VertexConsumer>): VertexConsumer {
        val world = MinecraftClient.getInstance().world ?: return original.call(instance, matrix, x, y, z)
        var multiplier = world.sunSize
        if (multiplier == 0.0F) multiplier = 1.0F
        SolarApocalypseClient.currentSunMultiplier = multiplier
        multiplier = SolarApocalypseClient.previousSunMultiplier + (SolarApocalypseClient.currentSunMultiplier - SolarApocalypseClient.previousSunMultiplier) * SolarApocalypseClient.sunTransition
        return original.call(instance, matrix, x * multiplier, y, z * multiplier)
    }
}