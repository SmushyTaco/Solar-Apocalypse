package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(ServerWorld.class)
public abstract class LightningPhases {
    @WrapOperation(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I", ordinal = 0))
    private int hookTickChunk(Random instance, int i, Operation<Integer> original) {
        if (!SolarApocalypse.INSTANCE.getConfig().getEnableLightningPhases()) return original.call(instance, i);
        double multiplier = SolarApocalypse.INSTANCE.getLightningMultiplier((ServerWorld) (Object) this);
        if (multiplier == 1.0) return original.call(instance, i);
        return original.call(instance, MathHelper.clamp(MathHelper.floor(i / multiplier), 1, Integer.MAX_VALUE));
    }
}