package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.ApocalypseTickable
import com.smushytaco.solar_apocalypse.BlockCache
import com.smushytaco.solar_apocalypse.SolarApocalypse
import com.smushytaco.solar_apocalypse.SolarApocalypse.block
import com.smushytaco.solar_apocalypse.SolarApocalypse.config
import com.smushytaco.solar_apocalypse.SolarApocalypse.shouldHeatLayerDamage
import com.smushytaco.solar_apocalypse.SolarApocalypse.stringIdentifier
import com.smushytaco.solar_apocalypse.WorldDayCalculation.isOldEnough
import net.minecraft.block.*
import net.minecraft.block.enums.BlockHalf
import net.minecraft.block.enums.SlabType
import net.minecraft.block.enums.StairShape
import net.minecraft.block.enums.WallShape
import net.minecraft.fluid.Fluids
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
import kotlin.jvm.optionals.getOrNull
object BlocksAreModifiedLogic {
    private fun blockChanges(blockOne: Block, blockTwo: Block, serverWorld: ServerWorld, blockPos: BlockPos, instance: BlockState) {
        when (blockOne) {
            is SlabBlock if blockTwo is SlabBlock -> serverWorld.setBlockState(blockPos, blockTwo.defaultState.with(SlabBlock.TYPE, instance.getOrEmpty(SlabBlock.TYPE).getOrNull() ?: SlabType.BOTTOM).with(SlabBlock.WATERLOGGED, instance.getOrEmpty(SlabBlock.WATERLOGGED).getOrNull() ?: false))
            is StairsBlock if blockTwo is StairsBlock -> serverWorld.setBlockState(blockPos, blockTwo.defaultState.with(StairsBlock.FACING, instance.getOrEmpty(StairsBlock.FACING).getOrNull() ?: Direction.NORTH).with(StairsBlock.HALF, instance.getOrEmpty(StairsBlock.HALF).getOrNull() ?: BlockHalf.BOTTOM).with(StairsBlock.SHAPE, instance.getOrEmpty(StairsBlock.SHAPE).getOrNull() ?: StairShape.STRAIGHT).with(StairsBlock.WATERLOGGED, instance.getOrEmpty(StairsBlock.WATERLOGGED).getOrNull() ?: false))
            is WallBlock if blockTwo is WallBlock -> serverWorld.setBlockState(blockPos, blockTwo.defaultState.with(WallBlock.UP, instance.getOrEmpty(WallBlock.UP).getOrNull() ?: true).with(WallBlock.NORTH_WALL_SHAPE, instance.getOrEmpty(WallBlock.NORTH_WALL_SHAPE).getOrNull() ?: WallShape.NONE).with(WallBlock.EAST_WALL_SHAPE, instance.getOrEmpty(WallBlock.EAST_WALL_SHAPE).getOrNull() ?: WallShape.NONE).with(WallBlock.SOUTH_WALL_SHAPE, instance.getOrEmpty(WallBlock.SOUTH_WALL_SHAPE).getOrNull() ?: WallShape.NONE).with(WallBlock.WEST_WALL_SHAPE, instance.getOrEmpty(WallBlock.WEST_WALL_SHAPE).getOrNull() ?: WallShape.NONE).with(WallBlock.WATERLOGGED, instance.getOrEmpty(WallBlock.WATERLOGGED).getOrNull() ?: false))
            Blocks.WET_SPONGE if blockTwo == Blocks.SPONGE -> {
                serverWorld.setBlockState(blockPos, Blocks.SPONGE.defaultState, Block.NOTIFY_ALL)
                serverWorld.syncWorldEvent(WorldEvents.WET_SPONGE_DRIES_OUT, blockPos, 0)
                serverWorld.playSound(null, blockPos, SoundEvents.BLOCK_WET_SPONGE_DRIES, SoundCategory.BLOCKS, 1.0F, (1.0F + serverWorld.getRandom().nextFloat() * 0.2F) * 0.7F)
            }
            Blocks.FARMLAND -> {
                val blockState = Block.pushEntitiesUpBeforeBlockChange(instance, blockTwo.defaultState, serverWorld, blockPos)
                serverWorld.setBlockState(blockPos, blockState)
                serverWorld.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(null, blockState))
            }
            else -> serverWorld.setBlockState(blockPos, blockTwo.defaultState)
        }
    }
    fun apocalypseRandomTicks(world: ServerWorld, profiler: Profiler, chunk: WorldChunk, startX: Int, startZ: Int) {
        profiler.swap("tickApocalypseBlocks")
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
                if (blockState.fluidState.fluid == Fluids.WATER) WaterEvaporatesLogic.randomTick(world, blockState, blockPos)
                profiler.pop()
            }
        }
        profiler.pop()
    }
    private fun randomTick(serverWorld: ServerWorld, blockState: BlockState, blockPos: BlockPos) {
        val pos = blockPos.up()
        if (serverWorld.isNight || serverWorld.isRaining || (!serverWorld.isSkyVisible(pos) && !blockPos.shouldHeatLayerDamage(serverWorld))) return
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
            serverWorld.setBlockState(blockPos, Blocks.LAVA.defaultState)
            return
        }
        if (serverWorld.getBlockState(blockPos).isBurnable && phaseOneDayCheck) {
            if (config.turnToAirInsteadOfBurn) {
                serverWorld.setBlockState(blockPos, Blocks.AIR.defaultState)
            } else {
                val posState = serverWorld.getBlockState(pos)
                if (!serverWorld.isOutOfHeightLimit(pos) && posState.isAir) {
                    serverWorld.setBlockState(pos, AbstractFireBlock.getState(serverWorld, pos), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
                } else {
                    if (posState.block is AbstractFireBlock) return
                    val east = blockPos.east()
                    val eastState = serverWorld.getBlockState(east)
                    if (!serverWorld.isOutOfHeightLimit(east) && eastState.isAir) {
                        serverWorld.setBlockState(east, AbstractFireBlock.getState(serverWorld, east), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
                    } else {
                        if (eastState.block is AbstractFireBlock) return
                        val west = blockPos.west()
                        val westState = serverWorld.getBlockState(west)
                        if (!serverWorld.isOutOfHeightLimit(west) && westState.isAir) {
                            serverWorld.setBlockState(west, AbstractFireBlock.getState(serverWorld, west), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
                        } else {
                            if (westState.block is AbstractFireBlock) return
                            val north = blockPos.north()
                            val northState = serverWorld.getBlockState(north)
                            if (!serverWorld.isOutOfHeightLimit(north) && northState.isAir) {
                                serverWorld.setBlockState(north, AbstractFireBlock.getState(serverWorld, north), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
                            } else {
                                if (northState.block is AbstractFireBlock) return
                                val south = blockPos.south()
                                val southState = serverWorld.getBlockState(south)
                                if (!serverWorld.isOutOfHeightLimit(south) && southState.isAir) {
                                    serverWorld.setBlockState(south, AbstractFireBlock.getState(serverWorld, south), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
                                } else {
                                    if (southState.block is AbstractFireBlock) return
                                    val down = blockPos.down()
                                    val downState = serverWorld.getBlockState(down)
                                    if (!serverWorld.isOutOfHeightLimit(down) && downState.isAir) {
                                        serverWorld.setBlockState(down, AbstractFireBlock.getState(serverWorld, down), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
                                    } else {
                                        if (downState.block is AbstractFireBlock) return
                                        serverWorld.setBlockState(blockPos, Blocks.AIR.defaultState)
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