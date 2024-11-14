package com.smushytaco.solar_apocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypse.shouldHeatLayerDamage
import com.smushytaco.solar_apocalypse.SolarApocalypse.sunscreen
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import com.smushytaco.solar_apocalypse.mixin_logic.BigSunLogic.sunSize
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import kotlin.math.ceil
@Environment(EnvType.CLIENT)
object SolarApocalypseClient: ClientModInitializer {
    private var hasInitialized = false
    var overlayOpacity = 0.0F
    var fogFade = 0.0F
    var sunTransition = 1.0F
    var previousSunMultiplier = 1.0F
    private var _currentSunMultiplier = 1.0F
    var currentSunMultiplier
        get() = _currentSunMultiplier
        set(value) {
            if (_currentSunMultiplier == value) return
            previousSunMultiplier = currentSunMultiplier
            _currentSunMultiplier = value
            sunTransition = 0.0F
        }
    override fun onInitializeClient() {
        ClientPlayConnectionEvents.DISCONNECT.register(ClientPlayConnectionEvents.Disconnect { _, _ -> hasInitialized = false })
        ClientTickEvents.END_WORLD_TICK.register(ClientTickEvents.EndWorldTick { world ->
            if (!hasInitialized) {
                hasInitialized = true
                MinecraftClient.getInstance().player?.let { player ->
                    fogFade = if (player.transitionConditions(config.apocalypseFogDay)) 1.0F else 0.0F
                    overlayOpacity = if (player.transitionConditions(config.phaseTwoDay)) 1.0F else 0.0F
                }
                _currentSunMultiplier = world.sunSize
                previousSunMultiplier = _currentSunMultiplier
            }
        })
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            val player = it.player ?: return@EndTick
            overlayOpacity = player.updateFade(overlayOpacity, config.heatOverlayFadeTime, config.phaseTwoDay)
            fogFade = player.updateFade(fogFade, config.apocalypseFadeTime, config.apocalypseFogDay)
            if (sunTransition != 1.0F) sunTransition = updateTransition(sunTransition, config.sunSizeTransitionTime)
        })
    }
    private fun ClientPlayerEntity.updateFade(value: Float, time: Double, day: Double): Float {
        val totalTicks = ceil(time * 20).toInt()
        val should = transitionConditions(day)
        if (totalTicks <= 0) return if (should) 1.0F else 0.0F
        return if (should) (value + (1.0F / totalTicks)).coerceIn(0.0F, 1.0F) else (value - (1.0F / totalTicks)).coerceIn(0.0F, 1.0F)
    }
    private fun ClientPlayerEntity.transitionConditions(day: Double) = world.isOldEnough(day) && isAlive && !world.isRaining && !isSpectator && !isCreative && !world.isNight && (world.isSkyVisible(blockPos) || shouldHeatLayerDamage(world)) && !hasStatusEffect(sunscreen)
    private fun updateTransition(value: Float, time: Double): Float {
        val totalTicks = ceil(time * 20).toInt()
        if (totalTicks <= 0) return 1.0F
        return (value + (1.0F / totalTicks)).coerceIn(0.0F, 1.0F)
    }
    fun skyColor(originalColor: Int?): Int? {
        val world = MinecraftClient.getInstance().world ?: return originalColor
        for (heatLayer in SolarApocalypse.heatLayers) if (heatLayer.enableCustomSkyColor && world.isOldEnough(heatLayer.day)) return heatLayer.skyColor
        return when {
            config.enablePhaseTwoCustomSkyColor && world.isOldEnough(config.phaseTwoDay) -> config.phaseTwoSkyColor
            config.enablePhaseOneCustomSkyColor && world.isOldEnough(config.phaseOneDay) -> config.phaseOneSkyColor
            else -> originalColor
        }
    }
    fun lerpColor(color1: Int, color2: Int, progress: Float): Int {
        val r1 = (color1 shr 16) and 0xFF
        val g1 = (color1 shr 8) and 0xFF
        val b1 = color1 and 0xFF
        val r2 = (color2 shr 16) and 0xFF
        val g2 = (color2 shr 8) and 0xFF
        val b2 = color2 and 0xFF
        val r = (r1 + ((r2 - r1) * progress)).toInt() and 0xFF
        val g = (g1 + ((g2 - g1) * progress)).toInt() and 0xFF
        val b = (b1 + ((b2 - b1) * progress)).toInt() and 0xFF
        return (r shl 16) or (g shl 8) or b
    }
    val Int.redToFloat
        get() = ((this shr 16) and 0xFF) / 255.0F
    val Int.greenToFloat
        get() = ((this shr 8) and 0xFF) / 255.0F
    val Int.blueToFloat
        get() = (this and 0xFF) / 255.0F
}