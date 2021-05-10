package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(PlayerEntity.class)
public abstract class PlayersBurnDuringTheDaylight extends LivingEntity {
    protected PlayersBurnDuringTheDaylight(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Shadow
    public abstract boolean isCreative();
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void hookTickMovement(CallbackInfo ci) {
        double worldAge = world.getTimeOfDay() / 24000.0D;
        if (worldAge < 7.0D || !isAlive() || isOnFire() || world.isRaining() || isSpectator() || isCreative() || world.isNight() || world.isClient || !world.isSkyVisible(getBlockPos()) || hasStatusEffect(SolarApocalypse.INSTANCE.getSUNSCREEN())) return;
        setOnFireFor(8);
    }
}