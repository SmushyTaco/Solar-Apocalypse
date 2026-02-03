package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.renderer.CloudRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(CloudRenderer.class)
public abstract class ApocalypseClouds {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target= "Lnet/minecraft/util/ARGB;vector4fFromARGB32(I)Lorg/joml/Vector4f;"))
    private Vector4f hookRenderClouds(int i, Operation<Vector4f> original) {
        Vector4f vector4f = original.call(i);
        vector4f.w *= 1.0F - SolarApocalypseClient.INSTANCE.getCloudFade();
        return vector4f;
    }
}