package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.Sunscreen;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(value = MobEntity.class, priority = 1001)
public abstract class AllMobsBurnInDaylight extends LivingEntity {
    protected AllMobsBurnInDaylight(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Shadow
    protected boolean isAffectedByDaylight() { throw new AssertionError(); }
    @Inject(method = "isAffectedByDaylight", at = @At("RETURN"), cancellable = true)
    private void hookIsAffectedByDaylight(CallbackInfoReturnable<Boolean> cir) {
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay()) || !isAlive() || isOnFire() || world.isRaining() || world.isNight() || world.isClient || !world.isSkyVisible(getBlockPos()) || hasStatusEffect(Sunscreen.INSTANCE)) return;
        cir.setReturnValue(true);
    }
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void hookTickMovement(CallbackInfo ci) {
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay()) || !isAlive() || isOnFire() || world.isRaining() || world.isNight() || !isAffectedByDaylight() || world.isClient || !world.isSkyVisible(getBlockPos()) || hasStatusEffect(Sunscreen.INSTANCE)) return;
        setOnFireFor(8);
    }
}