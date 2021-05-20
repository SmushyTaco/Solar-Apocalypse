package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;
@Mixin(AbstractBlock.class)
public abstract class BlocksAreModifiedInDaylight {
    @Mutable
    @Final
    @Shadow
    protected boolean randomTicks;
    @Final
    @Shadow
    protected Material material;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void hookInit(AbstractBlock.Settings settings, CallbackInfo ci) {
        if (material.isBurnable()) randomTicks = true;
    }
    @Inject(method = "randomTick", at = @At("HEAD"))
    private void hookRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockPos blockPos = pos.offset(Direction.UP);
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay()) || world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos)) return;
        if (state.getMaterial().isBurnable() && world.getBlockState(blockPos).isAir()) {
            BlockState blockState = AbstractFireBlock.getState(world, blockPos);
            world.setBlockState(blockPos, blockState, 11);
        }
        if (state.getBlock() == Blocks.CLAY) {
            world.setBlockState(pos, Blocks.TERRACOTTA.getDefaultState());
        } else if (state.getBlock() == Blocks.DIRT) {
            world.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState());
        } else if (state.getBlock() == Blocks.FARMLAND) {
            FarmlandBlock.setToDirt(state, world, pos);
        } else if (state.getBlock() instanceof GourdBlock || state.getBlock() instanceof CarvedPumpkinBlock) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }
}