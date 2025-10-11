package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.ApocalypseTickable;
import com.smushytaco.solar_apocalypse.BlockCache;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(targets = "net.minecraft.world.chunk.ChunkSection$BlockStateCounter")
public abstract class BlockStateCounterApocalypseTicks implements ApocalypseTickable {
    @Unique
    private int randomApocalypseTickableBlockCount = 0;
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public int getRandomApocalypseTickableBlockCount() { return randomApocalypseTickableBlockCount; }
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "accept", at = @At("RETURN"))
    private void hookAccept(BlockState blockState, int i, CallbackInfo ci) {
        if (blockState.getBlock() instanceof BlockCache blockCache && blockCache.getCacheShouldRandomTick()) randomApocalypseTickableBlockCount += i;
    }
}