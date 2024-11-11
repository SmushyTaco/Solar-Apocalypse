package com.smushytaco.solar_apocalypse.mixins.client;
import com.mojang.authlib.GameProfile;
import com.smushytaco.solar_apocalypse.client.HeatOverlayFade;
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
public class HeatOverlayFadeInAndOut extends AbstractClientPlayerEntity implements HeatOverlayFade {
    protected HeatOverlayFadeInAndOut(ClientWorld world, GameProfile profile) { super(world, profile); }
    @Unique
    float overlayOpacity = 0.0F;
    @Override
    public float getOverlayOpacity() { return overlayOpacity; }
    @Inject(method = "tick", at = @At("RETURN"))
    private void hookTick(CallbackInfo ci) { overlayOpacity = HeatOverlayFadeInAndOutLogic.INSTANCE.hookTick((ClientPlayerEntity) (Object) this, overlayOpacity); }
}