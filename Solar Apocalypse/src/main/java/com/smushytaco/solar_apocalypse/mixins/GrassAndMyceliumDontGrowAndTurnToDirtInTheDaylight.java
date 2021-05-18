package com.smushytaco.solar_apocalypse.mixins;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(SpreadableBlock.class)
public abstract class GrassAndMyceliumDontGrowAndTurnToDirtInTheDaylight {
    @Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
    private static void hookCanSurvive(BlockState state, WorldView worldView, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = pos.offset(Direction.UP);
        World world = (World) worldView;
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightDay()) || world.isNight() || !world.isSkyVisible(blockPos)) return;
        cir.setReturnValue(false);
    }
}