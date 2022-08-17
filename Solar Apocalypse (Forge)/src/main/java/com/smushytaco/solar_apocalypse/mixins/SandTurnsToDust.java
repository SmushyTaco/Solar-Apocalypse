package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import com.smushytaco.solar_apocalypse.WorldDayCalculation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Random;
@SuppressWarnings("deprecation")
@Mixin(value = SandBlock.class)
public abstract class SandTurnsToDust extends Block {
    public SandTurnsToDust(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        BlockPos blockPos = pos.relative(Direction.UP);
        if (!WorldDayCalculation.isOldEnough(world, SolarApocalypse.INSTANCE.getConfig().getBlocksAndWaterAreAffectedByDaylightDay()) || world.isNight() || world.isRaining() || !world.canSeeSky(blockPos)) return;
        world.setBlockAndUpdate(pos, SolarApocalypse.INSTANCE.DUST.get().defaultBlockState());
    }
    @Override
    public boolean isRandomlyTicking(BlockState state) { return true; }
}