package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
@Mixin(IceBlock.class)
public abstract class IceBlocksMeltInDaylight {
    @Shadow
    protected abstract void melt(BlockState state, World world, BlockPos pos);
    @Inject(method = "randomTick", at = @At("HEAD"))
    private void hookRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockPos blockPos = pos.offset(Direction.UP);
        double worldAge = world.getTimeOfDay() / 24000.0D;
        if (worldAge < SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay() || world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos)) return;
        melt(state, world, pos);
    }
}