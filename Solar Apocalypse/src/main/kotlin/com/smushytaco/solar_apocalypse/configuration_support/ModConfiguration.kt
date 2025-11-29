package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypse.rgbToInt
import com.smushytaco.solar_apocalypse.SolarApocalypse.stringIdentifier
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import net.minecraft.world.level.block.Blocks
@Config(name = SolarApocalypse.MOD_ID)
class ModConfiguration: ConfigData {
    val apocalypseRandomTickSpeed = 3
    val phaseOneDay = 20.0
    val turnToAirInsteadOfBurn = true
    val burnableBlockIdentifiers = listOf(
        Blocks.DRIED_GHAST.stringIdentifier,
        Blocks.CACTUS.stringIdentifier,
        Blocks.CACTUS_FLOWER.stringIdentifier,
        Blocks.SHORT_DRY_GRASS.stringIdentifier,
        Blocks.SHORT_GRASS.stringIdentifier,
        Blocks.TALL_DRY_GRASS.stringIdentifier,
        Blocks.TALL_GRASS.stringIdentifier,
        Blocks.BUSH.stringIdentifier,
        Blocks.FIREFLY_BUSH.stringIdentifier,
        Blocks.LEAF_LITTER.stringIdentifier,
        Blocks.WILDFLOWERS.stringIdentifier,
        Blocks.PUMPKIN.stringIdentifier,
        Blocks.CARVED_PUMPKIN.stringIdentifier,
        Blocks.JACK_O_LANTERN.stringIdentifier,
        Blocks.MELON.stringIdentifier,
        Blocks.HAY_BLOCK.stringIdentifier,
        Blocks.MUSHROOM_STEM.stringIdentifier,
        Blocks.BROWN_MUSHROOM.stringIdentifier,
        Blocks.BROWN_MUSHROOM_BLOCK.stringIdentifier,
        Blocks.RED_MUSHROOM.stringIdentifier,
        Blocks.RED_MUSHROOM_BLOCK.stringIdentifier,
        Blocks.COCOA.stringIdentifier,
        Blocks.MANGROVE_PROPAGULE.stringIdentifier,
        Blocks.OAK_SAPLING.stringIdentifier,
        Blocks.SPRUCE_SAPLING.stringIdentifier,
        Blocks.BIRCH_SAPLING.stringIdentifier,
        Blocks.JUNGLE_SAPLING.stringIdentifier,
        Blocks.ACACIA_SAPLING.stringIdentifier,
        Blocks.CHERRY_SAPLING.stringIdentifier,
        Blocks.DARK_OAK_SAPLING.stringIdentifier,
        Blocks.PALE_OAK_SAPLING.stringIdentifier,
        Blocks.SWEET_BERRY_BUSH.stringIdentifier,
        Blocks.SUGAR_CANE.stringIdentifier,
        Blocks.MOSS_BLOCK.stringIdentifier,
        Blocks.PALE_HANGING_MOSS.stringIdentifier,
        Blocks.MOSS_CARPET.stringIdentifier,
        Blocks.AZALEA.stringIdentifier,
        Blocks.FLOWERING_AZALEA.stringIdentifier,
        Blocks.GLOW_LICHEN.stringIdentifier,
        Blocks.CAVE_VINES.stringIdentifier,
        Blocks.CAVE_VINES_PLANT.stringIdentifier,
        Blocks.SPORE_BLOSSOM.stringIdentifier,
        Blocks.COBWEB.stringIdentifier,
        Blocks.OAK_SHELF.stringIdentifier,
        Blocks.SPRUCE_SHELF.stringIdentifier,
        Blocks.BIRCH_SHELF.stringIdentifier,
        Blocks.JUNGLE_SHELF.stringIdentifier,
        Blocks.ACACIA_SHELF.stringIdentifier,
        Blocks.DARK_OAK_SHELF.stringIdentifier,
        Blocks.MANGROVE_SHELF.stringIdentifier,
        Blocks.CHERRY_SHELF.stringIdentifier,
        Blocks.PALE_OAK_SHELF.stringIdentifier,
        Blocks.BAMBOO_SHELF.stringIdentifier,
        Blocks.TORCH.stringIdentifier,
        Blocks.WALL_TORCH.stringIdentifier,
        Blocks.SOUL_TORCH.stringIdentifier,
        Blocks.SOUL_WALL_TORCH.stringIdentifier,
        Blocks.COPPER_TORCH.stringIdentifier,
        Blocks.COPPER_WALL_TORCH.stringIdentifier,
        Blocks.REDSTONE_TORCH.stringIdentifier,
        Blocks.REDSTONE_WALL_TORCH.stringIdentifier,
        Blocks.GLASS.stringIdentifier,
        Blocks.GLASS_PANE.stringIdentifier
    )
    val burnableBlockTags = listOf("minecraft:wooden_doors", "minecraft:wooden_trapdoors", "minecraft:beds")
    val burnableBlockClasses = listOf("class_2261", "class_2504", "class_2506")
    val blockTransformationBlockToBlock = listOf(
        BlockPair(Blocks.CLAY, Blocks.TERRACOTTA),
        BlockPair(Blocks.MUD, Blocks.DIRT),
        BlockPair(Blocks.MUDDY_MANGROVE_ROOTS, Blocks.DIRT),
        BlockPair(Blocks.DIRT_PATH, Blocks.DIRT),
        BlockPair(Blocks.GRASS_BLOCK, Blocks.DIRT),
        BlockPair(Blocks.PODZOL, Blocks.DIRT),
        BlockPair(Blocks.MYCELIUM, Blocks.DIRT),
        BlockPair(Blocks.ROOTED_DIRT, Blocks.DIRT),
        BlockPair(Blocks.FARMLAND, Blocks.DIRT),
        BlockPair(Blocks.DIRT, Blocks.COARSE_DIRT),
        BlockPair(Blocks.COARSE_DIRT, Blocks.SAND),
        BlockPair(Blocks.BLUE_ICE, Blocks.PACKED_ICE),
        BlockPair(Blocks.PACKED_ICE, Blocks.ICE),
        BlockPair(Blocks.ICE, Blocks.WATER),
        BlockPair(Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS),
        BlockPair(Blocks.WET_SPONGE, Blocks.SPONGE),
        BlockPair(Blocks.INFESTED_STONE, Blocks.STONE),
        BlockPair(Blocks.INFESTED_COBBLESTONE, Blocks.COBBLESTONE),
        BlockPair(Blocks.INFESTED_STONE_BRICKS, Blocks.STONE_BRICKS),
        BlockPair(Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS),
        BlockPair(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS),
        BlockPair(Blocks.INFESTED_CHISELED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS),
        BlockPair(Blocks.INFESTED_DEEPSLATE, Blocks.DEEPSLATE),
        BlockPair(Blocks.STONE, Blocks.COBBLESTONE),
        BlockPair(Blocks.STONE_SLAB, Blocks.COBBLESTONE_SLAB),
        BlockPair(Blocks.STONE_STAIRS, Blocks.COBBLESTONE_STAIRS),
        BlockPair(Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE),
        BlockPair(Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS),
        BlockPair(Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES),
        BlockPair(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE),
        BlockPair(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.COBBLESTONE_SLAB),
        BlockPair(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.COBBLESTONE_STAIRS),
        BlockPair(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL),
        BlockPair(Blocks.MOSSY_STONE_BRICKS, Blocks.STONE_BRICKS),
        BlockPair(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.STONE_BRICK_SLAB),
        BlockPair(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.STONE_BRICK_STAIRS),
        BlockPair(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.STONE_BRICK_WALL),
        BlockPair(Blocks.SNOW, Blocks.WATER),
        BlockPair(Blocks.SNOW_BLOCK, Blocks.WATER),
        BlockPair(Blocks.POWDER_SNOW, Blocks.WATER),
        BlockPair(Blocks.SAND, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.GRAVEL, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.RED_SAND, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SANDSTONE, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SANDSTONE_STAIRS, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SANDSTONE_SLAB, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SANDSTONE_WALL, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.CHISELED_SANDSTONE, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SMOOTH_SANDSTONE, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SMOOTH_SANDSTONE_STAIRS, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SMOOTH_SANDSTONE_SLAB, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.CUT_SANDSTONE, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.CUT_SANDSTONE_SLAB, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.RED_SANDSTONE, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.RED_SANDSTONE_STAIRS, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.RED_SANDSTONE_SLAB, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.RED_SANDSTONE_WALL, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.CHISELED_RED_SANDSTONE, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SMOOTH_RED_SANDSTONE, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SMOOTH_RED_SANDSTONE_STAIRS, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.SMOOTH_RED_SANDSTONE_SLAB, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.CUT_RED_SANDSTONE, SolarApocalypse.DUST_IDENTIFIER.toString()),
        BlockPair(Blocks.CUT_RED_SANDSTONE_SLAB, SolarApocalypse.DUST_IDENTIFIER.toString())
    )
    val blockTransformationTagToBlock = listOf(TagAndBlock("minecraft:leaves", Blocks.AIR))
    val blockTransformationClassToBlock = listOf<ClassAndBlock>()
    val phaseTwoDay = 40.0
    val blocksTurnToLavaDay = -1.0
    val lavaBlockIdentifiers = listOf(
        Blocks.COBBLESTONE.stringIdentifier,
        Blocks.COBBLESTONE_SLAB.stringIdentifier,
        Blocks.COBBLESTONE_STAIRS.stringIdentifier,
        Blocks.COBBLESTONE_WALL.stringIdentifier,
        Blocks.COBBLED_DEEPSLATE.stringIdentifier,
        Blocks.COBBLED_DEEPSLATE_SLAB.stringIdentifier,
        Blocks.COBBLED_DEEPSLATE_STAIRS.stringIdentifier,
        Blocks.COBBLED_DEEPSLATE_WALL.stringIdentifier,
        Blocks.CRACKED_DEEPSLATE_TILES.stringIdentifier,
        Blocks.CRACKED_DEEPSLATE_BRICKS.stringIdentifier,
        Blocks.CRACKED_STONE_BRICKS.stringIdentifier
    )
    val lavaBlockTags = listOf<String>()
    val lavaBlockClasses = listOf<String>()
    val cropGrowthSlowDownDay = 20.0
    val cropGrowthSlowDownMultiplier = 3
    val solarFireDamageMultiplier = 5.0F
    val enableCustomSkyLight = true
    val enableHeatOverlay = true
    val heatOverlayFadeTime = 3.0
    val sunSizeTransitionTime = 3.0
    val skyColorTransitionTime = 3.0
    val fogColorTransitionTime = 3.0
    val apocalypseFogDay = 20.0
    val apocalypseFadeTime = 3.0
    val apocalypseFogMaximumDistance = 192.0F
    val apocalypseFogMultiplier = 0.5F
    val noCloudsDay = 40.0
    val cloudFadeTime = 3.0
    val sunMultiplierPhases = listOf(SunMultiplierPair(20.0, 2.0F), SunMultiplierPair(40.0, 3.0F), SunMultiplierPair(60.0, 3.25F), SunMultiplierPair(80.0, 3.5F), SunMultiplierPair(100.0, 3.75F), SunMultiplierPair(120.0, 4.0F), SunMultiplierPair(140.0, 5.00F))
    val skyColorPhases = listOf(SkyColorPair(20.0, rgbToInt(255, 140, 0)), SkyColorPair(40.0, rgbToInt(255, 69, 0)), SkyColorPair(60.0, rgbToInt(255, 50, 0)), SkyColorPair(80.0, rgbToInt(255, 40, 0)), SkyColorPair(100.0, rgbToInt(255, 30, 0)), SkyColorPair(120.0, rgbToInt(255, 30, 0)), SkyColorPair(140.0, rgbToInt(255, 0, 0)))
    val enableHeatLayers = true
    val enableHeatLayersOnBlocks = false
    val heatLayers = listOf(HeatLayer(60.0, 100.0), HeatLayer(80.0, 60.0), HeatLayer(100.0, 50.0), HeatLayer(120.0, 25.0), HeatLayer(140.0, 0.0))
    val enableLightningPhases = true
    val lightningPhases = listOf(LightningPhase(60.0, 2.0), LightningPhase(80.0, 4.0), LightningPhase(100.0, 8.0), LightningPhase(120.0, 16.0), LightningPhase(140.0, 32.0))
    val dimensionWhitelist = listOf("minecraft:overworld")
}