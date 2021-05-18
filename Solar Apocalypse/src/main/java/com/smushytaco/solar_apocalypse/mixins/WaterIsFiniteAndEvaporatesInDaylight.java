package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Random;
import static net.minecraft.block.Block.dropStacks;
@Mixin(WaterFluid.class)
public abstract class WaterIsFiniteAndEvaporatesInDaylight extends Fluid {
    @Inject(method = "isInfinite", at = @At("HEAD"), cancellable = true)
    private void hookIsInfinite(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
    @Override
    protected boolean hasRandomTicks() { return true; }
    @Override
    protected void onRandomTick(World world, BlockPos pos, FluidState state, Random random) {
        BlockPos blockPos = pos.offset(Direction.UP);
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay()) || state.getFluid() != Fluids.WATER || world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos)) return;
        BlockState blockState = world.getBlockState(pos);
        Material material = blockState.getMaterial();
        if (blockState.getBlock() instanceof FluidDrainable) {
            ((FluidDrainable)blockState.getBlock()).tryDrainFluid(world, pos, blockState);
        } else if (blockState.getBlock() instanceof FluidBlock) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        } else if (material == Material.UNDERWATER_PLANT || material == Material.REPLACEABLE_UNDERWATER_PLANT) {
            BlockEntity blockEntity = blockState.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
            dropStacks(blockState, world, pos, blockEntity);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }
}