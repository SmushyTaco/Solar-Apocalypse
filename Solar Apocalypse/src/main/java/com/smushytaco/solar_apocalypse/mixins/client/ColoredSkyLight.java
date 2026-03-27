package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.mixin_logic.ColoredSkyLightLogic;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LightmapRenderStateExtractor.class)
public abstract class ColoredSkyLight {
    @WrapOperation(method = "extract", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
    private void hookExtract(ProfilerFiller instance, Operation<Void> original, LightmapRenderState renderState, float partialTicks) { ColoredSkyLightLogic.INSTANCE.hookExtract(renderState); }
}