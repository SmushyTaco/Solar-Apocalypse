package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypseClient
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.blueToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.greenToFloat
import com.smushytaco.solar_apocalypse.SolarApocalypseClient.redToFloat
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.state.LightmapRenderState
import net.minecraft.util.Mth
import org.joml.Vector3f
object ColoredSkyLightLogic {
    fun hookExtract(renderState: LightmapRenderState) {
        if (!config.enableCustomSkyLight) return

        val skylight = SolarApocalypseClient.skyColor
        val clientWorld = Minecraft.getInstance().level
        val currentSkyLightColor = renderState.skyLightColor

        if (skylight == SolarApocalypseClient.originalSkyColor || clientWorld == null) return

        val skyBrightnessFactor = Mth.clamp(renderState.skyFactor, 0.0F, 1.0F)

        renderState.skyLightColor = Vector3f(currentSkyLightColor).lerp(
            Vector3f(skylight.redToFloat, skylight.greenToFloat, skylight.blueToFloat),
            skyBrightnessFactor
        )
    }
}