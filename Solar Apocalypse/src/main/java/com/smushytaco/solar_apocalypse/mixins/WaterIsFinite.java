package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(FlowableFluid.class)
public abstract class WaterIsFinite {
    @WrapOperation(method = "getUpdatedState", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FlowableFluid;isInfinite(Lnet/minecraft/world/World;)Z"))
    private boolean hookGetUpdatedState(FlowableFluid instance, World serverWorld, Operation<Boolean> original, World world, BlockPos pos, BlockState state) { return instance instanceof WaterFluid ? !SolarApocalypse.INSTANCE.apocalypseChecks(serverWorld, pos) && original.call(instance, serverWorld) : original.call(instance, serverWorld); }
}