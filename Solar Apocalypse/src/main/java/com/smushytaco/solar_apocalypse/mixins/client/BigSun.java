package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.mixin_logic.BigSunLogic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(SkyRendering.class)
public abstract class BigSun {
    @WrapOperation(method = "renderSun", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 0))
    private VertexConsumer hookRenderSkyVertexOne(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return BigSunLogic.INSTANCE.bigSunGenerator(instance, matrix, x, y, z, original); }
    @WrapOperation(method = "renderSun", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 1))
    private VertexConsumer hookRenderSkyVertexTwo(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return BigSunLogic.INSTANCE.bigSunGenerator(instance, matrix, x, y, z, original); }
    @WrapOperation(method = "renderSun", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 2))
    private VertexConsumer hookRenderSkyVertexThree(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return BigSunLogic.INSTANCE.bigSunGenerator(instance, matrix, x, y, z, original); }
    @WrapOperation(method = "renderSun", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 3))
    private VertexConsumer hookRenderSkyVertexFour(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return BigSunLogic.INSTANCE.bigSunGenerator(instance, matrix, x, y, z, original); }
}