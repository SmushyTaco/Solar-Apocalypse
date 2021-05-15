package com.smushytaco.solar_apocalypse.mixins;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Block.class)
public abstract class ClayHasRandomTick {
    @Inject(method = "hasRandomTicks", at = @At("HEAD"), cancellable = true)
    private void hookHasRandomTicks(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == Blocks.CLAY) cir.setReturnValue(true);
    }
}