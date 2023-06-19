package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(Block.class)
public abstract class CertainBlocksHaveRandomTick {
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    @SuppressWarnings("unused")
    private boolean hookHasRandomTicks(boolean original, BlockState state) { return state.getBlock() == Blocks.CLAY || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.DIRT_PATH || state.getBlock() == Blocks.COARSE_DIRT || state.getBlock() instanceof GourdBlock || state.getBlock() instanceof CarvedPumpkinBlock || state.getBlock() instanceof PlantBlock || state.getBlock() instanceof SandBlock || state.getBlock() instanceof HayBlock || original; }
}