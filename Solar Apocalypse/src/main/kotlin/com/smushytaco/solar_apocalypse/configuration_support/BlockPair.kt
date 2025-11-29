package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse.stringIdentifier
import net.minecraft.world.level.block.Block
data class BlockPair(val blockOne: String = "", val blockTwo: String = "") {
    constructor(blockOne: Block, blockTwo: Block) : this(blockOne.stringIdentifier, blockTwo.stringIdentifier)
    constructor(blockOne: Block, blockTwo: String) : this(blockOne.stringIdentifier, blockTwo)
}