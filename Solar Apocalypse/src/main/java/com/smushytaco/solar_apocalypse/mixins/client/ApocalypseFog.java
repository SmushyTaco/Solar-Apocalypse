package com.smushytaco.solar_apocalypse.mixins.client;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(FogRenderer.class)
public abstract class ApocalypseFog {
    @Inject(method = "updateBuffer(Lnet/minecraft/client/renderer/fog/FogData;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getDevice()Lcom/mojang/blaze3d/systems/GpuDevice;"))
    private void hookApplyFog(FogData fog, CallbackInfo ci) {
        fog.environmentalStart = fog.environmentalStart + SolarApocalypseClient.INSTANCE.getFogFade() * ((fog.renderDistanceEnd * 0.05F) - fog.environmentalStart);
        fog.environmentalEnd = fog.environmentalEnd + SolarApocalypseClient.INSTANCE.getFogFade() * ((Math.min(fog.renderDistanceEnd, SolarApocalypse.INSTANCE.getConfig().getApocalypseFogMaximumDistance()) * SolarApocalypse.INSTANCE.getConfig().getApocalypseFogMultiplier()) - fog.environmentalEnd);
    }
}