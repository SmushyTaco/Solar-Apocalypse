package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LavaFluid.class)
public abstract class LavaModification {
    @WrapOperation(method = "getSlopeFindDistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;ultraWarm()Z"))
    private boolean hookGetMaxFlowDistance(DimensionType instance, Operation<Boolean> original, LevelReader world) { return original.call(instance) || world instanceof Level realWorld && WorldDayCalculation.INSTANCE.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getPhaseOneDay()); }
    @WrapOperation(method = "getDropOff", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;ultraWarm()Z"))
    private boolean hookGetLevelDecreasePerBlock(DimensionType instance, Operation<Boolean> original, LevelReader world) { return original.call(instance) || world instanceof Level realWorld && WorldDayCalculation.INSTANCE.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getPhaseOneDay()); }
    @WrapOperation(method = "getTickDelay", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;ultraWarm()Z"))
    private boolean hookGetTickRate(DimensionType instance, Operation<Boolean> original, LevelReader world) { return original.call(instance) || world instanceof Level realWorld && WorldDayCalculation.INSTANCE.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getPhaseOneDay()); }
}