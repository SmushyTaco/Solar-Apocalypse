package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class ApocalypseClouds {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target="Lnet/minecraft/client/option/GameOptions;getCloudRenderModeValue()Lnet/minecraft/client/option/CloudRenderMode;"))
    private CloudRenderMode hookApplyFogMultiplier(GameOptions instance, Operation<CloudRenderMode> original) {
        ClientWorld clientWorld = MinecraftClient.getInstance().world;
        if (clientWorld == null || clientWorld.isNight() || clientWorld.isRaining() || !WorldDayCalculation.INSTANCE.isOldEnough(clientWorld, SolarApocalypse.INSTANCE.getConfig().getNoCloudsDay())) return original.call(instance);
        return CloudRenderMode.OFF;
    }
}