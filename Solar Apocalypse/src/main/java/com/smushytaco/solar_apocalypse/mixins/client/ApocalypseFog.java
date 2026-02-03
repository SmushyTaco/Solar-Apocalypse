package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.sugar.Local;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(FogRenderer.class)
public abstract class ApocalypseFog {
    @Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getDevice()Lcom/mojang/blaze3d/systems/GpuDevice;", remap = false))
    private void hookApplyFog(Camera camera, int i, DeltaTracker deltaTracker, float f, ClientLevel clientLevel, CallbackInfoReturnable<Vector4f> cir, @Local FogData data) {
        data.environmentalStart = data.environmentalStart + SolarApocalypseClient.INSTANCE.getFogFade() * ((data.renderDistanceEnd * 0.05F) - data.environmentalStart);
        data.environmentalEnd = data.environmentalEnd + SolarApocalypseClient.INSTANCE.getFogFade() * ((Math.min(data.renderDistanceEnd, SolarApocalypse.INSTANCE.getConfig().getApocalypseFogMaximumDistance()) * SolarApocalypse.INSTANCE.getConfig().getApocalypseFogMultiplier()) - data.environmentalEnd);
    }
}