package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(WaterFluid.class)
public abstract class WaterIsFiniteAndEvaporatesInDaylight extends Fluid {
    @ModifyReturnValue(method = "isInfinite", at = @At("RETURN"))
    @SuppressWarnings("unused")
    private boolean hookIsInfinite(boolean original, World world) { return false; }
}