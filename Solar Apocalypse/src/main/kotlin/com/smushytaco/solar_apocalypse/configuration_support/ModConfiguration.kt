package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
@Config(name = SolarApocalypse.MOD_ID)
class ModConfiguration: ConfigData {
    @Comment("Default value is 3.0. The number it's set to is the day this phase starts. If set to a negative number the phase will never start.")
    val myceliumAndGrassTurnToDirtInDaylightDay = 3.0
    @Comment("Default value is 1.5. The number it's set to is how much the sun size is multiplied by for the Mycelium And Grass Turn To Dirt In Daylight phase. Set it to 1.0 to keep the sun size unchanged.")
    val myceliumAndGrassTurnToDirtInDaylightSunSizeMultiplier = 1.5F
    @Comment("Default value is 5.0. The number it's set to is the day this phase starts. If set to a negative number the phase will never start.")
    val blocksAndWaterAreAffectedByDaylightDay = 5.0
    @Comment("Default value is 2.0. The number it's set to is how much the sun size is multiplied by for the Blocks And Water Are Affected By Daylight phase. Set it to 1.0 to keep the sun size unchanged.")
    val blocksAndWaterAreAffectedByDaylightSunSizeMultiplier = 2.0F
    @Comment("Default value is 7.0. The number it's set to is the day this phase starts. If set to a negative number the phase will never start.")
    val mobsAndPlayersBurnInDaylightDay = 7.0
    @Comment("Default value is 3.0. The number it's set to is how much the sun size is multiplied by for the Mobs And Players Burn In Daylight phase. Set it to 1.0 to keep the sun size unchanged.")
    val mobsAndPlayersBurnInDaylightSunSizeMultiplier = 3.0F
    @Comment("Default value is NONE. If set to NONE coarse dirt will never turn to sand. If set to any of the other options coarse dirt will turn to sand for the phase you chose.")
    val coarseDirtTurnsToSandPhase = Phases.NONE
    @Comment("Default value is NONE. If set to NONE cobbled and cracked stones will never turn to lava. If set to any of the other options cobbled and cracked stones will turn to lava for the phase you chose.")
    val cobbledAndCrackedStonesTurnsToLavaPhase = Phases.NONE
    @Comment("Default value is BLOCKS_AND_WATER_ARE_AFFECTED_BY_DAYLIGHT_PHASE. If set to NONE crop growth will remain untouched. If set to any of the other options crop growth will be slower by the multiplier you chose.")
    val cropGrowthSlowDownPhase = Phases.BLOCKS_AND_WATER_ARE_AFFECTED_BY_DAYLIGHT_PHASE
    @Comment("Default value is 3. The value is how many times slower crop growth is. Set it to 1 or below to restore vanilla behavior.")
    val cropGrowthSlowDownMultiplier = 3
    @Comment("Default value is 5.0. The value is how many times more damage you'll take in the daylight when on fire.")
    val daylightDamageMultiplier = 5.0F
    @Comment("Default value is yes. This determines if the sky will be a custom color in phase one.")
    val shouldPhaseOneHaveCustomSkyColor = true
    @Comment("Default value is 255. This determines the red coloring of the sky in phase one.")
    val phaseOneRed = 255
    @Comment("Default value is 165. This determines the green coloring of the sky in phase one.")
    val phaseOneGreen = 165
    @Comment("Default value is 0. This determines the blue coloring of the sky in phase one.")
    val phaseOneBlue = 0
    @Comment("Default value is yes. This determines if the sky will be a custom color in phase two.")
    val shouldPhaseTwoHaveCustomSkyColor = true
    @Comment("Default value is 255. This determines the red coloring of the sky in phase two.")
    val phaseTwoRed = 255
    @Comment("Default value is 140. This determines the green coloring of the sky in phase two.")
    val phaseTwoGreen = 140
    @Comment("Default value is 0. This determines the blue coloring of the sky in phase two.")
    val phaseTwoBlue = 0
    @Comment("Default value is yes. This determines if the sky will be a custom color in phase three.")
    val shouldPhaseThreeHaveCustomSkyColor = true
    @Comment("Default value is 255. This determines the red coloring of the sky in phase three.")
    val phaseThreeRed = 255
    @Comment("Default value is 69. This determines the green coloring of the sky in phase three.")
    val phaseThreeGreen = 69
    @Comment("Default value is 0. This determines the blue coloring of the sky in phase three.")
    val phaseThreeBlue = 0
    @Comment("Default value is \"minecraft:overworld\". All dimensions listed here will be effected by the Solar Apocalypse mod.")
    val dimensionWhitelist = listOf("minecraft:overworld")
}