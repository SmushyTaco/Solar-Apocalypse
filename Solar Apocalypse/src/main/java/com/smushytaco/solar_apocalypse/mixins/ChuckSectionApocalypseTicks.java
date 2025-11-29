package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.smushytaco.solar_apocalypse.ApocalypseTickable;
import com.smushytaco.solar_apocalypse.BlockCache;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(LevelChunkSection.class)
public abstract class ChuckSectionApocalypseTicks implements ApocalypseTickable {
    @Unique
    private int randomApocalypseTickableBlockCount = 0;
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public int getRandomApocalypseTickableBlockCount() { return randomApocalypseTickableBlockCount; }
    @WrapOperation(method = "recalcBlockCounts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/PalettedContainer;count(Lnet/minecraft/world/level/chunk/PalettedContainer$CountConsumer;)V"))
    private void hookCalculateCounts(PalettedContainer<BlockState> instance, PalettedContainer.CountConsumer<BlockState> counter, Operation<Void> original) {
        original.call(instance, counter);
        if (!(counter instanceof ApocalypseTickable apocalypseTickable)) return;
        randomApocalypseTickableBlockCount = apocalypseTickable.getRandomApocalypseTickableBlockCount();
    }
    @Inject(method = "<init>(Lnet/minecraft/world/level/chunk/LevelChunkSection;)V", at = @At("RETURN"))
    private void hookInitialize(LevelChunkSection section, CallbackInfo ci) {
        if (section instanceof ApocalypseTickable apocalypseTickable) randomApocalypseTickableBlockCount = apocalypseTickable.getRandomApocalypseTickableBlockCount();
    }
    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"))
    private void hookSetBlockState(int x, int y, int z, BlockState state, boolean lock, CallbackInfoReturnable<BlockState> cir, @Local(ordinal = 1) BlockState blockState) {
        if (blockState.getBlock() instanceof BlockCache blockCache && blockCache.getCacheShouldRandomTick()) randomApocalypseTickableBlockCount--;
        if (state.getBlock() instanceof BlockCache blockCache && blockCache.getCacheShouldRandomTick()) randomApocalypseTickableBlockCount++;
    }
}