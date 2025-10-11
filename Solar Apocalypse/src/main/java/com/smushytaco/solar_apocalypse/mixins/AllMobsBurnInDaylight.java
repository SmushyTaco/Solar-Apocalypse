package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
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
@Mixin(value = MobEntity.class, priority = 999)
public abstract class AllMobsBurnInDaylight extends LivingEntity {
    protected AllMobsBurnInDaylight(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }
    @Shadow
    protected boolean isAffectedByDaylight() { throw new AssertionError(); }
    @ModifyReturnValue(method = "isAffectedByDaylight", at = @At("RETURN"))
    private boolean hookIsAffectedByDaylight(boolean original) { return original || WorldDayCalculation.INSTANCE.isOldEnough(getEntityWorld(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) && isAlive() && !isOnFire() && !getEntityWorld().isRaining() && !getEntityWorld().isNight() && !getEntityWorld().isClient() && (getEntityWorld().isSkyVisible(getBlockPos()) || SolarApocalypse.INSTANCE.shouldHeatLayerDamage(this, getEntityWorld())) && !hasStatusEffect(SolarApocalypse.INSTANCE.getSunscreen()); }
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void hookTickMovement(CallbackInfo ci) { if (WorldDayCalculation.INSTANCE.isOldEnough(getEntityWorld(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) && isAlive() && !isOnFire() && !getEntityWorld().isRaining() && !getEntityWorld().isNight() && isAffectedByDaylight() && !getEntityWorld().isClient() && (getEntityWorld().isSkyVisible(getBlockPos()) || SolarApocalypse.INSTANCE.shouldHeatLayerDamage(this, getEntityWorld())) && !hasStatusEffect(SolarApocalypse.INSTANCE.getSunscreen())) setOnFireFor(8); }
}