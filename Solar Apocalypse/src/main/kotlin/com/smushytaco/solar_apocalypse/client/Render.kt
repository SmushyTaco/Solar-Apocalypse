package com.smushytaco.solar_apocalypse.client
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.VertexConsumer
import org.joml.Matrix4f
import kotlin.math.abs

@Environment(EnvType.CLIENT)
object Render {
    fun bigSunGenerator(instance: BufferBuilder, matrix: Matrix4f, x: Float, y: Float, z: Float, original: Operation<VertexConsumer>): VertexConsumer {
        val world = MinecraftClient.getInstance().world ?: return original.call(instance, matrix, x, y, z)
        val multiplier = when {
            world.isOldEnough(config.phaseThreeDay) -> abs(config.phaseThreeSunSizeMultiplier.toDouble()).toFloat()
            world.isOldEnough(config.phaseTwoDay) -> abs(config.phaseTwoSunSizeMultiplier.toDouble()).toFloat()
            world.isOldEnough(config.phaseOneDay) -> abs(config.phaseOneSunSizeMultiplier.toDouble()).toFloat()
            else -> return original.call(instance, matrix, x, y, z)
        }
        return original.call(instance, matrix, x * multiplier, y, z * multiplier)
    }
    val Int.skyColor: Int
        get() {
            val world = MinecraftClient.getInstance().world ?: return this
            return when {
                config.shouldPhaseThreeHaveCustomSkyColor && world.isOldEnough(config.phaseThreeDay) -> config.phaseThreeSkyColor
                config.shouldPhaseTwoHaveCustomSkyColor && world.isOldEnough(config.phaseTwoDay) -> config.phaseTwoSkyColor
                config.shouldPhaseOneHaveCustomSkyColor && world.isOldEnough(config.phaseOneDay) -> config.phaseOneSkyColor
                else -> this
            }
        }
}