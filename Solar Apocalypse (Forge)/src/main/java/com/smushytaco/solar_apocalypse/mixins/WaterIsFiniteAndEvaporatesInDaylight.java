package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
//import net.minecraft.block.*;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.fluid.Fluid;
//import net.minecraft.fluid.FluidState;
//import net.minecraft.fluid.Fluids;
//import net.minecraft.fluid.WaterFluid;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.world.World;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Random;
import static net.minecraft.world.level.block.Block.dropResources;
@Mixin(WaterFluid.class)
public abstract class WaterIsFiniteAndEvaporatesInDaylight extends Fluid {
    @Inject(method = "canConvertToSource", at = @At("HEAD"), cancellable = true)
    private void hookIsInfinite(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
    @Override
    protected boolean isRandomlyTicking() { return true; }
    @Override
    protected void randomTick(Level world, BlockPos pos, FluidState state, Random random) {
        BlockPos blockPos = pos.relative(Direction.UP);
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay()) || state.getType() != Fluids.WATER || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        BlockState blockState = world.getBlockState(pos);
        Material material = blockState.getMaterial();
        if (blockState.getBlock() instanceof BucketPickup) {
            ((BucketPickup)blockState.getBlock()).pickupBlock(world, pos, blockState);
        } else if (blockState.getBlock() instanceof LiquidBlock) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
            BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            dropResources(blockState, world, pos, blockEntity);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}