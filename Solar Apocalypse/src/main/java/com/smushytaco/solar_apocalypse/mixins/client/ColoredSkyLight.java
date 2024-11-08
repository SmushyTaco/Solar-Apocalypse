package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.client.Render;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public abstract class ColoredSkyLight {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;lerp(Lorg/joml/Vector3fc;F)Lorg/joml/Vector3f;"))
    private Vector3f modifySkyLightColor(Vector3f instance, Vector3fc other, float t, Operation<Vector3f> original) {
        if (!SolarApocalypse.INSTANCE.getConfig().getEnableCustomSkyLight()) return original.call(instance, other, t);
        int skylight = Render.INSTANCE.getSkyColor(Integer.MAX_VALUE);
        ClientWorld clientWorld = MinecraftClient.getInstance().world;
        if (skylight == Integer.MAX_VALUE || clientWorld == null) return original.call(instance, other, t);
        return original.call(instance, other, t).lerp(new Vector3f(SolarApocalypse.INSTANCE.getRedToFloat(skylight), SolarApocalypse.INSTANCE.getGreenToFloat(skylight), SolarApocalypse.INSTANCE.getBlueToFloat(skylight)), MathHelper.clamp(clientWorld.getSkyBrightness(1.0F), 0.0F, 1.0F));
    }
}