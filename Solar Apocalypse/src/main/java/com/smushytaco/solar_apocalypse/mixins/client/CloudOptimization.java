package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class CloudOptimization {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target="Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;"))
    private CloudRenderMode hookRender(GameOptions instance, Operation<CloudRenderMode> original) {
        if (SolarApocalypseClient.INSTANCE.getCloudFade() != 1.0F) return original.call(instance);
        return CloudRenderMode.OFF;
    }
}