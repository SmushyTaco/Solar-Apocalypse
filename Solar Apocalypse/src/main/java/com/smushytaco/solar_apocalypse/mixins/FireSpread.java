package com.smushytaco.solar_apocalypse.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.solar_apocalypse.mixin_logic.FireSpreadLogic;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(FireBlock.class)
public abstract class FireSpread {
    @WrapOperation(method = "getBurnChance(Lnet/minecraft/block/BlockState;)I", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;getInt(Ljava/lang/Object;)I", remap = false))
    private int hookGetBurnChance(Object2IntMap<Block> instance, Object o, Operation<Integer> original, BlockState state) { return FireSpreadLogic.INSTANCE.burnOrSpreadChance(instance, o, original, state, 60); }
    @WrapOperation(method = "getSpreadChance", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;getInt(Ljava/lang/Object;)I", remap = false))
    private int hookGetSpreadChance(Object2IntMap<Block> instance, Object o, Operation<Integer> original, BlockState state) { return FireSpreadLogic.INSTANCE.burnOrSpreadChance(instance, o, original, state, 100); }
}