package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.ApocalypseTickable
import com.smushytaco.solar_apocalypse.BlockCache
import com.smushytaco.solar_apocalypse.SolarApocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypse.block
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypse.shouldHeatLayerDamage
import com.smushytaco.solar_apocalypse.SolarApocalypse.stringIdentifier
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.SectionPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.block.state.properties.SlabType
import net.minecraft.world.level.block.state.properties.StairsShape
import net.minecraft.world.level.block.state.properties.WallSide
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.material.Fluids
import kotlin.jvm.optionals.getOrNull
object BlocksAreModifiedLogic {
    private fun blockChanges(blockOne: Block, blockTwo: Block, serverWorld: ServerLevel, blockPos: BlockPos, instance: BlockState) {
        when (blockOne) {
            is SlabBlock if blockTwo is SlabBlock -> serverWorld.setBlockAndUpdate(blockPos, blockTwo.defaultBlockState()
                .setValue(SlabBlock.TYPE, instance.getOptionalValue(SlabBlock.TYPE).getOrNull() ?: SlabType.BOTTOM).setValue(
                SlabBlock.WATERLOGGED, instance.getOptionalValue(SlabBlock.WATERLOGGED).getOrNull() ?: false))
            is StairBlock if blockTwo is StairBlock -> serverWorld.setBlockAndUpdate(blockPos, blockTwo.defaultBlockState()
                .setValue(StairBlock.FACING, instance.getOptionalValue(StairBlock.FACING).getOrNull() ?: Direction.NORTH).setValue(
                StairBlock.HALF, instance.getOptionalValue(StairBlock.HALF).getOrNull() ?: Half.BOTTOM).setValue(
                StairBlock.SHAPE, instance.getOptionalValue(StairBlock.SHAPE).getOrNull() ?: StairsShape.STRAIGHT).setValue(
                StairBlock.WATERLOGGED, instance.getOptionalValue(StairBlock.WATERLOGGED).getOrNull() ?: false))
            is WallBlock if blockTwo is WallBlock -> serverWorld.setBlockAndUpdate(blockPos, blockTwo.defaultBlockState()
                .setValue(WallBlock.UP, instance.getOptionalValue(WallBlock.UP).getOrNull() ?: true).setValue(
                WallBlock.NORTH, instance.getOptionalValue(
                    WallBlock.NORTH
                ).getOrNull() ?: WallSide.NONE).setValue(WallBlock.EAST, instance.getOptionalValue(WallBlock.EAST).getOrNull() ?: WallSide.NONE).setValue(
                WallBlock.SOUTH, instance.getOptionalValue(WallBlock.SOUTH).getOrNull() ?: WallSide.NONE).setValue(
                WallBlock.WEST, instance.getOptionalValue(WallBlock.WEST).getOrNull() ?: WallSide.NONE).setValue(
                WallBlock.WATERLOGGED, instance.getOptionalValue(WallBlock.WATERLOGGED).getOrNull() ?: false))
            Blocks.WET_SPONGE if blockTwo == Blocks.SPONGE -> {
                serverWorld.setBlock(blockPos, Blocks.SPONGE.defaultBlockState(), Block.UPDATE_ALL)
                serverWorld.levelEvent(LevelEvent.PARTICLES_WATER_EVAPORATING, blockPos, 0)
                serverWorld.playSound(null, blockPos, SoundEvents.WET_SPONGE_DRIES, SoundSource.BLOCKS, 1.0F, (1.0F + serverWorld.getRandom().nextFloat() * 0.2F) * 0.7F)
            }
            Blocks.FARMLAND -> {
                val blockState = Block.pushEntitiesUp(instance, blockTwo.defaultBlockState(), serverWorld, blockPos)
                serverWorld.setBlockAndUpdate(blockPos, blockState)
                serverWorld.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(null, blockState))
            }
            else -> serverWorld.setBlockAndUpdate(blockPos, blockTwo.defaultBlockState())
        }
    }
    fun apocalypseRandomTicks(world: ServerLevel, profiler: ProfilerFiller, chunk: LevelChunk, startX: Int, startZ: Int) {
        profiler.popPush("tickApocalypseBlocks")
        if (config.apocalypseRandomTickSpeed < 1) {
            profiler.pop()
            return
        }
        chunk.sections.forEachIndexed { index, chunkSection ->
            if (chunkSection !is ApocalypseTickable || chunkSection.randomApocalypseTickableBlockCount < 1) return@forEachIndexed
            val blockCoordinate = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(index))
            repeat(config.apocalypseRandomTickSpeed) {
                val blockPos = world.getBlockRandomPos(startX, blockCoordinate, startZ, 15)
                profiler.push("randomApocalypseTick")
                val blockState = chunkSection.getBlockState(blockPos.x - startX, blockPos.y - blockCoordinate, blockPos.z - startZ)
                if ((blockState.block as BlockCache).cacheShouldRandomTick) randomTick(world, blockState, blockPos)
                if (blockState.fluidState.type == Fluids.WATER) WaterEvaporatesLogic.randomTick(world, blockState, blockPos)
                profiler.pop()
            }
        }
        profiler.pop()
    }
    private fun randomTick(serverWorld: ServerLevel, blockState: BlockState, blockPos: BlockPos) {
        val pos = blockPos.above()
        if (serverWorld.isDarkOutside || serverWorld.isRaining || (!serverWorld.canSeeSky(pos) && !blockPos.shouldHeatLayerDamage(serverWorld))) return
        val phaseOneDayCheck = serverWorld.isOldEnough(config.phaseOneDay)
        val lavaDayCheck = serverWorld.isOldEnough(config.blocksTurnToLavaDay)
        if (phaseOneDayCheck && lavaDayCheck) {
            SolarApocalypse.blockTransformationBlockToBlockMapWithLava[blockState.block.stringIdentifier]?.let {
                blockChanges(blockState.block, it.random().block, serverWorld, blockPos, blockState)
                return
            }
        }
        if (phaseOneDayCheck) {
            SolarApocalypse.blockTransformationBlockToBlockMap[blockState.block.stringIdentifier]?.let {
                blockChanges(blockState.block, it.random().block, serverWorld, blockPos, blockState)
                return
            }
        }
        if (SolarApocalypse.lavaBlockIdentifiers.contains(blockState.block.stringIdentifier) && lavaDayCheck) {
            serverWorld.setBlockAndUpdate(blockPos, Blocks.LAVA.defaultBlockState())
            return
        }
        if (serverWorld.getBlockState(blockPos).ignitedByLava() && phaseOneDayCheck) {
            if (config.turnToAirInsteadOfBurn) {
                serverWorld.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState())
            } else {
                val posState = serverWorld.getBlockState(pos)
                if (!serverWorld.isOutsideBuildHeight(pos) && posState.isAir) {
                    serverWorld.setBlock(pos, BaseFireBlock.getState(serverWorld, pos), Block.UPDATE_ALL or Block.UPDATE_IMMEDIATE)
                } else {
                    if (posState.block is BaseFireBlock) return
                    val east = blockPos.east()
                    val eastState = serverWorld.getBlockState(east)
                    if (!serverWorld.isOutsideBuildHeight(east) && eastState.isAir) {
                        serverWorld.setBlock(east, BaseFireBlock.getState(serverWorld, east), Block.UPDATE_ALL or Block.UPDATE_IMMEDIATE)
                    } else {
                        if (eastState.block is BaseFireBlock) return
                        val west = blockPos.west()
                        val westState = serverWorld.getBlockState(west)
                        if (!serverWorld.isOutsideBuildHeight(west) && westState.isAir) {
                            serverWorld.setBlock(west, BaseFireBlock.getState(serverWorld, west), Block.UPDATE_ALL or Block.UPDATE_IMMEDIATE)
                        } else {
                            if (westState.block is BaseFireBlock) return
                            val north = blockPos.north()
                            val northState = serverWorld.getBlockState(north)
                            if (!serverWorld.isOutsideBuildHeight(north) && northState.isAir) {
                                serverWorld.setBlock(north, BaseFireBlock.getState(serverWorld, north), Block.UPDATE_ALL or Block.UPDATE_IMMEDIATE)
                            } else {
                                if (northState.block is BaseFireBlock) return
                                val south = blockPos.south()
                                val southState = serverWorld.getBlockState(south)
                                if (!serverWorld.isOutsideBuildHeight(south) && southState.isAir) {
                                    serverWorld.setBlock(south, BaseFireBlock.getState(serverWorld, south), Block.UPDATE_ALL or Block.UPDATE_IMMEDIATE)
                                } else {
                                    if (southState.block is BaseFireBlock) return
                                    val down = blockPos.below()
                                    val downState = serverWorld.getBlockState(down)
                                    if (!serverWorld.isOutsideBuildHeight(down) && downState.isAir) {
                                        serverWorld.setBlock(down, BaseFireBlock.getState(serverWorld, down), Block.UPDATE_ALL or Block.UPDATE_IMMEDIATE)
                                    } else {
                                        if (downState.block is BaseFireBlock) return
                                        serverWorld.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}