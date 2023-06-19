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
    @Comment("Default value is NONE. If set to NONE coarse dirt will never turn to sand. If set to any of the other options coarse dirt will turn to sand for the phase you chose. ")
    val coarseDirtTurnsToSandPhase = CoarseDirtToSandOptions.NONE
    @Comment("Default value is \"minecraft:overworld\". All dimensions listed here will be effected by the Solar Apocalypse mod.")
    val dimensionWhitelist = listOf("minecraft:overworld")
}