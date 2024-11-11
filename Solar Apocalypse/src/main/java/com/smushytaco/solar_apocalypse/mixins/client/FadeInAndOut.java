package com.smushytaco.solar_apocalypse.mixins.client;
import com.mojang.authlib.GameProfile;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.client.Fade;
import com.smushytaco.solar_apocalypse.mixin_logic.HeatOverlayFadeInAndOutLogic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class FadeInAndOut extends AbstractClientPlayerEntity implements Fade {
    protected FadeInAndOut(ClientWorld world, GameProfile profile) { super(world, profile); }
    @Unique
    float overlayOpacity = 0.0F;
    @Unique
    float fogFade = 0.0F;
    @Override
    public float getOverlayOpacity() { return overlayOpacity; }
    @Override
    public float getFogFade() { return fogFade; }
    @Inject(method = "tick", at = @At("RETURN"))
    private void hookTick(CallbackInfo ci) {
        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity) (Object) this;
        overlayOpacity = HeatOverlayFadeInAndOutLogic.INSTANCE.hookTick(clientPlayerEntity, overlayOpacity, SolarApocalypse.INSTANCE.getConfig().getHeatOverlayFadeTime(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay());
        fogFade = HeatOverlayFadeInAndOutLogic.INSTANCE.hookTick(clientPlayerEntity, fogFade, SolarApocalypse.INSTANCE.getConfig().getApocalypseFadeTime(), SolarApocalypse.INSTANCE.getConfig().getApocalypseFogDay());
    }
}