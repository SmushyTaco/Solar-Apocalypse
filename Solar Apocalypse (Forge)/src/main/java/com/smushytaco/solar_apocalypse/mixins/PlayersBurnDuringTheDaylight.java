package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.Sunscreen;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(value = Player.class)
public abstract class PlayersBurnDuringTheDaylight extends LivingEntity {
    protected PlayersBurnDuringTheDaylight(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }
    @Shadow
    public abstract boolean isCreative();
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void hookTickMovement(CallbackInfo ci) {
        if (!WorldDayCalculation.isOldEnough(level, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay()) || !isAlive() || isOnFire() || level.isRaining() || isSpectator() || isCreative() || level.isNight() || level.isClientSide || !level.canSeeSky(blockPosition()) || hasEffect(Sunscreen.INSTANCE)) return;
        setSecondsOnFire(8);
    }
}