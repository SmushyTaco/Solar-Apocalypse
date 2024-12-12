package com.smushytaco.solar_apocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypse.shouldHeatLayerDamage
import com.smushytaco.solar_apocalypse.SolarApocalypse.sunscreen
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import com.smushytaco.solar_apocalypse.mixin_logic.BigSunLogic.sunSize
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.world.World
import kotlin.math.ceil
object SolarApocalypseClient: ClientModInitializer {
    var hasInitialized = false
        private set
    var overlayOpacity = 0.0F
        private set
    var fogFade = 0.0F
        private set
    var cloudFade = 0.0F
        private set
    var sunTransition = 1.0F
        private set
    var previousSunMultiplier = 1.0F
        private set
    private var _currentSunMultiplier = 1.0F
    var currentSunMultiplier
        get() = _currentSunMultiplier
        set(value) {
            if (_currentSunMultiplier == value) return
            previousSunMultiplier = currentSunMultiplier
            _currentSunMultiplier = value
            sunTransition = 0.0F
        }
    private var skyTransition = 1.0F
    var originalSkyColor = 1
        private set
    private var previousSkyColor = 1
    private var _currentSkyColor = 1
    private var currentSkyColor
        get() = _currentSkyColor
        set(value) {
            if (_currentSkyColor == value) return
            previousSkyColor = _currentSkyColor
            _currentSkyColor = value
            if (SolarApocalypse.skyColors.contains(previousSkyColor) || SolarApocalypse.skyColors.contains(_currentSkyColor)) skyTransition = 0.0F
        }
    val skyColor
        get() = lerpColor(previousSkyColor, currentSkyColor, skyTransition)
    private var fogTransition = 1.0F
    private var previousFogColor = 1
    private var _currentFogColor = 1
    private var currentFogColor
        get() = _currentFogColor
        set(value) {
            if (_currentFogColor == value) return
            previousFogColor = _currentFogColor
            _currentFogColor = value
            if (SolarApocalypse.skyColors.contains(previousFogColor) || SolarApocalypse.skyColors.contains(_currentFogColor)) fogTransition = 0.0F
        }
    val fogColor
        get() = lerpColor(previousFogColor, currentFogColor, fogTransition)
    override fun onInitializeClient() {
        ClientPlayConnectionEvents.DISCONNECT.register(ClientPlayConnectionEvents.Disconnect { _, _ -> hasInitialized = false })
        ClientTickEvents.END_WORLD_TICK.register(ClientTickEvents.EndWorldTick { world ->
            if (hasInitialized) return@EndWorldTick
            cloudFade = if (world.transitionConditions(config.noCloudsDay)) 1.0F else 0.0F
            MinecraftClient.getInstance().player?.let { player ->
                fogFade = if (player.transitionConditions(config.apocalypseFogDay)) 1.0F else 0.0F
                overlayOpacity = if (player.transitionConditions(config.phaseTwoDay)) 1.0F else 0.0F
                _currentSkyColor = world.biomeAccess.getBiome(player.blockPos).value().skyColor
                previousSkyColor = _currentSkyColor
                originalSkyColor = _currentSkyColor
                _currentFogColor = world.biomeAccess.getBiome(player.blockPos).value().fogColor
                previousFogColor = _currentFogColor
            }
            _currentSunMultiplier = world.sunSize
            previousSunMultiplier = _currentSunMultiplier
            updateSkyColor(null, world)
            updateFogColor(null, world)
            hasInitialized = true
        })
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            val player = it.player ?: return@EndTick
            overlayOpacity = player.updateFade(overlayOpacity, config.heatOverlayFadeTime, config.phaseTwoDay)
            fogFade = player.updateFade(fogFade, config.apocalypseFadeTime, config.apocalypseFogDay)
            cloudFade = player.updateCloudFade(cloudFade, config.cloudFadeTime, config.noCloudsDay)
            if (sunTransition != 1.0F) sunTransition = updateTransition(sunTransition, config.sunSizeTransitionTime)
            if (skyTransition != 1.0F) skyTransition = updateTransition(skyTransition, config.skyColorTransitionTime)
            if (fogTransition != 1.0F) fogTransition = updateTransition(fogTransition, config.fogColorTransitionTime)
        })
    }
    private fun ClientPlayerEntity.updateFade(value: Float, time: Double, day: Double): Float {
        val totalTicks = ceil(time * 20).toInt()
        val should = transitionConditions(day)
        if (totalTicks <= 0) return if (should) 1.0F else 0.0F
        return if (should) (value + (1.0F / totalTicks)).coerceIn(0.0F, 1.0F) else (value - (1.0F / totalTicks)).coerceIn(0.0F, 1.0F)
    }
    private fun ClientPlayerEntity.updateCloudFade(value: Float, time: Double, day: Double): Float {
        val totalTicks = ceil(time * 20).toInt()
        val should = world.transitionConditions(day)
        if (totalTicks <= 0) return if (should) 1.0F else 0.0F
        return if (should) (value + (1.0F / totalTicks)).coerceIn(0.0F, 1.0F) else (value - (1.0F / totalTicks)).coerceIn(0.0F, 1.0F)
    }
    private fun World.transitionConditions(day: Double) = isOldEnough(day) && !isRaining && !isNight
    private fun ClientPlayerEntity.transitionConditions(day: Double) = world.isOldEnough(day) && isAlive && !world.isRaining && !isSpectator && !isCreative && !world.isNight && (world.isSkyVisible(blockPos) || shouldHeatLayerDamage(world)) && !hasStatusEffect(sunscreen)
    @Suppress("SameParameterValue")
    private fun updateTransition(value: Float, time: Double): Float {
        val totalTicks = ceil(time * 20).toInt()
        if (totalTicks <= 0) return 1.0F
        return (value + (1.0F / totalTicks)).coerceIn(0.0F, 1.0F)
    }
    private fun updateColor(originalColor: Int?, world: World?): Int? {
        val theWorld = world ?: MinecraftClient.getInstance().world ?: return originalColor
        for (skyColorPhase in SolarApocalypse.skyColorPhases) if (theWorld.isOldEnough(skyColorPhase.day)) return skyColorPhase.skyColor
        return originalColor
    }
    fun updateSkyColor(originalColor: Int?, world: World? = null) {
        val color = updateColor(originalColor, world)
        if (color != null) {
            if (color == originalColor) originalSkyColor = color
            if (hasInitialized) {
                currentSkyColor = color
            } else {
                _currentSkyColor = color
                previousSkyColor = _currentSkyColor
            }
        }
    }
    fun updateFogColor(originalColor: Int?, world: World? = null) {
        val color = updateColor(originalColor, world)
        if (color != null) {
            if (hasInitialized) {
                currentFogColor = color
            } else {
                _currentFogColor = color
                previousFogColor = _currentFogColor
            }
        }
    }
    private fun lerpColor(colorOne: Int, colorTwo: Int, progress: Float): Int {
        val r1 = (colorOne shr 16) and 0xFF
        val g1 = (colorOne shr 8) and 0xFF
        val b1 = colorOne and 0xFF
        val r2 = (colorTwo shr 16) and 0xFF
        val g2 = (colorTwo shr 8) and 0xFF
        val b2 = colorTwo and 0xFF
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