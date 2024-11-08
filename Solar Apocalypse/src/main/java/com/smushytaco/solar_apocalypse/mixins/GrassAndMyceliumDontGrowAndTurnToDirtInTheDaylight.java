package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(SpreadableBlock.class)
public abstract class GrassAndMyceliumDontGrowAndTurnToDirtInTheDaylight {
    @ModifyReturnValue(method = "canSurvive", at = @At("RETURN"))
    private static boolean hookCanSurvive(boolean original, BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.offset(Direction.UP);
        World realWorld = (World) world;
        return (!WorldDayCalculation.INSTANCE.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getPhaseOneDay()) || realWorld.isNight() || !realWorld.isSkyVisible(blockPos)) && original;
    }
}