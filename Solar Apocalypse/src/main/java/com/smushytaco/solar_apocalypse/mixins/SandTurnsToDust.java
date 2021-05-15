package com.smushytaco.solar_apocalypse.mixins;
import com.smushytaco.solar_apocalypse.SolarApocalypse;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SandBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Random;
@SuppressWarnings("deprecation")
@Mixin(SandBlock.class)
public abstract class SandTurnsToDust extends Block {
    public SandTurnsToDust(Settings settings) {
        super(settings);
    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        BlockPos blockPos = pos.offset(Direction.UP);
        double worldAge = world.getTimeOfDay() / 24000.0D;
        if (worldAge < 5.0D || world.isNight() || world.isRaining() || !world.isSkyVisible(blockPos)) return;
        world.setBlockState(pos, SolarApocalypse.INSTANCE.getDUST().getDefaultState());
    }
    @Override
    public boolean hasRandomTicks(BlockState state) { return true; }
}