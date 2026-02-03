package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(AtmosphericFogEnvironment.class)
public abstract class FogColor {
    @ModifyExpressionValue(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;F)Ljava/lang/Object;", ordinal = 0))
    private Object hookGetBaseColor(Object original) {
        SolarApocalypseClient.INSTANCE.updateFogColor((int) original, null);
        return SolarApocalypseClient.INSTANCE.getHasInitialized() ? SolarApocalypseClient.INSTANCE.getFogColor() : original;
    }
}