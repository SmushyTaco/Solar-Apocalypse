package com.smushytaco.solar_apocalypse.configuration_support;

import com.smushytaco.solar_apocalypse.SolarApocalypse;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(name= SolarApocalypse.MOD_ID)
public class ModConfiguration implements ConfigData {
    @Comment("Default value is 3.0. The number it's set to is the day this phase starts. If set to a negative number the phase will never start.")

    double myceliumAndGrassTurnToDirtInDaylightDay = 3.0;
    @Comment("Default value is 5.0. The number it's set to is the day this phase starts. If set to a negative number the phase will never start.")

    double blocksAndWaterAreAffectedByDaylightDay = 5.0;
    @Comment("Default value is 7.0. The number it's set to is the day this phase starts. If set to a negative number the phase will never start.")

    double mobsAndPlayersBurnInDaylightDay = 7.0;
    @Comment("Default value is NONE. If set to NONE coarse dirt will never turn to sand. If set to any of the other options coarse dirt will turn to sand for the phase you chose. ")

    CoarseDirtToSandOptions coarseDirtTurnsToSandPhase = CoarseDirtToSandOptions.NONE;
    @Comment("Default value is \"minecraft:overworld\". All dimensions listed here will be effected by the Solar Apocalypse mod.")

    ArrayList<String> dimensionWhitelist = new ArrayList<>();
    {
        dimensionWhitelist.add("minecraft:overworld");
    }

    public Double getMobsAndPlayersBurnInDaylightDay() {
        return mobsAndPlayersBurnInDaylightDay;
    }
    public Double getMyceliumAndGrassTurnToDirtInDaylightDay() {
        return myceliumAndGrassTurnToDirtInDaylightDay;
    }

    public Double getBlocksAndWaterAreAffectedByDaylightDay() {
        return blocksAndWaterAreAffectedByDaylightDay;
    }
    public ArrayList<String> getDimensionWhitelist() {
        return dimensionWhitelist;
    }
    public CoarseDirtToSandOptions getCoarseDirtTurnsToSandPhase() {
        return coarseDirtTurnsToSandPhase;
    }
}
