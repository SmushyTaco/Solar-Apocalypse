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
}