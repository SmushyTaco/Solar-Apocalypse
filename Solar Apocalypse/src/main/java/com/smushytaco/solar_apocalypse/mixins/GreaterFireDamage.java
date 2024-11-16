package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
@Mixin(LivingEntity.class)
public abstract class GreaterFireDamage extends Entity {
    protected GreaterFireDamage(EntityType<?> type, World world) { super(type, world); }
    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);
    @ModifyVariable(method = "damage", at = @At("HEAD"), index = 2, argsOnly = true)
    private float modifyDamageAmount(float value, DamageSource source) {
        if (!source.isIn(DamageTypeTags.IS_FIRE) || hasStatusEffect(StatusEffects.FIRE_RESISTANCE) || !WorldDayCalculation.INSTANCE.isOldEnough(getWorld(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) || !isAlive() || getWorld().isRaining() || getWorld().isNight() || getWorld().isClient || (!getWorld().isSkyVisible(getBlockPos()) && !SolarApocalypse.INSTANCE.shouldHeatLayerDamage(this, getWorld()))) return value;
        return value * MathHelper.clamp(SolarApocalypse.INSTANCE.getConfig().getSolarFireDamageMultiplier(), 1.0F, Float.MAX_VALUE);
    }
}