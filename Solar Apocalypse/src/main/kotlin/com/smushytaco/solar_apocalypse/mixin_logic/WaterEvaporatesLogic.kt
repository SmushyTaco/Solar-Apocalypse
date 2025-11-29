package com.smushytaco.solar_apocalypse.mixin_logic
import com.smushytaco.solar_apocalypse.SolarApocalypse.apocalypseChecks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BucketPickup
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
object WaterEvaporatesLogic {
    fun randomTick(serverWorld: ServerLevel, blockState: BlockState, blockPos: BlockPos) {
        if (!serverWorld.apocalypseChecks(blockPos)) return
        val block = blockState.block
        when {
            block is BucketPickup && block.pickupBlock(null, serverWorld, blockPos, blockState).isEmpty && block is LiquidBlock -> serverWorld.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL)
            blockState.`is`(Blocks.KELP) || blockState.`is`(Blocks.KELP_PLANT) || blockState.`is`(Blocks.SEAGRASS) || blockState.`is`(
                Blocks.TALL_SEAGRASS) -> {
                val blockEntity = if (blockState.hasBlockEntity()) serverWorld.getBlockEntity(blockPos) else null
                Block.dropResources(blockState, serverWorld, blockPos, blockEntity)
                serverWorld.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL)
            }
        }
    }
}