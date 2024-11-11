package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(WaterFluid.class)
public abstract class WaterIsFinite {
    @ModifyReturnValue(method = "isInfinite", at = @At("RETURN"))
    private boolean hookIsInfinite(boolean original, ServerWorld world) { return !WorldDayCalculation.INSTANCE.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getPhaseOneDay()) && original; }
}