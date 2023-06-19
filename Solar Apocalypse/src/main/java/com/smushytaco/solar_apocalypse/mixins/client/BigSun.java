package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class BigSun {
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private VertexConsumer bigSunGenerator(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) {
        if (client.world == null) return original.call(instance, matrix, x, y, z);
        if (WorldDayCalculation.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay())) {
            original.call(instance, matrix, x * Math.abs(SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightSunSizeMultiplier()), y, z * Math.abs(SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightSunSizeMultiplier()));
        } else if (WorldDayCalculation.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay())) {
            original.call(instance, matrix, x * Math.abs(SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightSunSizeMultiplier()), y, z * Math.abs(SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightSunSizeMultiplier()));
        } else if (WorldDayCalculation.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightDay())) {
            original.call(instance, matrix, x * Math.abs(SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightSunSizeMultiplier()), y, z * Math.abs(SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightSunSizeMultiplier()));
        }
        return original.call(instance, matrix, x, y, z);
    }
    @WrapOperation(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 2))
    @SuppressWarnings("unused")
    private VertexConsumer hookRenderSkyVertexOne(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return bigSunGenerator(instance, matrix, x, y, z, original); }
    @WrapOperation(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 3))
    @SuppressWarnings("unused")
    private VertexConsumer hookRenderSkyVertexTwo(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return bigSunGenerator(instance, matrix, x, y, z, original); }
    @WrapOperation(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 4))
    @SuppressWarnings("unused")
    private VertexConsumer hookRenderSkyVertexThree(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return bigSunGenerator(instance, matrix, x, y, z, original); }
    @WrapOperation(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 5))
    @SuppressWarnings("unused")
    private VertexConsumer hookRenderSkyVertexFour(BufferBuilder instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return bigSunGenerator(instance, matrix, x, y, z, original); }
}