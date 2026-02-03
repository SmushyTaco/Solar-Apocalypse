package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(Biome.class)
public abstract class SnowAndIce {
    @ModifyReturnValue(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At("RETURN"))
    public boolean hookCanSetIce(boolean original, LevelReader world, BlockPos pos, boolean doWaterCheck) {
        if (!(world instanceof Level realWorld)) return original;
        if (SolarApocalypse.INSTANCE.apocalypseChecks(realWorld, pos, false)) return false;
        return original;
    }
    @ModifyReturnValue(method = "shouldSnow", at = @At("RETURN"))
    public boolean hookCanSetSnow(boolean original, LevelReader world, BlockPos pos) {
        if (!(world instanceof Level realWorld)) return original;
        if (SolarApocalypse.INSTANCE.apocalypseChecks(realWorld, pos, false)) return false;
        return original;
    }
}