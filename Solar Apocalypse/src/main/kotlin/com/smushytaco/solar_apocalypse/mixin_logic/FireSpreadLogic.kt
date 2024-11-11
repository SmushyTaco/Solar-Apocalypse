package com.smushytaco.solar_apocalypse.mixin_logic
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.smushytaco.solar_apocalypse.BlockCache
import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.block.Block
import net.minecraft.block.BlockState
object FireSpreadLogic {
    fun burnOrSpreadChance(instance: Object2IntMap<Block>, o: Any, original: Operation<Int>, state: BlockState, chance: Int): Int {
        val originalValue = original.call(instance, o)
        if (originalValue != 0) return chance
        return if ((state.block as BlockCache).cacheShouldBurn) chance else originalValue
    }
}