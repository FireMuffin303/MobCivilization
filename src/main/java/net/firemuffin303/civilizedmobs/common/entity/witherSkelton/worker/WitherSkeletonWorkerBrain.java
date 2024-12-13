package net.firemuffin303.civilizedmobs.common.entity.witherSkelton.worker;

import com.google.common.collect.ImmutableList;
import net.firemuffin303.civilizedmobs.registry.ModBrains;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;

import java.util.List;

public class WitherSkeletonWorkerBrain {
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
    protected static final List<SensorType<? extends Sensor<? super WitherSkeletonWorkerEntity>>> SENSORS;

    protected static Brain<?> create(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity, Brain<WitherSkeletonWorkerEntity> brain){

        brain.refreshActivities(witherSkeletonWorkerEntity.getWorld().getTimeOfDay(),witherSkeletonWorkerEntity.getWorld().getTime());
        return brain;
    }

    static {
        SENSORS = List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY,
                ModBrains.WITHER_SKELETON_PLAYER_FRIENDLY

        );

        MEMORY_MODULES = ImmutableList.of(
                MemoryModuleType.HOME,
                MemoryModuleType.JOB_SITE,
                MemoryModuleType.POTENTIAL_JOB_SITE,
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.MOBS,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.NEAREST_PLAYERS,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                //Navigation
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.PATH,

                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.ANGRY_AT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,

                MemoryModuleType.NEAREST_HOSTILE,
                MemoryModuleType.LAST_WORKED_AT_POI);
    }
}
