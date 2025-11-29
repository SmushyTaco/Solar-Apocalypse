package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(SpreadingSnowyDirtBlock.class)
public abstract class PhaseOne {
    @ModifyReturnValue(method = "canBeGrass", at = @At("RETURN"))
    private static boolean hookCanSurvive(boolean original, BlockState state, LevelReader world, BlockPos pos) { return (world instanceof Level realWorld && (!WorldDayCalculation.INSTANCE.isOldEnough(realWorld, SolarApocalypse.INSTANCE.getConfig().getPhaseOneDay()) || realWorld.isDarkOutside() || !realWorld.canSeeSky(pos.above()))) && original; }
}