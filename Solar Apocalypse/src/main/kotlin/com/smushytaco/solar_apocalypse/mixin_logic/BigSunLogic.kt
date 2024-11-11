package com.smushytaco.solar_apocalypse.mixin_logic
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
object BigSunLogic {
    fun bigSunGenerator(instance: BufferBuilder, matrix: Matrix4f, x: Float, y: Float, z: Float, original: Operation<VertexConsumer>): VertexConsumer {
        val world = MinecraftClient.getInstance().world ?: return original.call(instance, matrix, x, y, z)
        var multiplier: Float? = null
        val heatLayers = config.heatLayers.sorted()
        for (heatLayer in heatLayers) {
            if (world.isOldEnough(heatLayer.day)) {
                multiplier = abs(heatLayer.sunSizeMultiplier)
                break
            }
        }
        if (multiplier == null) {
            multiplier = when {
                world.isOldEnough(config.phaseTwoDay) -> abs(config.phaseTwoSunSizeMultiplier)
                world.isOldEnough(config.phaseOneDay) -> abs(config.phaseOneSunSizeMultiplier)
                else -> return original.call(instance, matrix, x, y, z)
            }
        }
        if (multiplier == 0.0F) original.call(instance, matrix, x, y, z)
        return original.call(instance, matrix, x * multiplier, y, z * multiplier)
    }
}