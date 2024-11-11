package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.smushytaco.solar_apocalypse.BlockCache;
import net.minecraft.block.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import java.util.HashSet;
@Mixin(AbstractBlock.class)
public abstract class BlockCacheImplementation implements BlockCache {
    @Unique
    private String cacheIdentifier = "";
    @Unique
    private final HashSet<String> cacheTags = new HashSet<>();
    @Unique
    private final HashSet<String> cacheCorrectClasses = new HashSet<>();
    @Unique
    private final HashSet<String> cacheIncorrectClasses = new HashSet<>();
    @Unique
    private boolean cacheShouldRandomTick = false;
    @Unique
    private boolean cacheShouldBurn = false;
    @Override
    public @NotNull String getCacheIdentifier() { return cacheIdentifier; }
    @Override
    public void setCacheIdentifier(@NotNull String s) { cacheIdentifier = s; }
    @Override
    public @NotNull HashSet<String> getCacheTags() { return cacheTags; }
    @Override
    public @NotNull HashSet<String> getCacheCorrectClasses() { return cacheCorrectClasses; }
    @Override
    public @NotNull HashSet<String> getCacheIncorrectClasses() { return cacheIncorrectClasses; }
    @Override
    public boolean getCacheShouldRandomTick() { return cacheShouldRandomTick; }
    @Override
    public void setCacheShouldRandomTick(boolean b) { cacheShouldRandomTick = b; }
    @Override
    public boolean getCacheShouldBurn() { return cacheShouldBurn; }
    @Override
    public void setCacheShouldBurn(boolean b) { cacheShouldBurn = b; }
    @ModifyReturnValue(method = "hasRandomTicks", at = @At("RETURN"))
    private boolean hookHasRandomTicks(boolean original) { return original || cacheShouldRandomTick; }
}