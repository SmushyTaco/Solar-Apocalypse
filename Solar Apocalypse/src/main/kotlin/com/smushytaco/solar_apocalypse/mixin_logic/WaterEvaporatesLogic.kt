package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.SolarApocalypse.apocalypseChecks
import net.minecraft.block.*
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
object WaterEvaporatesLogic {
    fun randomTick(serverWorld: ServerWorld, blockState: BlockState, blockPos: BlockPos) {
        if (!serverWorld.apocalypseChecks(blockPos)) return
        val block = blockState.block
        when {
            block is FluidDrainable && block.tryDrainFluid(serverWorld, blockPos, blockState).isEmpty && block is FluidBlock -> serverWorld.setBlockState(blockPos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
            blockState.isOf(Blocks.KELP) || blockState.isOf(Blocks.KELP_PLANT) || blockState.isOf(Blocks.SEAGRASS) || blockState.isOf(Blocks.TALL_SEAGRASS) -> {
                val blockEntity = if (blockState.hasBlockEntity()) serverWorld.getBlockEntity(blockPos) else null
                Block.dropStacks(blockState, serverWorld, blockPos, blockEntity)
                serverWorld.setBlockState(blockPos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
            }
        }
    }
}