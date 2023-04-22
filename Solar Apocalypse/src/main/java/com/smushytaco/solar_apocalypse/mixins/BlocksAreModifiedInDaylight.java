package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import com.smushytaco.solar_apocalypse.configuration_support.CoarseDirtToSandOptions;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
        if (world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos)) return;
        if (WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay())) {
            if (state.getMaterial().isBurnable() && world.getBlockState(blockPos).isAir()) {
                BlockState blockState = AbstractFireBlock.getState(world, blockPos);
                world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            }
            if (state.getBlock() == Blocks.CLAY) {
                world.setBlockState(pos, Blocks.TERRACOTTA.getDefaultState());
            } else if (state.getBlock() == Blocks.DIRT) {
                world.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState());
            } else if (state.getBlock() == Blocks.FARMLAND) {
                FarmlandBlock.setToDirt(null, state, world, pos);
            } else if (state.getBlock() instanceof GourdBlock || state.getBlock() instanceof CarvedPumpkinBlock) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
        if (state.getBlock() == Blocks.COARSE_DIRT && SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase() != CoarseDirtToSandOptions.NONE) {
            if (SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase() == CoarseDirtToSandOptions.MYCELIUM_AND_GRASS_TURN_TO_DIRT_IN_DAYLIGHT_PHASE && WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightDay())) {
                world.setBlockState(pos, Blocks.SAND.getDefaultState());
            } else if (SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase() == CoarseDirtToSandOptions.BLOCKS_AND_WATER_ARE_AFFECTED_BY_DAYLIGHT_PHASE && WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay())) {
                world.setBlockState(pos, Blocks.SAND.getDefaultState());
            } else if (SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase() == CoarseDirtToSandOptions.MOBS_AND_PLAYERS_BURN_IN_DAYLIGHT_PHASE && WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay())) {
                world.setBlockState(pos, Blocks.SAND.getDefaultState());
            }
        }
    }
}