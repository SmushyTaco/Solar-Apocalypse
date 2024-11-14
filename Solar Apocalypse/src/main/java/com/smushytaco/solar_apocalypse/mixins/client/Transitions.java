package com.smushytaco.solar_apocalypse.mixins.client;
import com.mojang.authlib.GameProfile;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import com.smushytaco.solar_apocalypse.mixin_logic.TransitionsLogic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class Transitions extends AbstractClientPlayerEntity {
    protected Transitions(ClientWorld world, GameProfile profile) { super(world, profile); }
    @Inject(method = "tick", at = @At("RETURN"))
    private void hookTick(CallbackInfo ci) {
        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity) (Object) this;
        SolarApocalypseClient.INSTANCE.setOverlayOpacity(TransitionsLogic.INSTANCE.hookTick(clientPlayerEntity, SolarApocalypseClient.INSTANCE.getOverlayOpacity(), SolarApocalypse.INSTANCE.getConfig().getHeatOverlayFadeTime(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()));
        SolarApocalypseClient.INSTANCE.setFogFade(TransitionsLogic.INSTANCE.hookTick(clientPlayerEntity, SolarApocalypseClient.INSTANCE.getFogFade(), SolarApocalypse.INSTANCE.getConfig().getApocalypseFadeTime(), SolarApocalypse.INSTANCE.getConfig().getApocalypseFogDay()));
        if (SolarApocalypseClient.INSTANCE.getSunTransition() != 1.0F) SolarApocalypseClient.INSTANCE.setSunTransition(TransitionsLogic.INSTANCE.hookTick(SolarApocalypseClient.INSTANCE.getSunTransition(), SolarApocalypse.INSTANCE.getConfig().getSunSizeTransitionTime()));
    }
}