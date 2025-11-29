package com.smushytaco.solar_apocalypse.mixin_logic
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.mojang.blaze3d.buffers.Std140Builder
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypseClient
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.blueToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.greenToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.redToFloat
import net.minecraft.client.Minecraft
import net.minecraft.util.Mth
import org.joml.Vector3f
import org.joml.Vector3fc
object ColoredSkyLightLogic {
    fun hookUpdate(instance: Std140Builder, vec: Vector3fc, original: Operation<Std140Builder>): Std140Builder {
        if (!config.enableCustomSkyLight) return original.call(instance, vec)
        val skylight = SolarApocalypseClient.skyColor
        val clientWorld = Minecraft.getInstance().level
        if (skylight == SolarApocalypseClient.originalSkyColor || clientWorld == null || vec !is Vector3f) return original.call(instance, vec)
        val skyBrightnessFactor = Mth.clamp(clientWorld.getSkyDarken(1.0F), 0.0F, 1.0F)
        return original.call(instance, vec.lerp(Vector3f(skylight.redToFloat, skylight.greenToFloat, skylight.blueToFloat), skyBrightnessFactor))
    }
}