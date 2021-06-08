package com.smushytaco.solar_apocalypse.mixins;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Block.class)
public abstract class CertainBlocksHaveRandomTick {
    @Inject(method = "hasRandomTicks", at = @At("HEAD"), cancellable = true)
    private void hookHasRandomTicks(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == Blocks.CLAY || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.DIRT_PATH || state.getBlock() == Blocks.COARSE_DIRT || state.getBlock() instanceof GourdBlock || state.getBlock() instanceof CarvedPumpkinBlock) cir.setReturnValue(true);
    }
}