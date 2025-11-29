package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Player.class)
public abstract class PlayersBurn extends LivingEntity {
    protected PlayersBurn(EntityType<? extends LivingEntity> entityType, Level world) { super(entityType, world); }
    @Shadow
    public abstract boolean isCreative();
    @SuppressWarnings("resource")
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void hookTickMovement(CallbackInfo ci) { if (WorldDayCalculation.INSTANCE.isOldEnough(level(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()) && isAlive() && !isOnFire() && !level().isRaining() && !isSpectator() && !isCreative() && !level().isDarkOutside() && !level().isClientSide() && (level().canSeeSky(blockPosition()) || SolarApocalypse.INSTANCE.shouldHeatLayerDamage(this, level())) && !hasEffect(SolarApocalypse.INSTANCE.getSunscreen())) igniteForSeconds(8); }
}