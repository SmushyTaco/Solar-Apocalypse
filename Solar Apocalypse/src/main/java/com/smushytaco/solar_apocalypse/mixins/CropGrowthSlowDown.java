package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(CropBlock.class)
public abstract class CropGrowthSlowDown {
    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean hookSetBlockState(ServerLevel instance, BlockPos blockPos, BlockState blockState, int i, Operation<Boolean> original, BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!WorldDayCalculation.INSTANCE.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownDay()) || world.isDarkOutside() || world.isRaining() || world.canSeeSky(blockPos) || SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownMultiplier() < 2) return original.call(instance, blockPos, blockState, i);
        if (random.nextInt(SolarApocalypse.INSTANCE.getConfig().getCropGrowthSlowDownMultiplier()) == 0) return original.call(instance, blockPos, blockState, i);
        return false;
    }
}