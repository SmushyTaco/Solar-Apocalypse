package com.smushytaco.solar_apocalypse.mixins;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(AbstractBlock.class)
public interface AbstractBlockAccessor {
    @Mutable
    @Final
    @Accessor
    void setRandomTicks(boolean randomTicks);
}