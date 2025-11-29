package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(CloudRenderer.class)
public abstract class ApocalypseClouds {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target= "Lcom/mojang/blaze3d/buffers/Std140Builder;putVec4(FFFF)Lcom/mojang/blaze3d/buffers/Std140Builder;", ordinal = 0))
    private Std140Builder hookRenderClouds(Std140Builder instance, float x, float y, float z, float w, Operation<Std140Builder> original) {
        return original.call(instance, x, y, z, w * (1.0F - SolarApocalypseClient.INSTANCE.getCloudFade()));
    }
}