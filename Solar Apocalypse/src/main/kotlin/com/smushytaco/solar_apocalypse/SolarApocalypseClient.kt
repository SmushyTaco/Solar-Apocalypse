package com.smushytaco.solar_apocalypse
import com.smushytaco.solar_apocalypse.mixin_logic.BigSunLogic.sunSize
import com.smushytaco.solar_apocalypse.mixin_logic.HeatOverlayFadeInAndOutLogic.transitionConditions
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.MinecraftClient
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
            if (hasInitialized) return@EndWorldTick
            hasInitialized = true
            MinecraftClient.getInstance().player?.let { player ->
                fogFade = if (player.transitionConditions(SolarApocalypse.config.apocalypseFogDay)) 1.0F else 0.0F
                overlayOpacity = if (player.transitionConditions(SolarApocalypse.config.phaseTwoDay)) 1.0F else 0.0F
            }
            _currentSunMultiplier = world.sunSize
            previousSunMultiplier = _currentSunMultiplier
        })
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