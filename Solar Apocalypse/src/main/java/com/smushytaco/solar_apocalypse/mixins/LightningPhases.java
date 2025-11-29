package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(ServerLevel.class)
public abstract class LightningPhases {
    @WrapOperation(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 0))
    private int hookTickChunk(RandomSource instance, int i, Operation<Integer> original) {
        if (!SolarApocalypse.INSTANCE.getConfig().getEnableLightningPhases()) return original.call(instance, i);
        double multiplier = SolarApocalypse.INSTANCE.getLightningMultiplier((ServerLevel) (Object) this);
        if (multiplier == 1.0) return original.call(instance, i);
        return original.call(instance, Mth.clamp(Mth.floor(i / multiplier), 1, Integer.MAX_VALUE));
    }
}