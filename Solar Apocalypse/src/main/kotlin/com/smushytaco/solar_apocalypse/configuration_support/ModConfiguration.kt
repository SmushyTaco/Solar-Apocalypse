package com.smushytaco.solar_apocalypse.configuration_support
import com.smushytaco.solar_apocalypse.SolarApocalypse
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
@Config(name = SolarApocalypse.MOD_ID)
class ModConfiguration: ConfigData {
    @Comment("Default value is 3.0. The number it's set to is the day this phase starts.")
    val myceliumAndGrassTurnToDirtInDaylightDay = 3.0
    @Comment("Default value is 5.0. The number it's set to is the day this phase starts.")
    val blocksAndWaterAreAffectedByDaylightDay = 5.0
    @Comment("Default value is 7.0. The number it's set to is the day this phase starts.")
    val mobsAndPlayersBurnInDaylightDay = 7.0
}