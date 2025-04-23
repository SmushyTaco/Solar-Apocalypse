package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderPass;
import com.smushytaco.solar_apocalypse.mixin_logic.ColoredSkyLightLogic;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LightmapTextureManager.class)
public abstract class ColoredSkyLight {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setUniform(Ljava/lang/String;[F)V", ordinal = 3))
    private void hookUpdate(RenderPass instance, String name, float[] floats, Operation<Void> original) { ColoredSkyLightLogic.INSTANCE.hookUpdate(instance, name, floats, original); }
}