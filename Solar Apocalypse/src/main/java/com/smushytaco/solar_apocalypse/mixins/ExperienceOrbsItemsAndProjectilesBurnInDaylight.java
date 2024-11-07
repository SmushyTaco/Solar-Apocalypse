package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
@Mixin(Entity.class)
public abstract class ExperienceOrbsItemsAndProjectilesBurnInDaylight {
    @Shadow
    public abstract World getWorld();
    @Shadow
    public abstract boolean isAlive();
    @Shadow
    public abstract BlockPos getBlockPos();
    @ModifyReturnValue(method = "isInLava", at = @At("RETURN"))
    private boolean hookIsInLava(boolean original) {
        Entity entity = (Entity) (Object) this;
        return original || (entity instanceof ExperienceOrbEntity || entity instanceof ItemEntity || entity instanceof PersistentProjectileEntity || entity instanceof SnowballEntity) && WorldDayCalculation.INSTANCE.isOldEnough(getWorld(), SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay()) && isAlive() && !getWorld().isRaining() && !getWorld().isNight() && !getWorld().isClient && getWorld().isSkyVisible(getBlockPos());
    }
}