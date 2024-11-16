package com.smushytaco.solar_apocalypse.mixins.client;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(InGameHud.class)
public abstract class HeatOverlay {
    @Shadow
    protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);
    @Shadow
    @Final
    private MinecraftClient client;
    @Inject(method = "renderMiscOverlays", at = @At("RETURN"))
    private void hookRenderMiscOverlays(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!SolarApocalypse.INSTANCE.getConfig().getEnableHeatOverlay() || SolarApocalypseClient.INSTANCE.getOverlayOpacity() == 0.0F) return;
        renderOverlay(context, SolarApocalypse.INSTANCE.getHEAT_OVERLAY(), SolarApocalypseClient.INSTANCE.getOverlayOpacity());
    }
}