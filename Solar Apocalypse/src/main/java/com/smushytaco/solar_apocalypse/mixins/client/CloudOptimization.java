package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LevelRenderer.class)
public abstract class CloudOptimization {
    @WrapOperation(method = "renderLevel", at = @At(value = "INVOKE", target= "Lnet/minecraft/client/Options;getCloudsType()Lnet/minecraft/client/CloudStatus;"))
    private CloudStatus hookRender(Options instance, Operation<CloudStatus> original) {
        if (SolarApocalypseClient.INSTANCE.getCloudFade() != 1.0F) return original.call(instance);
        return CloudStatus.OFF;
    }
}