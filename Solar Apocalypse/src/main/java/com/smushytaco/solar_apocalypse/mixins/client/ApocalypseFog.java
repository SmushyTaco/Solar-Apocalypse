package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.client.Fade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public abstract class ApocalypseFog {
    @ModifyExpressionValue(method = "applyFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer$FogData;fogEnd:F", ordinal = 15))
    private static float hookApplyFogStart(float original, Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta) {
        if (!(MinecraftClient.getInstance().player instanceof Fade fade)) return original;
        return original + fade.getFogFade() * ((Math.min(viewDistance, SolarApocalypse.INSTANCE.getConfig().getApocalypseFogMaximumDistance()) * SolarApocalypse.INSTANCE.getConfig().getApocalypseFogMultiplier()) - original);
    }
    @ModifyExpressionValue(method = "applyFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer$FogData;fogStart:F", ordinal = 9))
    private static float hookApplyFogEnd(float original, Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta) {
        if (!(MinecraftClient.getInstance().player instanceof Fade fade)) return original;
        return original + fade.getFogFade() * ((viewDistance * 0.05F) - original);
    }
}