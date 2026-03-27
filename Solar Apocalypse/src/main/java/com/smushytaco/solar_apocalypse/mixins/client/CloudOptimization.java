package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LevelRenderer.class)
public abstract class CloudOptimization {
    @Definition(id = "optionsRenderState", field = "Lnet/minecraft/client/renderer/LevelRenderer;optionsRenderState:Lnet/minecraft/client/renderer/state/OptionsRenderState;")
    @Definition(id = "cloudStatus", field = "Lnet/minecraft/client/renderer/state/OptionsRenderState;cloudStatus:Lnet/minecraft/client/CloudStatus;")
    @Expression("this.optionsRenderState.cloudStatus")
    @ModifyExpressionValue(method = "renderLevel", at = @At("MIXINEXTRAS:EXPRESSION"))
    private CloudStatus hookRender(CloudStatus original) {
        if (SolarApocalypseClient.INSTANCE.getCloudFade() != 1.0F) return original;
        return CloudStatus.OFF;
    }
}