package com.smushytaco.solar_apocalypse.mixins.client;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public abstract class ColoredSkyLight {
    @Inject(method = "update", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/LightmapTextureManager;flickerIntensity:F"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void doOurLightMap(float partialTicks, CallbackInfo ci, ClientWorld $$1, float $$2, float $$4, float $$5, float $$6, float $$7, float $$11, float $$8, Vector3f skyVector) {
        if (SolarApocalypse.INSTANCE.getConfig().getEnableCustomSkyLight()) {
            int skylight = SolarApocalypseClient.INSTANCE.getSkyColor();
            ClientWorld clientWorld = MinecraftClient.getInstance().world;
            if (skylight == SolarApocalypseClient.INSTANCE.getOriginalSkyColor() || clientWorld == null) {
                ci.cancel();
            }
            skyVector.lerp(new Vector3f(SolarApocalypseClient.INSTANCE.getRedToFloat(skylight), SolarApocalypseClient.INSTANCE.getGreenToFloat(skylight), SolarApocalypseClient.INSTANCE.getBlueToFloat(skylight)), MathHelper.clamp(clientWorld.getSkyBrightness(1.0F), 0.0F, 1.0F));
        }
    }
}