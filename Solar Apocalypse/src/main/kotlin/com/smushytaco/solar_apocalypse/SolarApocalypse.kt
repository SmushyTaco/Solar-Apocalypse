package com.smushytaco.solar_apocalypse
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import com.smushytaco.solar_apocalypse.configuration_support.ModConfiguration
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.*
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
object SolarApocalypse : ModInitializer {
    const val MOD_ID = "solar_apocalypse"
    lateinit var config: ModConfiguration
        private set
    override fun onInitialize() {
        AutoConfig.register(ModConfiguration::class.java) { definition: Config, configClass: Class<ModConfiguration> ->
            GsonConfigSerializer(definition, configClass)
        }
        config = AutoConfig.getConfigHolder(ModConfiguration::class.java).config
        Registry.register(Registries.BLOCK, Identifier(MOD_ID, "dust"), DUST)
        val dustBlockItem = Registry.register(Registries.ITEM, Identifier(MOD_ID, "dust"), BlockItem(DUST, Item.Settings()))
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ItemGroupEvents.ModifyEntries { it.add(dustBlockItem) })
        Registry.register(Registries.STATUS_EFFECT, Identifier(MOD_ID, "sunscreen"), Sunscreen)
        ServerPlayerEvents.AFTER_RESPAWN.register(ServerPlayerEvents.AfterRespawn { _, newPlayer, _ ->
            val world = newPlayer.world
            if (!world.isOldEnough(config.mobsAndPlayersBurnInDaylightDay) || !newPlayer.isAlive || world.isRaining || newPlayer.isSpectator || newPlayer.isCreative || world.isNight || world.isClient || !world.isSkyVisible(newPlayer.blockPos) || newPlayer.hasStatusEffect(Sunscreen)) return@AfterRespawn
            newPlayer.addStatusEffect(StatusEffectInstance(Sunscreen, 2400, 0, false, false, true))
        })
    }
    val DUST = FallingBlock(FabricBlockSettings.of(Material.AGGREGATE, MapColor.BLACK).strength(0.5F).sounds(BlockSoundGroup.SAND))
}