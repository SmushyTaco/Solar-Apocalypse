package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.BlockCache;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class TickAndBurnGuarantee {
    @Shadow
    public abstract Block getBlock();
    @ModifyReturnValue(method = "isBurnable", at = @At("RETURN"))
    private boolean hookIsBurnable(boolean original) { return original || getBlock() instanceof BlockCache blockCache && blockCache.getCacheShouldBurn(); }
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    private boolean hookHasRandomTicks(boolean original) { return original || getBlock() instanceof BlockCache blockCache && blockCache.getCacheShouldRandomTick(); }
}