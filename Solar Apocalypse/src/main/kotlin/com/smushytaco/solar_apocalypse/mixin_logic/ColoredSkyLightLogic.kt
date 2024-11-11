package com.smushytaco.solar_apocalypse.mixin_logic
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.smushytaco.solar_apocalypse.SolarApocalypse.blueToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypse.greenToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypse.redToFloat
import com.smushytaco.solar_apocalypse.mixin_logic.SkyColorLogic.skyColor
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
        val skylight: Int = Integer.MAX_VALUE.skyColor
        val clientWorld = MinecraftClient.getInstance().world
        if (skylight == Int.MAX_VALUE || clientWorld == null) {
            original.call(instance, vector)
            return
        }
        original.call(instance, vector.lerp(Vector3f(skylight.redToFloat, skylight.greenToFloat, skylight.blueToFloat), MathHelper.clamp(clientWorld.getSkyBrightness(1.0f), 0.0f, 1.0f)))
    }
}