package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(FlowingFluid.class)
public abstract class WaterIsFinite {
    @WrapOperation(method = "getNewLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FlowingFluid;canConvertToSource(Lnet/minecraft/server/level/ServerLevel;)Z"))
    private boolean hookGetUpdatedState(FlowingFluid instance, ServerLevel serverWorld, Operation<Boolean> original, ServerLevel world, BlockPos pos, BlockState state) { return instance instanceof WaterFluid ? !SolarApocalypse.INSTANCE.apocalypseChecks(serverWorld, pos, true) && original.call(instance, serverWorld) : original.call(instance, serverWorld); }
}