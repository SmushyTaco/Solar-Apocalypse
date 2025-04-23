package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Entity.class)
public abstract class ExperienceOrbsItemsAndProjectilesBurn {
    @Shadow
    public abstract World getWorld();
    @Shadow
    public abstract boolean isAlive();
    @Shadow
    public abstract BlockPos getBlockPos();
    @Inject(method = "tick", at = @At("RETURN"))
    private void hookTick(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity.isOnFire() || entity.isFireImmune()) return;
        boolean conditions = (entity instanceof ExperienceOrbEntity || entity instanceof ItemEntity || entity instanceof PersistentProjectileEntity || entity instanceof SnowballEntity) && WorldDayCalculation.INSTANCE.isOldEnough(getWorld(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) && isAlive() && !getWorld().isRaining() && !getWorld().isNight() && !getWorld().isClient && (getWorld().isSkyVisible(getBlockPos()) || SolarApocalypse.INSTANCE.shouldHeatLayerDamage(entity, getWorld()));
        if (conditions) {
            entity.setOnFireFromLava();
            entity.setOnFireForTicks(300);
        }
    }
}