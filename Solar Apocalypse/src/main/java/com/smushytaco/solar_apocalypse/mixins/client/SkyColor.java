package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(Biome.class)
public abstract class SkyColor {
    @ModifyReturnValue(method = "getFogColor", at = @At("RETURN"))
    public int hookGetFogColor(int original) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return original;
        if (SolarApocalypse.INSTANCE.getConfig().getShouldPhaseThreeHaveCustomSkyColor() && WorldDayCalculation.INSTANCE.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay())) {
            return SolarApocalypse.INSTANCE.rgbToInt(SolarApocalypse.INSTANCE.getConfig().getPhaseThreeRed(), SolarApocalypse.INSTANCE.getConfig().getPhaseThreeGreen(), SolarApocalypse.INSTANCE.getConfig().getPhaseThreeBlue());
        } else if (SolarApocalypse.INSTANCE.getConfig().getShouldPhaseTwoHaveCustomSkyColor() && WorldDayCalculation.INSTANCE.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay())) {
            return SolarApocalypse.INSTANCE.rgbToInt(SolarApocalypse.INSTANCE.getConfig().getPhaseTwoRed(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoGreen(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoBlue());
        } else if (SolarApocalypse.INSTANCE.getConfig().getShouldPhaseOneHaveCustomSkyColor() && WorldDayCalculation.INSTANCE.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightDay())) {
            return SolarApocalypse.INSTANCE.rgbToInt(SolarApocalypse.INSTANCE.getConfig().getPhaseOneRed(), SolarApocalypse.INSTANCE.getConfig().getPhaseOneGreen(), SolarApocalypse.INSTANCE.getConfig().getPhaseOneBlue());
        }
        return original;
    }
    @ModifyReturnValue(method = "getSkyColor", at = @At("RETURN"))
    public int hookGetSkyColor(int original) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return original;
        if (SolarApocalypse.INSTANCE.getConfig().getShouldPhaseThreeHaveCustomSkyColor() && WorldDayCalculation.INSTANCE.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getMobsAndPlayersBurnInDaylightDay())) {
            return SolarApocalypse.INSTANCE.rgbToInt(SolarApocalypse.INSTANCE.getConfig().getPhaseThreeRed(), SolarApocalypse.INSTANCE.getConfig().getPhaseThreeGreen(), SolarApocalypse.INSTANCE.getConfig().getPhaseThreeBlue());
        } else if (SolarApocalypse.INSTANCE.getConfig().getShouldPhaseTwoHaveCustomSkyColor() && WorldDayCalculation.INSTANCE.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay())) {
            return SolarApocalypse.INSTANCE.rgbToInt(SolarApocalypse.INSTANCE.getConfig().getPhaseTwoRed(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoGreen(), SolarApocalypse.INSTANCE.getConfig().getPhaseTwoBlue());
        } else if (SolarApocalypse.INSTANCE.getConfig().getShouldPhaseOneHaveCustomSkyColor() && WorldDayCalculation.INSTANCE.isOldEnough(client.world, SolarApocalypse.INSTANCE.getConfig().getMyceliumAndGrassTurnToDirtInDaylightDay())) {
            return SolarApocalypse.INSTANCE.rgbToInt(SolarApocalypse.INSTANCE.getConfig().getPhaseOneRed(), SolarApocalypse.INSTANCE.getConfig().getPhaseOneGreen(), SolarApocalypse.INSTANCE.getConfig().getPhaseOneBlue());
        }
        return original;
    }
}