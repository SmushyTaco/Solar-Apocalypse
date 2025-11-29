package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
@Mixin(LivingEntity.class)
public abstract class GreaterFireDamage extends Entity {
    protected GreaterFireDamage(EntityType<?> type, Level world) { super(type, world); }
    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);
    @SuppressWarnings("resource")
    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), index = 3, argsOnly = true)
    private float modifyDamageAmount(float value, ServerLevel world, DamageSource source) {
        if (!source.is(DamageTypeTags.IS_FIRE) || hasEffect(MobEffects.FIRE_RESISTANCE) || !WorldDayCalculation.INSTANCE.isOldEnough(level(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) || !isAlive() || level().isRaining() || level().isDarkOutside() || level().isClientSide() || (!level().canSeeSky(blockPosition()) && !SolarApocalypse.INSTANCE.shouldHeatLayerDamage(this, level()))) return value;
        return value * Mth.clamp(SolarApocalypse.INSTANCE.getConfig().getSolarFireDamageMultiplier(), 1.0F, Float.MAX_VALUE);
    }
}