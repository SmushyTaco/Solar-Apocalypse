package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.mixin_logic.ColoredSkyLightLogic;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.LightmapTextureManager;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LightmapTextureManager.class)
public abstract class ColoredSkyLight {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Uniform;set(Lorg/joml/Vector3f;)V"))
    private void hookUpdate(Uniform instance, Vector3f vector, Operation<Void> original) { ColoredSkyLightLogic.INSTANCE.hookUpdate(instance, vector, original); }
}