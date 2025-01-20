package net.firemuffin303.villagefoe.common.entity.brain;

import com.google.common.collect.ImmutableSet;
import net.firemuffin303.villagefoe.registry.ModTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;
import java.util.Set;

public class PiglinNemesisSensor extends Sensor<LivingEntity> {
    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        LivingTargetCache livingTargetCache = brain.getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty());
        Optional<MobEntity> nemesis = Optional.empty();
        for (LivingEntity livingEntity : livingTargetCache.iterate(livingEntity -> livingEntity.getType().isIn(ModTags.PIGLIN_NEMESIS))) {
            nemesis = Optional.of((MobEntity) livingEntity);
        }

        brain.remember(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, nemesis);

    }

    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_NEMESIS,MemoryModuleType.VISIBLE_MOBS);
    }
}
