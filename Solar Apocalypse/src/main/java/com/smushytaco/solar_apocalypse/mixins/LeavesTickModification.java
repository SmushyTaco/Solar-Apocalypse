package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LeavesBlock.class)
public abstract class LeavesTickModification extends Block {
    protected LeavesTickModification(Settings settings) { super(settings); }
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    private boolean hookHasRandomTicks(boolean original, BlockState state) { return original || super.hasRandomTicks(state); }
}