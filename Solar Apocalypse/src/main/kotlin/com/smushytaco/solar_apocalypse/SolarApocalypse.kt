package com.smushytaco.solar_apocalypse
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import com.smushytaco.solar_apocalypse.configuration_support.ModConfiguration
import com.smushytaco.solar_apocalypse.configuration_support.Phases
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.ColoredFallingBlock
import net.minecraft.block.MapColor
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.VertexConsumer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.ColorCode
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.joml.Matrix4f
import kotlin.math.abs

object SolarApocalypse : ModInitializer {
    fun isPhaseReady(phase: Phases, world: World): Boolean {
        if (phase == Phases.PHASE_THREE && world.isOldEnough(config.phaseThreeDay)) return true
        if (phase == Phases.PHASE_TWO && world.isOldEnough(config.phaseTwoDay)) return true
        if (phase == Phases.PHASE_ONE && world.isOldEnough(config.phaseOneDay)) return true
        return false
    }
    fun rgbToInt(red: Int, green: Int, blue: Int) = (red.coerceIn(0, 255) shl 16) or (green.coerceIn(0, 255) shl 8) or blue.coerceIn(0, 255)
    const val MOD_ID = "solar_apocalypse"
    lateinit var config: ModConfiguration
        private set
    lateinit var sunscreen: RegistryEntry.Reference<StatusEffect>
        private set
    override fun onInitialize() {
        AutoConfig.register(ModConfiguration::class.java) { definition: Config, configClass: Class<ModConfiguration> ->
            GsonConfigSerializer(definition, configClass)
        }
        config = AutoConfig.getConfigHolder(ModConfiguration::class.java).config
        Registry.register(Registries.BLOCK, DUST_IDENTIFIER, DUST)
        val dustBlockItem = Registry.register(Registries.ITEM, DUST_IDENTIFIER, BlockItem(DUST, Item.Settings().useBlockPrefixedTranslationKey().registryKey(RegistryKey.of(RegistryKeys.ITEM, DUST_IDENTIFIER))))
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ItemGroupEvents.ModifyEntries { it.add(dustBlockItem) })
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "sunscreen"), Sunscreen)
        sunscreen = Registries.STATUS_EFFECT.getEntry(Identifier.of(MOD_ID, "sunscreen")).get()
        ServerPlayerEvents.AFTER_RESPAWN.register(ServerPlayerEvents.AfterRespawn { _, newPlayer, _ ->
            val world = newPlayer.world
            if (!world.isOldEnough(config.phaseThreeDay) || !newPlayer.isAlive || world.isRaining || newPlayer.isSpectator || newPlayer.isCreative || world.isNight || world.isClient || !world.isSkyVisible(newPlayer.blockPos) || newPlayer.hasStatusEffect(sunscreen)) return@AfterRespawn
            newPlayer.addStatusEffect(StatusEffectInstance(sunscreen, 2400, 0, false, false, true))
        })
    }
    private val DUST_IDENTIFIER = Identifier.of(MOD_ID, "dust")
    val DUST = ColoredFallingBlock(ColorCode(0x191919), AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sounds(BlockSoundGroup.SAND).registryKey(RegistryKey.of(RegistryKeys.BLOCK, DUST_IDENTIFIER)))
}