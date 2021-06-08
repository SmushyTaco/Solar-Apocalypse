package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Random;
@SuppressWarnings("deprecation")
@Mixin(DirtPathBlock.class)
public abstract class DirtPathBlockIsTurnedToDirtInDaylight extends Block {
    public DirtPathBlockIsTurnedToDirtInDaylight(Settings settings) {
        super(settings);
    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos blockPos = pos.offset(Direction.UP);
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay()) || world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos)) return;
        super.randomTick(state, world, pos, random);
    }
}