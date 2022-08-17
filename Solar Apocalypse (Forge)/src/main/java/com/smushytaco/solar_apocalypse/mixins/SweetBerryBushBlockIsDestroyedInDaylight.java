package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Random;
@Mixin(value = SweetBerryBushBlock.class)
public abstract class SweetBerryBushBlockIsDestroyedInDaylight {
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void hookHasRandomTick(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void hookRandomTick(BlockState state, ServerLevel world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockPos blockPos = pos.relative(Direction.UP);
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay()) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        ci.cancel();
    }
}