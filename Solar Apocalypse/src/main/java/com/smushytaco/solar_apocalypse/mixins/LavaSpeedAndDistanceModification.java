package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LavaFluid.class)
public abstract class LavaSpeedAndDistanceModification {
    @WrapOperation(method = "getMaxFlowDistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/DimensionType;ultrawarm()Z"))
    private boolean hookGetMaxFlowDistance(DimensionType instance, Operation<Boolean> original, WorldView world) { return original.call(instance) || world instanceof World realWorld && WorldDayCalculation.INSTANCE.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()); }
    @WrapOperation(method = "getLevelDecreasePerBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/DimensionType;ultrawarm()Z"))
    private boolean hookGetLevelDecreasePerBlock(DimensionType instance, Operation<Boolean> original, WorldView world) { return original.call(instance) || world instanceof World realWorld && WorldDayCalculation.INSTANCE.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()); }
    @WrapOperation(method = "getTickRate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/DimensionType;ultrawarm()Z"))
    private boolean hookGetTickRate(DimensionType instance, Operation<Boolean> original, WorldView world) { return original.call(instance) || world instanceof World realWorld && WorldDayCalculation.INSTANCE.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getPhaseTwoDay()); }
}