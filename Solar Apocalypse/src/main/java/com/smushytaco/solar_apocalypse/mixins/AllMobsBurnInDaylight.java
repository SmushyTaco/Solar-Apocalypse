package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
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
        double worldAge = world.getTimeOfDay() / 24000.0D;
        if (worldAge < 7.0D || !isAlive() || isOnFire() || world.isRaining() || world.isNight() || world.isClient || !world.isSkyVisible(getBlockPos()) || hasStatusEffect(SolarApocalypse.INSTANCE.getSUNSCREEN())) return;
        cir.setReturnValue(true);
    }
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void hookTickMovement(CallbackInfo ci) {
        double worldAge = world.getTimeOfDay() / 24000.0D;
        if (worldAge < 7.0D || !isAlive() || isOnFire() || world.isRaining() || world.isNight() || !isAffectedByDaylight() || world.isClient || !world.isSkyVisible(getBlockPos()) || hasStatusEffect(SolarApocalypse.INSTANCE.getSUNSCREEN())) return;
        setOnFireFor(8);
    }
}