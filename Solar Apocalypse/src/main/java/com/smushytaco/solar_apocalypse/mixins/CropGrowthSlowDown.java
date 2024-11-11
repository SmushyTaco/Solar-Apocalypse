package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(CropBlock.class)
public abstract class CropGrowthSlowDown {
    @WrapWithCondition(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean hookSetBlockState(ServerWorld instance, BlockPos blockPos, BlockState blockState, int i, BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!WorldDayCalculation.INSTANCE.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownDay()) || world.isNight() || world.isRaining() || world.isSkyVisible(blockPos) || SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownMultiplier() < 2) return true;
        return random.nextInt(SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownMultiplier()) == 0;
    }
}