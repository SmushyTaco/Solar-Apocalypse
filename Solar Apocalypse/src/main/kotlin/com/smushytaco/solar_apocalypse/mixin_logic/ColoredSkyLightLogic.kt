package com.smushytaco.solar_apocalypse.mixin_logic
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.mojang.blaze3d.systems.RenderPass
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.blueToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypseClient
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.greenToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.redToFloat
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.MathHelper
object ColoredSkyLightLogic {
    fun hookUpdate(instance: RenderPass, name: String, floats: FloatArray, original: Operation<Void>) {
        if (!config.enableCustomSkyLight) {
            original.call(instance, name, floats)
            return
        }
        val skylight = SolarApocalypseClient.skyColor
        val clientWorld = MinecraftClient.getInstance().world
        if (skylight == SolarApocalypseClient.originalSkyColor || clientWorld == null) {
            original.call(instance, name, floats)
            return
        }
        val skyBrightnessFactor = MathHelper.clamp(clientWorld.getSkyBrightness(1.0F), 0.0F, 1.0F)
        original.call(instance, name, floatArrayOf(
            MathHelper.lerp(skyBrightnessFactor, floats[0], skylight.redToFloat),
            MathHelper.lerp(skyBrightnessFactor, floats[1], skylight.greenToFloat),
            MathHelper.lerp(skyBrightnessFactor, floats[2], skylight.blueToFloat)
        ))
    }
}