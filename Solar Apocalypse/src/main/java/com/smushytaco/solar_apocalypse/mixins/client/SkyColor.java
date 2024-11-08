package com.smushytaco.solar_apocalypse.mixins.client;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.client.Render;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Environment(EnvType.CLIENT)
@Mixin(Biome.class)
public abstract class SkyColor {
    @ModifyReturnValue(method = "getFogColor", at = @At("RETURN"))
    public int hookGetFogColor(int original) { return Render.INSTANCE.getSkyColor(original); }
    @ModifyReturnValue(method = "getSkyColor", at = @At("RETURN"))
    public int hookGetSkyColor(int original) { return Render.INSTANCE.getSkyColor(original); }
}