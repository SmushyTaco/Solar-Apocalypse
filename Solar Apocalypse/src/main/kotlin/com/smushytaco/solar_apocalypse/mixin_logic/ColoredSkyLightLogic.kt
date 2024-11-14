package com.smushytaco.solar_apocalypse.mixin_logic
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.blueToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypseClient
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.greenToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.redToFloat
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.Uniform
import net.minecraft.util.math.MathHelper
import org.joml.Vector3f
@Environment(EnvType.CLIENT)
object ColoredSkyLightLogic {
    fun hookUpdate(instance: Uniform, vector: Vector3f, original: Operation<Void>) {
        if (!config.enableCustomSkyLight) {
            original.call(instance, vector)
            return
        }
        val skylight = SolarApocalypseClient.skyColor
        val clientWorld = MinecraftClient.getInstance().world
        if (skylight == SolarApocalypseClient.originalSkyColor || clientWorld == null) {
            original.call(instance, vector)
            return
        }
        original.call(instance, vector.lerp(Vector3f(skylight.redToFloat, skylight.greenToFloat, skylight.blueToFloat), MathHelper.clamp(clientWorld.getSkyBrightness(1.0F), 0.0F, 1.0F)))
    }
}