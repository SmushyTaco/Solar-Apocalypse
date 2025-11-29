package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Entity.class)
public abstract class ExperienceOrbsItemsAndProjectilesBurn {
    @Shadow
    public abstract Level level();
    @Shadow
    public abstract boolean isAlive();
    @Shadow
    public abstract BlockPos blockPosition();
    @SuppressWarnings("resource")
    @Inject(method = "tick", at = @At("RETURN"))
    private void hookTick(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity.isOnFire() || entity.fireImmune()) return;
        boolean conditions = (entity instanceof ExperienceOrb || entity instanceof ItemEntity || entity instanceof AbstractArrow || entity instanceof Snowball) && WorldDayCalculation.INSTANCE.isOldEnough(level(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) && isAlive() && !level().isRaining() && !level().isDarkOutside() && !level().isClientSide() && (level().canSeeSky(blockPosition()) || SolarApocalypse.INSTANCE.shouldHeatLayerDamage(entity, level()));
        if (conditions) {
            entity.lavaHurt();
            entity.igniteForTicks(300);
        }
    }
}