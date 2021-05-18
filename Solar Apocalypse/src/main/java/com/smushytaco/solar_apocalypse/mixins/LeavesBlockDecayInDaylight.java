package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Random;
@Mixin(LeavesBlock.class)
public abstract class LeavesBlockDecayInDaylight {
    @Inject(method = "hasRandomTicks", at = @At("HEAD"), cancellable = true)
    private void hookHasRandomTicks(BlockState state, CallbackInfoReturnable<Boolean> cir) { cir.setReturnValue(true); }
    @Inject(method = "randomTick", at = @At("HEAD"))
    private void hookRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockPos blockPos = pos.offset(Direction.UP);
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay()) || world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos) || !state.getMaterial().isBurnable()) return;
        world.removeBlock(pos, false);
    }
}