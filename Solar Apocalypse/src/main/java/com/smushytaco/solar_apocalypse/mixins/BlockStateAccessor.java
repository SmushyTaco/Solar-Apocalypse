package com.smushytaco.solar_apocalypse.mixins;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(AbstractBlock.AbstractBlockState.class)
public interface BlockStateAccessor {
    @Mutable
    @Final
    @Accessor
    void setBurnable(boolean burnable);
}