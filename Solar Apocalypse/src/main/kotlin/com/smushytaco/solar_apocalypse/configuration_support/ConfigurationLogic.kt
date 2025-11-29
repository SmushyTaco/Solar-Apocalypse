package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse.containsTag
import com.smushytaco.solar_apocalypse.SolarApocalypse.isInstanceOfClassByName
import com.smushytaco.solar_apocalypse.SolarApocalypse.stringIdentifier
import net.minecraft.world.level.block.Block
object ConfigurationLogic {
    fun isWhitelisted(original: Boolean, block: Block, identifiers: Collection<String>, tags: Collection<String>, classes: Collection<String>) = original || identifiers.contains(block.stringIdentifier) || tags.any { block.containsTag(it) } || classes.any { isInstanceOfClassByName(block, it) }
}