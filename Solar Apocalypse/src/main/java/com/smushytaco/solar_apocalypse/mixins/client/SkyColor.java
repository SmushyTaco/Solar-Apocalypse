package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(SkyRenderer.class)
public abstract class SkyColor {
    @ModifyExpressionValue(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;F)Ljava/lang/Object;", ordinal = 6))
    public Object hookExtractRenderState(Object original) {
        SolarApocalypseClient.INSTANCE.updateSkyColor((int) original, null);
        return SolarApocalypseClient.INSTANCE.getHasInitialized() ? SolarApocalypseClient.INSTANCE.getSkyColor() : original;
    }
}