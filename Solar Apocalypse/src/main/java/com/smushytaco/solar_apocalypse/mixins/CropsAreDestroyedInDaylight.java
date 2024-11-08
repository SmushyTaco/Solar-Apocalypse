package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(CropBlock.class)
public abstract class CropsAreDestroyedInDaylight {
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    private boolean hookHasRandomTick(boolean original, BlockState state) { return true; }
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void hookRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) { SolarApocalypse.INSTANCE.blockDestruction(world, pos, ci); }
    @WrapWithCondition(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean hookSetBlockState(ServerWorld instance, BlockPos blockPos, BlockState blockState, int i, BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!SolarApocalypse.INSTANCE.isPhaseReady(SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownPhase(), world) || world.isNight() || world.isRaining() || world.isSkyVisible(blockPos) || SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownMultiplier() < 2) return true;
        return random.nextInt(SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownMultiplier()) == 0;
    }
}