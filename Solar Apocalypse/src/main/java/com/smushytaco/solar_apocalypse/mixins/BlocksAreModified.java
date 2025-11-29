package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.sugar.Local;
import com.smushytaco.solar_apocalypse.mixin_logic.BlocksAreModifiedLogic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ServerLevel.class)
public abstract class BlocksAreModified {
    @Inject(method = "tickChunk", at = @At("RETURN"))
    private void hookRandomTick(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci, @Local ProfilerFiller profiler, @Local(ordinal = 1) int i, @Local(ordinal = 2) int j) {
        BlocksAreModifiedLogic.INSTANCE.apocalypseRandomTicks((ServerLevel) (Object) this, profiler, chunk, i, j);
    }
}