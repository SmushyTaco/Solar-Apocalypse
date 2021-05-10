package com.smushytaco.solar_apocalypse.mixins;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
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
public abstract class BurnableBlocksBurnInDaylight {
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
        double worldAge = world.getTimeOfDay() / 24000.0D;
        if (worldAge < 5.0D || world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos) || !state.getMaterial().isBurnable() || !world.getBlockState(blockPos).isAir()) return;
        BlockState blockState = AbstractFireBlock.getState(world, blockPos);
        world.setBlockState(blockPos, blockState, 11);
    }
}