package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.mixin_logic.BigSunLogic;
import net.minecraft.client.render.SkyRendering;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(SkyRendering.class)
public abstract class BigSun {
    @WrapOperation(method = "renderSun", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4fStack;scale(FFF)Lorg/joml/Matrix4f;", remap = false))
    private Matrix4f hookRenderSun(Matrix4fStack instance, float x, float y, float z, Operation<Matrix4f> original) { return BigSunLogic.INSTANCE.bigSunGenerator(instance, x, y, z, original); }
}