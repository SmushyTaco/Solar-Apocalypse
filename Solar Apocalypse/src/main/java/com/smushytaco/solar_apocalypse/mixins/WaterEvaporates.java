package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.mixin_logic.WaterEvaporatesLogic;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Fluid.class)
public abstract class WaterEvaporates {
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    protected boolean hookHasRandomTicks(boolean original) { return (Fluid) (Object) this instanceof WaterFluid || original; }
    @Inject(method = "onRandomTick", at = @At("HEAD"))
    protected void onRandomTick(ServerWorld world, BlockPos pos, FluidState state, Random random, CallbackInfo ci) { WaterEvaporatesLogic.INSTANCE.onRandomTick((Fluid) (Object) this, world, pos, state); }
}