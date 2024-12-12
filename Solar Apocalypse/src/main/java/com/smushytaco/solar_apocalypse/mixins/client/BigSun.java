package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.mixin_logic.BigSunLogic;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(SkyRendering.class)
public abstract class BigSun {
    @WrapOperation(method = "renderSun", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;vertex(Lorg/joml/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;"))
    private VertexConsumer hookRenderSun(VertexConsumer instance, Matrix4f matrix, float x, float y, float z, Operation<VertexConsumer> original) { return BigSunLogic.INSTANCE.bigSunGenerator(instance, matrix, x, y, z, original); }
}