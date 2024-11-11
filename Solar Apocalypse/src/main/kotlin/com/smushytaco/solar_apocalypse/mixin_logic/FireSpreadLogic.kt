package com.smushytaco.solar_apocalypse.mixin_logic
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.smushytaco.solar_apocalypse.SolarApocalypse
import com.smushytaco.solar_apocalypse.configuration_support.ConfigurationLogic
import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.block.Block
import net.minecraft.block.BlockState
object FireSpreadLogic {
    fun burnOrSpreadChance(instance: Object2IntMap<Block>, o: Any, original: Operation<Int>, state: BlockState, chance: Int): Int {
        val originalValue = original.call(instance, o)
        if (originalValue != 0) return chance
        return if (ConfigurationLogic.isWhitelisted(false, state.block, SolarApocalypse.config.burnableBlockIdentifiers, SolarApocalypse.config.burnableBlockTags, SolarApocalypse.config.burnableBlockClasses)) chance else originalValue
    }
}