package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.BlockCache;
import net.minecraft.block.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public @NotNull String getCacheIdentifier() { return cacheIdentifier; }
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public void setCacheIdentifier(@NotNull String s) { cacheIdentifier = s; }
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public @NotNull HashSet<String> getCacheTags() { return cacheTags; }
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public @NotNull HashSet<String> getCacheCorrectClasses() { return cacheCorrectClasses; }
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public @NotNull HashSet<String> getCacheIncorrectClasses() { return cacheIncorrectClasses; }
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public boolean getCacheShouldRandomTick() { return cacheShouldRandomTick; }
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public void setCacheShouldRandomTick(boolean b) { cacheShouldRandomTick = b; }
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public boolean getCacheShouldBurn() { return cacheShouldBurn; }
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public void setCacheShouldBurn(boolean b) { cacheShouldBurn = b; }
}