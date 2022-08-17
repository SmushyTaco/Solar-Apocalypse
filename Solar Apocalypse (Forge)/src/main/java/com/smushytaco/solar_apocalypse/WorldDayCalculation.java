package com.smushytaco.solar_apocalypse;

import net.minecraft.world.level.Level;

public class WorldDayCalculation {

    public static boolean isOldEnough(Level level, Double days) {
        //if (!SolarApocalypse.INSTANCE.config.getDimensionWhitelist().contains(level.dimension().toString())) return false;
        var worldAgeInMinecraftDays = level.dayTime() / 24000.0;
        return (days < 0.0) ? false : worldAgeInMinecraftDays >= days;
    }

}
