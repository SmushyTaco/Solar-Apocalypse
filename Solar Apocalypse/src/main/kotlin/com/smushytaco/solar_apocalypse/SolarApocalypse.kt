package com.smushytaco.solar_apocalypse
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import com.smushytaco.solar_apocalypse.configuration_support.ModConfiguration
import com.smushytaco.solar_apocalypse.configuration_support.Phases
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.*
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.entity.Entity
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
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.ColorCode
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
object SolarApocalypse : ModInitializer {
    fun isPhaseReady(phase: Phases, world: World): Boolean {
        if (phase == Phases.PHASE_THREE && world.isOldEnough(config.phaseThreeDay)) return true
        if (phase == Phases.PHASE_TWO && world.isOldEnough(config.phaseTwoDay)) return true
        if (phase == Phases.PHASE_ONE && world.isOldEnough(config.phaseOneDay)) return true
        return false
    }
    fun rgbToInt(red: Int, green: Int, blue: Int) = (red.coerceIn(0, 255) shl 16) or (green.coerceIn(0, 255) shl 8) or blue.coerceIn(0, 255)
    val Int.redToFloat
        get() = ((this shr 16) and 0xFF) / 255.0F
    val Int.greenToFloat
        get() = ((this shr 8) and 0xFF) / 255.0F
    val Int.blueToFloat
        get() = (this and 0xFF) / 255.0F
    fun blockDestruction(world: ServerWorld, pos: BlockPos, ci: CallbackInfo) {
        if (!world.apocalypseChecks(pos)) return
        world.setBlockState(pos, Blocks.AIR.defaultState)
        ci.cancel()
    }
    fun ServerWorld.apocalypseChecks(pos: BlockPos) = isOldEnough(config.phaseTwoDay) && !isNight && !isRaining && (isSkyVisible(pos.offset(Direction.UP)) || pos.shouldHeatLayerDamage(this))
    private fun heatLayerCheck(world: World, y: Double, condition: Boolean = config.enableHeatLayers): Boolean {
        if (!condition || !world.isOldEnough(config.phaseThreeDay)) return false
        val heatLayers = config.heatLayers.sorted()
        for (heatLayer in heatLayers) if (y > heatLayer.layer && world.isOldEnough(heatLayer.day)) return true
        return false
    }
    fun Entity.shouldHeatLayerDamage(world: World) = heatLayerCheck(world, y)
    fun BlockPos.shouldHeatLayerDamage(world: World) = heatLayerCheck(world, y.toDouble(), config.enableHeatLayers && config.enableHeatLayersOnBlocks)
    val BlockState.blockChecks: Boolean
        get() = block === Blocks.COBBLESTONE || block === Blocks.COBBLESTONE_SLAB || block === Blocks.COBBLESTONE_STAIRS || block === Blocks.COBBLESTONE_WALL || block === Blocks.COBBLED_DEEPSLATE || block === Blocks.COBBLED_DEEPSLATE_SLAB || block === Blocks.COBBLED_DEEPSLATE_STAIRS || block === Blocks.COBBLED_DEEPSLATE_WALL || block === Blocks.CRACKED_DEEPSLATE_TILES || block === Blocks.CRACKED_DEEPSLATE_BRICKS || block === Blocks.CRACKED_STONE_BRICKS
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