package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.render.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(CloudRenderer.class)
public abstract class ApocalypseClouds {
    @WrapOperation(method = "renderClouds", at = @At(value = "INVOKE", target="Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0))
    private void hookRenderClouds(float red, float green, float blue, float alpha, Operation<Void> original) {
        original.call(red, green, blue, alpha * (1.0F - SolarApocalypseClient.INSTANCE.getCloudFade()));
    }
}