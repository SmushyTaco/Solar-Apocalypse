package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(BambooBlock.class)
public abstract class BambooBlockIsDestroyedInDaylight {
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    private boolean hookHasRandomTick(boolean original, BlockState state) { return true; }
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void hookRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) { SolarApocalypse.INSTANCE.blockDestruction(world, pos, ci); }
}