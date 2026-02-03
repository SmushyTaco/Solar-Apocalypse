package com.smushytaco.solar_apocalypse.mixins.client;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Gui.class)
public abstract class HeatOverlay {
    @Shadow
    protected abstract void renderTextureOverlay(GuiGraphics context, Identifier texture, float opacity);
    @Inject(method = "renderCameraOverlays", at = @At("RETURN"))
    private void hookRenderMiscOverlays(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        if (!SolarApocalypse.INSTANCE.getConfig().getEnableHeatOverlay() || SolarApocalypseClient.INSTANCE.getOverlayOpacity() == 0.0F) return;
        renderTextureOverlay(context, SolarApocalypse.INSTANCE.getHEAT_OVERLAY(), SolarApocalypseClient.INSTANCE.getOverlayOpacity());
    }
}