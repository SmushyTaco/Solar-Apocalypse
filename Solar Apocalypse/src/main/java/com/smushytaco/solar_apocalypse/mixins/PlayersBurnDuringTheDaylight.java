package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.Sunscreen;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
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
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay()) || !isAlive() || isOnFire() || world.isRaining() || isSpectator() || isCreative() || world.isNight() || world.isClient || !world.isSkyVisible(getBlockPos()) || hasStatusEffect(Sunscreen.INSTANCE)) return;
        setOnFireFor(8);
    }
}