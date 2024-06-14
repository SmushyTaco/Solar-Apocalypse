package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(WaterFluid.class)
public abstract class WaterIsFiniteAndEvaporatesInDaylight {
    @ModifyReturnValue(method = "isInfinite", at = @At("RETURN"))
    private boolean hookIsInfinite(boolean original, World world) { return false; }
}