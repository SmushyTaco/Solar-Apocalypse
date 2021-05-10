package com.smushytaco.solar_apocalypse
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
object SolarApocalypse : ModInitializer {
    private const val MOD_ID = "solar_apocalypse"
    override fun onInitialize() {
        Registry.register(Registry.STATUS_EFFECT, Identifier(MOD_ID, "sunscreen"), SUNSCREEN)
        ServerPlayerEvents.AFTER_RESPAWN.register(ServerPlayerEvents.AfterRespawn { _, newPlayer, _ ->
            val world = newPlayer.world
            val worldAge = world.timeOfDay / 24000.0
            if (worldAge < 7.0 || !newPlayer.isAlive || world.isRaining || newPlayer.isSpectator || newPlayer.isCreative || world.isNight || world.isClient || !world.isSkyVisible(newPlayer.blockPos) || newPlayer.hasStatusEffect(SUNSCREEN)) return@AfterRespawn
            newPlayer.addStatusEffect(StatusEffectInstance(SUNSCREEN, 2400, 0, false, false, true))
        })
    }
    val SUNSCREEN = Sunscreen
}