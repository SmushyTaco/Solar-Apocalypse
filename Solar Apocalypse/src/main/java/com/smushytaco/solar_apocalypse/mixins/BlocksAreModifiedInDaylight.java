package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
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
    @Inject(method = "<init>", at = @At("RETURN"))
    private void hookInit(AbstractBlock.Settings settings, CallbackInfo ci) { if (((GetBurnableAccessor) settings).getBurnable()) randomTicks = true; }
    @Inject(method = "randomTick", at = @At("HEAD"))
    private void hookRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockPos blockPos = pos.offset(Direction.UP);
        if (world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos)) return;
        if (WorldDayCalculation.INSTANCE.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay())) {
            if (state.isBurnable() && world.getBlockState(blockPos).isAir()) {
                BlockState blockState = AbstractFireBlock.getState(world, blockPos);
                world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            }
            if (state.getBlock() == Blocks.CLAY) {
                world.setBlockState(pos, Blocks.TERRACOTTA.getDefaultState());
            } else if (state.getBlock() == Blocks.DIRT) {
                world.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState());
            } else if (state.getBlock() == Blocks.STONE_BRICKS) {
                world.setBlockState(pos, Blocks.CRACKED_STONE_BRICKS.getDefaultState());
            } else if (state.getBlock() == Blocks.PACKED_ICE) {
                world.setBlockState(pos, Blocks.ICE.getDefaultState());
            } else if (state.getBlock() == Blocks.BLUE_ICE) {
                world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
            } else if (state.getBlock() == Blocks.STONE) {
                world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
            } else if (state.getBlock() == Blocks.STONE_SLAB) {
                world.setBlockState(pos, Blocks.COBBLESTONE_SLAB.getDefaultState().withIfExists(SlabBlock.TYPE, state.getNullable(SlabBlock.TYPE)).withIfExists(SlabBlock.WATERLOGGED, state.getNullable(SlabBlock.WATERLOGGED)));
            } else if (state.getBlock() == Blocks.STONE_STAIRS) {
                world.setBlockState(pos, Blocks.COBBLESTONE_STAIRS.getDefaultState().withIfExists(StairsBlock.FACING, state.getNullable(StairsBlock.FACING)).withIfExists(StairsBlock.HALF, state.getNullable(StairsBlock.HALF)).withIfExists(StairsBlock.SHAPE, state.getNullable(StairsBlock.SHAPE)).withIfExists(StairsBlock.WATERLOGGED, state.getNullable(StairsBlock.WATERLOGGED)));
            } else if (state.getBlock() == Blocks.DEEPSLATE) {
                world.setBlockState(pos, Blocks.COBBLED_DEEPSLATE.getDefaultState());
            } else if (state.getBlock() == Blocks.DEEPSLATE_BRICKS) {
                world.setBlockState(pos, Blocks.CRACKED_DEEPSLATE_BRICKS.getDefaultState());
            } else if (state.getBlock() == Blocks.DEEPSLATE_TILES) {
                world.setBlockState(pos, Blocks.CRACKED_DEEPSLATE_TILES.getDefaultState());
            } else if (state.getBlock() == Blocks.MOSSY_COBBLESTONE) {
                world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
            } else if (state.getBlock() == Blocks.MOSSY_COBBLESTONE_SLAB) {
                world.setBlockState(pos, Blocks.COBBLESTONE_SLAB.getDefaultState().withIfExists(SlabBlock.TYPE, state.getNullable(SlabBlock.TYPE)).withIfExists(SlabBlock.WATERLOGGED, state.getNullable(SlabBlock.WATERLOGGED)));
            } else if (state.getBlock() == Blocks.MOSSY_COBBLESTONE_STAIRS) {
                world.setBlockState(pos, Blocks.COBBLESTONE_STAIRS.getDefaultState().withIfExists(StairsBlock.FACING, state.getNullable(StairsBlock.FACING)).withIfExists(StairsBlock.HALF, state.getNullable(StairsBlock.HALF)).withIfExists(StairsBlock.SHAPE, state.getNullable(StairsBlock.SHAPE)).withIfExists(StairsBlock.WATERLOGGED, state.getNullable(StairsBlock.WATERLOGGED)));
            } else if (state.getBlock() == Blocks.MOSSY_COBBLESTONE_WALL) {
                world.setBlockState(pos, Blocks.COBBLESTONE_WALL.getDefaultState().withIfExists(WallBlock.UP, state.getNullable(WallBlock.UP)).withIfExists(WallBlock.NORTH_SHAPE, state.getNullable(WallBlock.NORTH_SHAPE)).withIfExists(WallBlock.EAST_SHAPE, state.getNullable(WallBlock.EAST_SHAPE)).withIfExists(WallBlock.SOUTH_SHAPE, state.getNullable(WallBlock.SOUTH_SHAPE)).withIfExists(WallBlock.WEST_SHAPE, state.getNullable(WallBlock.WEST_SHAPE)).withIfExists(WallBlock.WATERLOGGED, state.getNullable(WallBlock.WATERLOGGED)));
            } else if (state.getBlock() == Blocks.MOSSY_STONE_BRICKS) {
                world.setBlockState(pos, Blocks.STONE_BRICKS.getDefaultState());
            } else if (state.getBlock() == Blocks.MOSSY_STONE_BRICK_SLAB) {
                world.setBlockState(pos, Blocks.STONE_BRICK_SLAB.getDefaultState().withIfExists(SlabBlock.TYPE, state.getNullable(SlabBlock.TYPE)).withIfExists(SlabBlock.WATERLOGGED, state.getNullable(SlabBlock.WATERLOGGED)));
            } else if (state.getBlock() == Blocks.MOSSY_STONE_BRICK_STAIRS) {
                world.setBlockState(pos, Blocks.STONE_BRICK_STAIRS.getDefaultState().withIfExists(StairsBlock.FACING, state.getNullable(StairsBlock.FACING)).withIfExists(StairsBlock.HALF, state.getNullable(StairsBlock.HALF)).withIfExists(StairsBlock.SHAPE, state.getNullable(StairsBlock.SHAPE)).withIfExists(StairsBlock.WATERLOGGED, state.getNullable(StairsBlock.WATERLOGGED)));
            } else if (state.getBlock() == Blocks.MOSSY_STONE_BRICK_WALL) {
                world.setBlockState(pos, Blocks.STONE_BRICK_WALL.getDefaultState().withIfExists(WallBlock.UP, state.getNullable(WallBlock.UP)).withIfExists(WallBlock.NORTH_SHAPE, state.getNullable(WallBlock.NORTH_SHAPE)).withIfExists(WallBlock.EAST_SHAPE, state.getNullable(WallBlock.EAST_SHAPE)).withIfExists(WallBlock.SOUTH_SHAPE, state.getNullable(WallBlock.SOUTH_SHAPE)).withIfExists(WallBlock.WEST_SHAPE, state.getNullable(WallBlock.WEST_SHAPE)).withIfExists(WallBlock.WATERLOGGED, state.getNullable(WallBlock.WATERLOGGED)));
            } else if (state.getBlock() instanceof InfestedBlock infestedBlock) {
                world.setBlockState(pos, infestedBlock.getRegularBlock().getDefaultState());
            } else if (state.getBlock() == Blocks.DIRT_PATH) {
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            } else if (state.getBlock() == Blocks.FARMLAND) {
                FarmlandBlock.setToDirt(null, state, world, pos);
            } else if (state.getBlock() instanceof PumpkinBlock || state.getBlock() == Blocks.MELON || state.getBlock() instanceof CarvedPumpkinBlock || state.getBlock() instanceof PlantBlock || state.getBlock() instanceof HayBlock || state.getBlock() == Blocks.MUSHROOM_STEM || state.getBlock() == Blocks.BROWN_MUSHROOM_BLOCK || state.getBlock() == Blocks.RED_MUSHROOM_BLOCK || state.getBlock() == Blocks.SNOW_BLOCK || state.getBlock() == Blocks.POWDER_SNOW) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            } else if (state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.RED_SAND) {
                world.setBlockState(pos, SolarApocalypse.INSTANCE.getDUST().getDefaultState());
            }
        }
        if (state.getBlock() == Blocks.COARSE_DIRT && SolarApocalypse.INSTANCE.isPhaseReady(SolarApocalypse.INSTANCE.getConfig().getCoarseDirtTurnsToSandPhase(), world)) world.setBlockState(pos, Blocks.SAND.getDefaultState());
        if (SolarApocalypse.INSTANCE.isPhaseReady(SolarApocalypse.INSTANCE.getConfig().getCobbledAndCrackedStonesTurnsToLavaPhase(), world) && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock() == Blocks.COBBLESTONE_SLAB || state.getBlock() == Blocks.COBBLESTONE_STAIRS || state.getBlock() == Blocks.COBBLESTONE_WALL || state.getBlock() == Blocks.COBBLED_DEEPSLATE || state.getBlock() == Blocks.COBBLED_DEEPSLATE_SLAB || state.getBlock() == Blocks.COBBLED_DEEPSLATE_STAIRS || state.getBlock() == Blocks.COBBLED_DEEPSLATE_WALL || state.getBlock() == Blocks.CRACKED_DEEPSLATE_TILES || state.getBlock() == Blocks.CRACKED_DEEPSLATE_BRICKS || state.getBlock() == Blocks.CRACKED_STONE_BRICKS)) world.setBlockState(pos, Blocks.LAVA.getDefaultState());
    }
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    private boolean hookHasRandomTicks(boolean original, BlockState state) { return original || state.getBlock() == Blocks.CLAY || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.DIRT_PATH || state.getBlock() == Blocks.COARSE_DIRT || state.getBlock() instanceof PumpkinBlock || state.getBlock() == Blocks.MELON || state.getBlock() instanceof CarvedPumpkinBlock || state.getBlock() instanceof PlantBlock || state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.RED_SAND || state.getBlock() == Blocks.MUSHROOM_STEM || state.getBlock() == Blocks.BROWN_MUSHROOM_BLOCK || state.getBlock() == Blocks.RED_MUSHROOM_BLOCK || state.getBlock() == Blocks.SNOW_BLOCK || state.getBlock() == Blocks.POWDER_SNOW || state.getBlock() == Blocks.PACKED_ICE || state.getBlock() == Blocks.BLUE_ICE || state.getBlock() == Blocks.STONE_BRICKS || state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.STONE_SLAB || state.getBlock() == Blocks.STONE_STAIRS || state.getBlock() == Blocks.DEEPSLATE || state.getBlock() == Blocks.DEEPSLATE_BRICKS || state.getBlock() == Blocks.DEEPSLATE_TILES || state.getBlock() == Blocks.MOSSY_COBBLESTONE || state.getBlock() == Blocks.MOSSY_COBBLESTONE_SLAB || state.getBlock() == Blocks.MOSSY_COBBLESTONE_STAIRS || state.getBlock() == Blocks.MOSSY_COBBLESTONE_WALL || state.getBlock() == Blocks.MOSSY_STONE_BRICKS || state.getBlock() == Blocks.MOSSY_STONE_BRICK_SLAB || state.getBlock() == Blocks.MOSSY_STONE_BRICK_STAIRS || state.getBlock() == Blocks.MOSSY_STONE_BRICK_WALL || state.getBlock() == Blocks.COBBLESTONE || state.getBlock() == Blocks.COBBLESTONE_SLAB || state.getBlock() == Blocks.COBBLESTONE_STAIRS || state.getBlock() == Blocks.COBBLESTONE_WALL || state.getBlock() == Blocks.COBBLED_DEEPSLATE || state.getBlock() == Blocks.COBBLED_DEEPSLATE_SLAB || state.getBlock() == Blocks.COBBLED_DEEPSLATE_STAIRS || state.getBlock() == Blocks.CRACKED_DEEPSLATE_TILES || state.getBlock() == Blocks.COBBLED_DEEPSLATE_WALL || state.getBlock() == Blocks.CRACKED_DEEPSLATE_BRICKS || state.getBlock() == Blocks.CRACKED_STONE_BRICKS || state.getBlock() instanceof HayBlock || state.getBlock() instanceof InfestedBlock; }
}