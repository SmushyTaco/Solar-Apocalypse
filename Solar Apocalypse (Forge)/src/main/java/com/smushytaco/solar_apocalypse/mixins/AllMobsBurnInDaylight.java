package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.Sunscreen;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(value = Mob.class, priority = 1001)
public abstract class AllMobsBurnInDaylight extends LivingEntity {
    protected AllMobsBurnInDaylight(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Shadow
    protected boolean isSunBurnTick() { throw new AssertionError(); }
    @Inject(method = "isSunBurnTick", at = @At("RETURN"), cancellable = true)
    private void hookIsAffectedByDaylight(CallbackInfoReturnable<Boolean> cir) {
        if (!WorldDayCalculation.isOldEnough(level, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay()) || !isAlive() || isOnFire() || level.isRaining() || level.isNight() || level.isClientSide || !level.canSeeSky(blockPosition()) || hasEffect(Sunscreen.INSTANCE)) return;
        cir.setReturnValue(true);
    }
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void hookTickMovement(CallbackInfo ci) {
        if (!WorldDayCalculation.isOldEnough(level, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay()) || !isAlive() || isOnFire() || level.isRaining() || level.isNight() || !isSunBurnTick() || level.isClientSide || !level.canSeeSky(blockPosition()) || hasEffect(Sunscreen.INSTANCE)) return;
        setSecondsOnFire(8);
    }
}