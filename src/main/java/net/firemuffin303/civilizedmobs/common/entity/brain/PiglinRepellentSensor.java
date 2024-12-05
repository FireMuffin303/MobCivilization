package net.firemuffin303.civilizedmobs.common.entity.brain;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.Set;

public class  PiglinRepellentSensor extends Sensor<LivingEntity> {
    public PiglinRepellentSensor(){}

    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        brain.remember(MemoryModuleType.NEAREST_REPELLENT,findPiglinRepellent(world,entity));
    }

    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_REPELLENT);
    }

    private static Optional<BlockPos> findPiglinRepellent(ServerWorld world, LivingEntity entity) {
        return BlockPos.findClosest(entity.getBlockPos(), 8, 4, (pos) -> {
            return isPiglinRepellent(world, pos);
        });
    }

    private static boolean isPiglinRepellent(ServerWorld world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        boolean bl = blockState.isIn(BlockTags.PIGLIN_REPELLENTS);
        return bl && blockState.isOf(Blocks.SOUL_CAMPFIRE) ? CampfireBlock.isLitCampfire(blockState) : bl;
    }
}
