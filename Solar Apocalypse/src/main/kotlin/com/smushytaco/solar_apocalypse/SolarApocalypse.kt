package com.smushytaco.solar_apocalypse
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import com.smushytaco.solar_apocalypse.configuration_support.*
import com.smushytaco.solar_apocalypse.mixins.BlockStateAccessor
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
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
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.ActionResult
import net.minecraft.util.ColorCode
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
object SolarApocalypse : ModInitializer {
    fun rgbToInt(red: Int, green: Int, blue: Int) = (red.coerceIn(0, 255) shl 16) or (green.coerceIn(0, 255) shl 8) or blue.coerceIn(0, 255)
    val Block.stringIdentifier: String
        get() {
            this as BlockCache
            if (cacheIdentifier != "") return cacheIdentifier
            val identifier = Registries.BLOCK.getId(this).toString()
            cacheIdentifier = identifier
            return identifier
        }
    fun Block.containsTag(tag: String): Boolean {
        this as BlockCache
        if (cacheTags.contains(tag)) return true
        @Suppress("DEPRECATION")
        if (registryEntry.streamTags().use { it.anyMatch { theTag -> theTag.id.toString() == tag } }) {
            cacheTags.add(tag)
            return true
        }
        return false
    }
    private val stringToBlock = hashMapOf<String, Block>()
    val String.block: Block
        get() {
            stringToBlock[this]?.let { return it }
            val block = Registries.BLOCK[Identifier.of(this)]
            stringToBlock[this] = block
            return block
        }
    fun World.apocalypseChecks(pos: BlockPos) = isOldEnough(config.phaseOneDay) && !isNight && !isRaining && (isSkyVisible(pos.up()) || pos.shouldHeatLayerDamage(this))
    private fun heatLayerCheck(world: World, y: Double, condition: Boolean = config.enableHeatLayers): Boolean {
        if (!condition) return false
        for (heatLayer in heatLayers) if (y > heatLayer.layer && world.isOldEnough(heatLayer.day)) return true
        return false
    }
    fun Entity.shouldHeatLayerDamage(world: World) = heatLayerCheck(world, y)
    fun BlockPos.shouldHeatLayerDamage(world: World) = heatLayerCheck(world, y.toDouble(), config.enableHeatLayers && config.enableHeatLayersOnBlocks)
    fun isInstanceOfClassByName(block: Block, className: String): Boolean {
        val name = if (!className.contains('.')) "net.minecraft.$className" else className
        block as BlockCache
        if (block.cacheCorrectClasses.contains(name)) return true
        if (block.cacheIncorrectClasses.contains(name)) return false
        val kClass = try {
            Class.forName(name).kotlin
        } catch (_: Exception) {
            return false
        }
        if (kClass.isInstance(block)) {
            block.cacheCorrectClasses.add(name)
            return true
        }
        block.cacheIncorrectClasses.add(name)
        return false
    }
    val World.lightningMultiplier: Double
        get() {
            for (lightningPhase in lightningPhases) if (isOldEnough(lightningPhase.day)) return lightningPhase.multiplier.coerceAtLeast(1.0)
            return 1.0
        }
    const val MOD_ID = "solar_apocalypse"
    val HEAT_OVERLAY: Identifier = Identifier.of(MOD_ID, "textures/misc/heat_overlay_outline.png")
    private var burnableBlockIdentifiers = hashSetOf<String>()
    private var burnableBlockTags = hashSetOf<String>()
    private var burnableBlockClasses = hashSetOf<String>()
    private var blockTransformationBlockToBlock = hashSetOf<BlockPair>()
    val blockTransformationBlockToBlockMap = hashMapOf<String, HashSet<String>>()
    val blockTransformationBlockToBlockMapWithLava = hashMapOf<String, HashSet<String>>()
    private var blockTransformationTagToBlock = hashSetOf<TagAndBlock>()
    private var blockTransformationClassToBlock = hashSetOf<ClassAndBlock>()
    var lavaBlockIdentifiers = hashSetOf<String>()
        private set
    private var lavaBlockTags = hashSetOf<String>()
    private var lavaBlockClasses = hashSetOf<String>()
    var sunMultiplierPhases = listOf<SunMultiplierPair>()
        private set
    var skyColorPhases = listOf<SkyColorPair>()
        private set
    private var heatLayers = listOf<HeatLayer>()
    private var lightningPhases = listOf<LightningPhase>()
    var skyColors = hashSetOf<Int>()
        private set
    var dimensionWhitelist = hashSetOf<String>()
        private set
    lateinit var config: ModConfiguration
        private set
    lateinit var sunscreen: RegistryEntry.Reference<StatusEffect>
        private set
    private fun calculateBlocks() {
        val identifiers = hashSetOf<String>()
        identifiers.addAll(blockTransformationBlockToBlock.map { it.blockOne })
        identifiers.addAll(burnableBlockIdentifiers)
        identifiers.addAll(lavaBlockIdentifiers)
        val tags = hashSetOf<String>()
        tags.addAll(blockTransformationTagToBlock.map { it.tag })
        tags.addAll(burnableBlockTags)
        tags.addAll(lavaBlockTags)
        val classes = hashSetOf<String>()
        classes.addAll(blockTransformationClassToBlock.map { it.className })
        classes.addAll(burnableBlockClasses)
        classes.addAll(lavaBlockClasses)
        Registries.BLOCK.forEach {
            it as BlockCache
            val isBurnable = (it.defaultState as BlockStateAccessor).burnable
            it.cacheShouldBurn = ConfigurationLogic.isWhitelisted(isBurnable, it, burnableBlockIdentifiers, burnableBlockTags, burnableBlockClasses)
            it.cacheShouldRandomTick = ConfigurationLogic.isWhitelisted(isBurnable, it, identifiers, tags, classes) || it == Blocks.WATER
            for (tagAndBlock in blockTransformationTagToBlock) {
                if (!it.containsTag(tagAndBlock.tag)) continue
                blockTransformationBlockToBlockMap.putIfAbsent(it.stringIdentifier, hashSetOf())
                blockTransformationBlockToBlockMap[it.stringIdentifier]?.add(tagAndBlock.block)
            }
            for (classAndBlock in blockTransformationClassToBlock) {
                if (!isInstanceOfClassByName(it, classAndBlock.className)) continue
                blockTransformationBlockToBlockMap.putIfAbsent(it.stringIdentifier, hashSetOf())
                blockTransformationBlockToBlockMap[it.stringIdentifier]?.add(classAndBlock.block)
            }
            for (tag in lavaBlockTags) {
                if (!it.containsTag(tag)) continue
                lavaBlockIdentifiers.add(it.stringIdentifier)
            }
            for (className in lavaBlockClasses) {
                if (!isInstanceOfClassByName(it, className)) continue
                lavaBlockIdentifiers.add(it.stringIdentifier)
            }
            blockTransformationBlockToBlockMapWithLava.clear()
            blockTransformationBlockToBlockMap.forEach { (key, value) ->
                val newHashSet = hashSetOf<String>()
                value.forEach { block -> newHashSet.add(block) }
                blockTransformationBlockToBlockMapWithLava[key] = newHashSet
            }
            lavaBlockIdentifiers.forEach { value ->
                blockTransformationBlockToBlockMapWithLava.putIfAbsent(value, hashSetOf())
                blockTransformationBlockToBlockMapWithLava[value]?.add(Blocks.LAVA.stringIdentifier)
            }
        }
    }
    private fun configCache(modConfiguration: ModConfiguration) {
        burnableBlockIdentifiers = modConfiguration.burnableBlockIdentifiers.toHashSet()
        burnableBlockTags = modConfiguration.burnableBlockTags.toHashSet()
        burnableBlockClasses = modConfiguration.burnableBlockClasses.toHashSet()
        blockTransformationBlockToBlock = modConfiguration.blockTransformationBlockToBlock.toHashSet()
        blockTransformationBlockToBlockMap.clear()
        blockTransformationBlockToBlock.forEach {
            blockTransformationBlockToBlockMap.putIfAbsent(it.blockOne, hashSetOf())
            blockTransformationBlockToBlockMap[it.blockOne]?.add(it.blockTwo)
        }
        blockTransformationTagToBlock = modConfiguration.blockTransformationTagToBlock.toHashSet()
        blockTransformationClassToBlock = modConfiguration.blockTransformationClassToBlock.toHashSet()
        lavaBlockIdentifiers = modConfiguration.lavaBlockIdentifiers.toHashSet()
        lavaBlockTags = modConfiguration.lavaBlockTags.toHashSet()
        lavaBlockClasses = modConfiguration.lavaBlockClasses.toHashSet()
        sunMultiplierPhases = modConfiguration.sunMultiplierPhases.sorted()
        skyColorPhases = modConfiguration.skyColorPhases.sorted()
        heatLayers = modConfiguration.heatLayers.sorted()
        lightningPhases = modConfiguration.lightningPhases.sorted()
        dimensionWhitelist = modConfiguration.dimensionWhitelist.toHashSet()
        skyColors = skyColorPhases.map { it.skyColor }.toHashSet()
    }
    override fun onInitialize() {
        AutoConfig.register(ModConfiguration::class.java) { definition: Config, configClass: Class<ModConfiguration> ->
            GsonConfigSerializer(definition, configClass)
        }.registerSaveListener { _, modConfiguration ->
            configCache(modConfiguration)
            calculateBlocks()
            ActionResult.PASS
        }
        config = AutoConfig.getConfigHolder(ModConfiguration::class.java).config
        configCache(config)
        ServerLifecycleEvents.SERVER_STARTING.register(ServerLifecycleEvents.ServerStarting { calculateBlocks() })
        Registry.register(Registries.BLOCK, DUST_IDENTIFIER, DUST)
        val dustBlockItem = Registry.register(Registries.ITEM, DUST_IDENTIFIER, BlockItem(DUST, Item.Settings().useBlockPrefixedTranslationKey().registryKey(RegistryKey.of(RegistryKeys.ITEM, DUST_IDENTIFIER))))
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ItemGroupEvents.ModifyEntries { it.add(dustBlockItem) })
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "sunscreen"), Sunscreen)
        sunscreen = Registries.STATUS_EFFECT.getEntry(Identifier.of(MOD_ID, "sunscreen")).get()
        ServerPlayerEvents.AFTER_RESPAWN.register(ServerPlayerEvents.AfterRespawn { _, newPlayer, _ ->
            val world = newPlayer.world
            if (!world.isOldEnough(config.phaseTwoDay) || !newPlayer.isAlive || world.isRaining || newPlayer.isSpectator || newPlayer.isCreative || world.isNight || world.isClient || (!world.isSkyVisible(newPlayer.blockPos) && !newPlayer.shouldHeatLayerDamage(world)) || newPlayer.hasStatusEffect(sunscreen)) return@AfterRespawn
            newPlayer.addStatusEffect(StatusEffectInstance(sunscreen, 2400, 0, false, false, true))
        })
    }
    val DUST_IDENTIFIER: Identifier = Identifier.of(MOD_ID, "dust")
    private val DUST = ColoredFallingBlock(ColorCode(0x191919), AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sounds(BlockSoundGroup.SAND).registryKey(RegistryKey.of(RegistryKeys.BLOCK, DUST_IDENTIFIER)))
}