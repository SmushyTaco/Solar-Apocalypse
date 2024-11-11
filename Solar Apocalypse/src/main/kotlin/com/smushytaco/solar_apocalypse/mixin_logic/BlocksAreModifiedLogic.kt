package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.ApocalypseTickable
import com.smushytaco.solar_apocalypse.BlockCache
import com.smushytaco.solar_apocalypse.SolarApocalypse.block
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypse.containsTag
import com.smushytaco.solar_apocalypse.SolarApocalypse.isInstanceOfClassByName
import com.smushytaco.solar_apocalypse.SolarApocalypse.shouldHeatLayerDamage
import com.smushytaco.solar_apocalypse.SolarApocalypse.stringIdentifier
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.minecraft.block.*
import net.minecraft.block.enums.BlockHalf
import net.minecraft.block.enums.SlabType
import net.minecraft.block.enums.StairShape
import net.minecraft.block.enums.WallShape
import net.minecraft.fluid.Fluids
import net.minecraft.fluid.WaterFluid
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkSectionPos
import net.minecraft.util.math.Direction
import net.minecraft.util.profiler.Profiler
import net.minecraft.world.WorldEvents
import net.minecraft.world.chunk.WorldChunk
import net.minecraft.world.event.GameEvent
object BlocksAreModifiedLogic {
    private fun blockChanges(blockOne: Block, blockTwo: Block, serverWorld: ServerWorld, blockPos: BlockPos, instance: BlockState) {
        when {
            blockOne is SlabBlock && blockTwo is SlabBlock -> serverWorld.setBlockState(blockPos, blockTwo.defaultState.with(SlabBlock.TYPE, instance.getNullable(SlabBlock.TYPE) ?: SlabType.BOTTOM).with(SlabBlock.WATERLOGGED, instance.getNullable(SlabBlock.WATERLOGGED) ?: false))
            blockOne is StairsBlock && blockTwo is StairsBlock -> serverWorld.setBlockState(blockPos, blockTwo.defaultState.with(StairsBlock.FACING, instance.getNullable(StairsBlock.FACING) ?: Direction.NORTH).with(StairsBlock.HALF, instance.getNullable(StairsBlock.HALF) ?: BlockHalf.BOTTOM).with(StairsBlock.SHAPE, instance.getNullable(StairsBlock.SHAPE) ?: StairShape.STRAIGHT).with(StairsBlock.WATERLOGGED, instance.getNullable(StairsBlock.WATERLOGGED) ?: false))
            blockOne is WallBlock && blockTwo is WallBlock -> serverWorld.setBlockState(blockPos, blockTwo.defaultState.with(WallBlock.UP, instance.getNullable(WallBlock.UP) ?: true).with(WallBlock.NORTH_SHAPE, instance.getNullable(WallBlock.NORTH_SHAPE) ?: WallShape.NONE).with(WallBlock.EAST_SHAPE, instance.getNullable(WallBlock.EAST_SHAPE) ?: WallShape.NONE).with(WallBlock.SOUTH_SHAPE, instance.getNullable(WallBlock.SOUTH_SHAPE) ?: WallShape.NONE).with(WallBlock.WEST_SHAPE, instance.getNullable(WallBlock.WEST_SHAPE) ?: WallShape.NONE).with(WallBlock.WATERLOGGED, instance.getNullable(WallBlock.WATERLOGGED) ?: false))
            blockOne == Blocks.WET_SPONGE && blockTwo == Blocks.SPONGE -> {
                serverWorld.setBlockState(blockPos, Blocks.SPONGE.defaultState, Block.NOTIFY_ALL)
                serverWorld.syncWorldEvent(WorldEvents.WET_SPONGE_DRIES_OUT, blockPos, 0)
                serverWorld.playSound(null, blockPos, SoundEvents.BLOCK_WET_SPONGE_DRIES, SoundCategory.BLOCKS, 1.0F, (1.0F + serverWorld.getRandom().nextFloat() * 0.2F) * 0.7F)
            }
            blockOne == Blocks.FARMLAND -> {
                val blockState = Block.pushEntitiesUpBeforeBlockChange(instance, blockTwo.defaultState, serverWorld, blockPos)
                serverWorld.setBlockState(blockPos, blockState)
                serverWorld.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(null, blockState))
            }
            else -> serverWorld.setBlockState(blockPos, blockTwo.defaultState)
        }
    }
    fun apocalypseRandomTicks(world: ServerWorld, profiler: Profiler, chunk: WorldChunk, startX: Int, startZ: Int) {
        profiler.swap("tickApocalypseBlocks")
        @Suppress("KotlinConstantConditions")
        if (config.apocalypseRandomTickSpeed < 1) {
            profiler.pop()
            return
        }
        chunk.sectionArray.forEachIndexed { index, chunkSection ->
            if (chunkSection !is ApocalypseTickable || chunkSection.randomApocalypseTickableBlockCount < 1) return@forEachIndexed
            val blockCoordinate = ChunkSectionPos.getBlockCoord(chunk.sectionIndexToCoord(index))
            repeat(config.apocalypseRandomTickSpeed) {
                val blockPos = world.getRandomPosInChunk(startX, blockCoordinate, startZ, 15)
                profiler.push("randomApocalypseTick")
                val blockState = chunkSection.getBlockState(blockPos.x - startX, blockPos.y - blockCoordinate, blockPos.z - startZ)
                if ((blockState.block as BlockCache).cacheShouldRandomTick) randomTick(world, blockState, blockPos)
                if (blockState.fluidState.fluid is WaterFluid && blockState.fluidState.fluid == Fluids.WATER) WaterEvaporatesLogic.randomTick(world, blockState, blockPos)
                profiler.pop()
            }
        }
        profiler.pop()
    }
    private fun randomTick(serverWorld: ServerWorld, blockState: BlockState, blockPos: BlockPos) {
        val pos = blockPos.offset(Direction.UP)
        if (serverWorld.isNight || serverWorld.isRaining || (!serverWorld.isSkyVisible(pos) && !blockPos.shouldHeatLayerDamage(serverWorld))) return
        if (serverWorld.isOldEnough(config.phaseOneDay)) {
            for (blockPair in config.blockTransformationBlockToBlock) {
                if (blockState.block.stringIdentifier != blockPair.blockOne) continue
                blockChanges(blockState.block, blockPair.blockTwo.block, serverWorld, blockPos, blockState)
                return
            }
            for (tagAndBlock in config.blockTransformationTagToBlock) {
                if (!blockState.block.containsTag(tagAndBlock.tag)) continue
                blockChanges(blockState.block, tagAndBlock.block.block, serverWorld, blockPos, blockState)
                return
            }
            for (classAndBlock in config.blockTransformationClassToBlock) {
                if (!isInstanceOfClassByName(blockState.block, classAndBlock.className)) continue
                blockChanges(blockState.block, classAndBlock.block.block, serverWorld, blockPos, blockState)
                return
            }
        }
        if (serverWorld.isOldEnough(config.blocksTurnToLavaDay)) {
            for (block in config.lavaBlockIdentifiers) {
                if (blockState.block.stringIdentifier != block) continue
                serverWorld.setBlockState(blockPos, Blocks.LAVA.defaultState)
                return
            }
            for (tag in config.lavaBlockTags) {
                if (!blockState.block.containsTag(tag)) continue
                serverWorld.setBlockState(blockPos, Blocks.LAVA.defaultState)
                return
            }
            for (className in config.lavaBlockClasses) {
                if (!isInstanceOfClassByName(blockState.block, className)) continue
                serverWorld.setBlockState(blockPos, Blocks.LAVA.defaultState)
                return
            }
        }
        if (serverWorld.getBlockState(blockPos).isBurnable && serverWorld.getBlockState(pos).isAir && serverWorld.isOldEnough(config.phaseOneDay)) if (config.turnToAirInsteadOfBurn) serverWorld.setBlockState(blockPos, Blocks.AIR.defaultState) else if (!serverWorld.isOutOfHeightLimit(pos)) serverWorld.setBlockState(pos, AbstractFireBlock.getState(serverWorld, pos), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD) else serverWorld.setBlockState(blockPos, Blocks.AIR.defaultState)
    }
}