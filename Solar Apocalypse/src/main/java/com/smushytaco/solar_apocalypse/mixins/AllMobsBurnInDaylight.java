package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
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
@Mixin(value = Mob.class, priority = 999)
public abstract class AllMobsBurnInDaylight extends LivingEntity {
    protected AllMobsBurnInDaylight(EntityType<? extends LivingEntity> entityType, Level world) { super(entityType, world); }
    @Shadow
    protected boolean isSunBurnTick() { throw new AssertionError(); }
    @SuppressWarnings("resource")
    @ModifyReturnValue(method = "isSunBurnTick", at = @At("RETURN"))
    private boolean hookIsAffectedByDaylight(boolean original) { return original || WorldDayCalculation.INSTANCE.isOldEnough(level(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) && isAlive() && !isOnFire() && !level().isRaining() && !level().isDarkOutside() && !level().isClientSide() && (level().canSeeSky(blockPosition()) || SolarApocalypse.INSTANCE.shouldHeatLayerDamage(this, level())) && !hasEffect(SolarApocalypse.INSTANCE.getSunscreen()); }
    @SuppressWarnings("resource")
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void hookTickMovement(CallbackInfo ci) { if (WorldDayCalculation.INSTANCE.isOldEnough(level(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) && isAlive() && !isOnFire() && !level().isRaining() && !level().isDarkOutside() && isSunBurnTick() && !level().isClientSide() && (level().canSeeSky(blockPosition()) || SolarApocalypse.INSTANCE.shouldHeatLayerDamage(this, level())) && !hasEffect(SolarApocalypse.INSTANCE.getSunscreen())) igniteForSeconds(8); }
}