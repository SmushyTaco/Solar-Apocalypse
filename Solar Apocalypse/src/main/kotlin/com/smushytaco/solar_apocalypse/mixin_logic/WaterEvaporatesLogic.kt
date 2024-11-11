package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.SolarApocalypse.apocalypseChecks
import net.minecraft.block.*
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.fluid.WaterFluid
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
object WaterEvaporatesLogic {
    fun onRandomTick(fluid: Fluid, world: ServerWorld, pos: BlockPos, state: FluidState) {
        if (fluid !is WaterFluid || state.fluid !== Fluids.WATER || !world.apocalypseChecks(pos)) return
        val blockState = world.getBlockState(pos)
        val block = blockState.block
        when {
            block is FluidDrainable && block.tryDrainFluid(null, world, pos, blockState).isEmpty && block is FluidBlock -> world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
            blockState.isOf(Blocks.KELP) || blockState.isOf(Blocks.KELP_PLANT) || blockState.isOf(Blocks.SEAGRASS) || blockState.isOf(Blocks.TALL_SEAGRASS) -> {
                val blockEntity = if (blockState.hasBlockEntity()) world.getBlockEntity(pos) else null
                Block.dropStacks(blockState, world, pos, blockEntity)
                world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
            }
        }
    }
}