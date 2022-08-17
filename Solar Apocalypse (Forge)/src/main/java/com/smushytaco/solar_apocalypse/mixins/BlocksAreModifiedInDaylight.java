package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import com.smushytaco.solar_apocalypse.configuration_support.CoarseDirtToSandOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;
@Mixin(value = BlockBehaviour.class)
public abstract class BlocksAreModifiedInDaylight {
    @Mutable
    @Final
    @Shadow
    protected boolean isRandomlyTicking;
    @Final
    @Shadow
    protected Material material;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void hookInit(BlockBehaviour.Properties settings, CallbackInfo ci) {
        if (material.isFlammable()) isRandomlyTicking = true;
    }
    @Inject(method = "randomTick", at = @At("HEAD"))
    private void hookRandomTick(BlockState state, ServerLevel world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockPos blockPos = pos.relative(Direction.UP);
        if (world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        if (WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay())) {
            if (state.getMaterial().isFlammable() && world.getBlockState(blockPos).isAir()) {
                BlockState blockState = BaseFireBlock.getState(world, blockPos);
                world.setBlock(blockPos, blockState, Block.UPDATE_ALL | Block.UPDATE_IMMEDIATE);
            }
            if (state.getBlock() == Blocks.CLAY) {
                world.setBlockAndUpdate(pos, Blocks.TERRACOTTA.defaultBlockState());
            } else if (state.getBlock() == Blocks.DIRT) {
                world.setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState());
            } else if (state.getBlock() == Blocks.FARMLAND) {
                FarmBlock.turnToDirt(state, world, pos);
            } else if (state.getBlock() instanceof StemGrownBlock || state.getBlock() instanceof CarvedPumpkinBlock) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
        if (state.getBlock() == Blocks.COARSE_DIRT && SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase() != CoarseDirtToSandOptions.NONE) {
            if (SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase() == CoarseDirtToSandOptions.MYCELIUM_AND_GRASS_TURN_TO_DIRT_IN_DAYLIGHT_PHASE && WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightDay())) {
                world.setBlockAndUpdate(pos, Blocks.SAND.defaultBlockState());
            } else if (SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase() == CoarseDirtToSandOptions.BLOCKS_AND_WATER_ARE_AFFECTED_BY_DAYLIGHT_PHASE && WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay())) {
                world.setBlockAndUpdate(pos, Blocks.SAND.defaultBlockState());
            } else if (SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase() == CoarseDirtToSandOptions.MOBS_AND_PLAYERS_BURN_IN_DAYLIGHT_PHASE && WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay())) {
                world.setBlockAndUpdate(pos, Blocks.SAND.defaultBlockState());
            }
        }
    }
}