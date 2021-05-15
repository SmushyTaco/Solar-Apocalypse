package com.smushytaco.solar_apocalypse
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.block.*
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
object SolarApocalypse : ModInitializer {
    private const val MOD_ID = "solar_apocalypse"
    override fun onInitialize() {
        Blocks.CLAY
        Registry.register(Registry.BLOCK, Identifier(MOD_ID, "dust"), DUST)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "dust"), BlockItem(DUST, Item.Settings().group(ItemGroup.BUILDING_BLOCKS)))
        Registry.register(Registry.STATUS_EFFECT, Identifier(MOD_ID, "sunscreen"), Sunscreen)
        ServerPlayerEvents.AFTER_RESPAWN.register(ServerPlayerEvents.AfterRespawn { _, newPlayer, _ ->
            val world = newPlayer.world
            val worldAge = world.timeOfDay / 24000.0
            if (worldAge < 7.0 || !newPlayer.isAlive || world.isRaining || newPlayer.isSpectator || newPlayer.isCreative || world.isNight || world.isClient || !world.isSkyVisible(newPlayer.blockPos) || newPlayer.hasStatusEffect(Sunscreen)) return@AfterRespawn
            newPlayer.addStatusEffect(StatusEffectInstance(Sunscreen, 2400, 0, false, false, true))
        })
    }
    val DUST = FallingBlock(AbstractBlock.Settings.of(Material.AGGREGATE, MaterialColor.SAND).strength(0.5F).sounds(BlockSoundGroup.SAND))
}