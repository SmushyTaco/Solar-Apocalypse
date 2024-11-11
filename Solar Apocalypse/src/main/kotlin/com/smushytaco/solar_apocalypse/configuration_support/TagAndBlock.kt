package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse.stringIdentifier
import net.minecraft.block.Block
data class TagAndBlock(val tag: String = "", val block: String = "") {
    constructor(tag: String, block: Block) : this(tag, block.stringIdentifier)
}