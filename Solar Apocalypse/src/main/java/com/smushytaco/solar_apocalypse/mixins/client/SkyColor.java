package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.SolarApocalypseClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(Biome.class)
public abstract class SkyColor {
    @ModifyReturnValue(method = "getFogColor", at = @At("RETURN"))
    public int hookGetFogColor(int original) {
        SolarApocalypseClient.INSTANCE.updateFogColor(original, null);
        return SolarApocalypseClient.INSTANCE.getHasInitialized() ? SolarApocalypseClient.INSTANCE.getFogColor() : original;
    }
    @ModifyReturnValue(method = "getSkyColor", at = @At("RETURN"))
    public int hookGetSkyColor(int original) {
        SolarApocalypseClient.INSTANCE.updateSkyColor(original, null);
        return SolarApocalypseClient.INSTANCE.getHasInitialized() ? SolarApocalypseClient.INSTANCE.getSkyColor() : original;
    }
    @ModifyReturnValue(method = "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z", at = @At("RETURN"))
    public boolean hookCanSetIce(boolean original, WorldView world, BlockPos pos, boolean doWaterCheck) {
        if (!(world instanceof World realWorld)) return original;
        if (SolarApocalypse.INSTANCE.apocalypseChecks(realWorld, pos)) return false;
        return original;
    }
    @ModifyReturnValue(method = "canSetSnow", at = @At("RETURN"))
    public boolean hookCanSetSnow(boolean original, WorldView world, BlockPos pos) {
        if (!(world instanceof World realWorld)) return original;
        if (SolarApocalypse.INSTANCE.apocalypseChecks(realWorld, pos)) return false;
        return original;
    }
}