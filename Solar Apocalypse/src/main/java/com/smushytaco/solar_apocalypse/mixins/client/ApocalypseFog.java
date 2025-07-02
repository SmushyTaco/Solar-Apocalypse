package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.sugar.Local;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(FogRenderer.class)
public abstract class ApocalypseFog {
    @Inject(method = "applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getDevice()Lcom/mojang/blaze3d/systems/GpuDevice;", remap = false))
    private void hookApplyFog(Camera camera, int viewDistance, boolean thick, RenderTickCounter tickCounter, float skyDarkness, ClientWorld world, CallbackInfoReturnable<Vector4f> cir, @Local FogData data) {
        data.environmentalStart = data.environmentalStart + SolarApocalypseClient.INSTANCE.getFogFade() * ((data.renderDistanceEnd * 0.05F) - data.environmentalStart);
        data.environmentalEnd = data.environmentalEnd + SolarApocalypseClient.INSTANCE.getFogFade() * ((Math.min(data.renderDistanceEnd, SolarApocalypse.INSTANCE.getConfig().getApocalypseFogMaximumDistance()) * SolarApocalypse.INSTANCE.getConfig().getApocalypseFogMultiplier()) - data.environmentalEnd);
    }
}