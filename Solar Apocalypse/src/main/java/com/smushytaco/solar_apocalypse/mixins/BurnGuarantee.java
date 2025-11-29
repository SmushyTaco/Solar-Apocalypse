package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.BlockCache;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BurnGuarantee {
    @Shadow
    public abstract Block getBlock();
    @ModifyReturnValue(method = "ignitedByLava", at = @At("RETURN"))
    private boolean hookIsBurnable(boolean original) { return original || getBlock() instanceof BlockCache blockCache && blockCache.getCacheShouldBurn(); }
}