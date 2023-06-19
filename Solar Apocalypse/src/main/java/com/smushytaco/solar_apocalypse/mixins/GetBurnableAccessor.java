package com.smushytaco.solar_apocalypse.mixins;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(AbstractBlock.Settings.class)
public interface GetBurnableAccessor {
    @Accessor
    boolean getBurnable();
}