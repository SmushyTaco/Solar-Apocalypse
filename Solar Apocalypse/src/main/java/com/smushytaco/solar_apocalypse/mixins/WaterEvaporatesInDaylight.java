package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static net.minecraft.block.Block.dropStacks;
@Mixin(Fluid.class)
public abstract class WaterEvaporatesInDaylight {
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    @SuppressWarnings("ConstantConditions")
    protected boolean hookHasRandomTicks(boolean original) { return (Fluid) (Object) this instanceof WaterFluid || original; }
    @Inject(method = "onRandomTick", at = @At("HEAD"))
    @SuppressWarnings("ConstantConditions")
    protected void onRandomTick(World world, BlockPos pos, FluidState state, Random random, CallbackInfo ci) {
        if (!((Fluid) (Object) this instanceof WaterFluid)) return;
        BlockPos blockPos = pos.offset(Direction.UP);
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay()) || state.getFluid() != Fluids.WATER || world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos)) return;
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof FluidDrainable fluidDrainable) {
            fluidDrainable.tryDrainFluid(world, pos, blockState);
        } else if (blockState.getBlock() instanceof FluidBlock) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
        } else if (blockState.isOf(Blocks.KELP) || blockState.isOf(Blocks.KELP_PLANT) || blockState.isOf(Blocks.SEAGRASS) || blockState.isOf(Blocks.TALL_SEAGRASS)) {
            BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            dropStacks(blockState, world, pos, blockEntity);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
        }
    }
}