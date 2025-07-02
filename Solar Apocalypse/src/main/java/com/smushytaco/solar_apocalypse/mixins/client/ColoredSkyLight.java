package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.smushytaco.solar_apocalypse.mixin_logic.ColoredSkyLightLogic;
import net.minecraft.client.render.LightmapTextureManager;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LightmapTextureManager.class)
public abstract class ColoredSkyLight {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/buffers/Std140Builder;putVec3(Lorg/joml/Vector3fc;)Lcom/mojang/blaze3d/buffers/Std140Builder;", remap = false))
    private Std140Builder hookUpdate(Std140Builder instance, Vector3fc vec, Operation<Std140Builder> original) { return ColoredSkyLightLogic.INSTANCE.hookUpdate(instance, vec, original); }
}