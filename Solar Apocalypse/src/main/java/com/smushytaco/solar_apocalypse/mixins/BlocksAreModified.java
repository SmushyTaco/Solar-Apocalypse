package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.sugar.Local;
import com.smushytaco.solar_apocalypse.mixin_logic.BlocksAreModifiedLogic;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ServerWorld.class)
public abstract class BlocksAreModified {
    @Inject(method = "tickChunk", at = @At("RETURN"))
    private void hookRandomTick(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci, @Local Profiler profiler, @Local(ordinal = 1) int i, @Local(ordinal = 2) int j) {
        BlocksAreModifiedLogic.INSTANCE.apocalypseRandomTicks((ServerWorld) (Object) this, profiler, chunk, i, j);
    }
}