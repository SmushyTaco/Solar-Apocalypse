package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(SpreadingSnowyDirtBlock.class)
public abstract class GrassAndMyceliumDontGrowAndTurnToDirtInTheDaylight {
    @Inject(method = "canBeGrass", at = @At("RETURN"), cancellable = true)
    private static void hookCanSurvive(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = pos.relative(Direction.UP);
        Level realWorld = (Level) world;
        if (!WorldDayCalculation.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightDay()) || realWorld.isNight() || !realWorld.canSeeSky(blockPos)) return;
        cir.setReturnValue(false);
    }
}