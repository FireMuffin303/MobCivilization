package net.firemuffin303.civilizedmobs.common.entity.pillager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.common.entity.task.ModLoseOnSiteLossTask;
import net.firemuffin303.civilizedmobs.common.entity.task.ModWalkTowardJobsiteTask;
import net.firemuffin303.civilizedmobs.common.entity.task.ModWorkTask;
import net.firemuffin303.civilizedmobs.common.entity.task.WorkerGoToWorkTask;
import net.firemuffin303.civilizedmobs.registry.ModBrains;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.firemuffin303.civilizedmobs.registry.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.village.VillagerProfession;

import java.util.List;
import java.util.Optional;

public class PillagerWorkerBrain {
    protected static final List<SensorType<? extends Sensor<? super PillagerWorkerEntity>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;

    protected static Brain<?> create(PillagerWorkerEntity pillagerWorkerEntity,Brain<PillagerWorkerEntity> brain){

        addCoreActivities(pillagerWorkerEntity,brain);
        addIdleActivities(pillagerWorkerEntity,brain);
        addWorkActivities(pillagerWorkerEntity,brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.doExclusively(Activity.IDLE);
        brain.refreshActivities(pillagerWorkerEntity.getWorld().getTimeOfDay(),pillagerWorkerEntity.getWorld().getTime());
        return brain;
    }

    private static void addCoreActivities(PillagerWorkerEntity pillagerWorkerEntity,Brain<PillagerWorkerEntity> brain){
        VillagerProfession villagerProfession = pillagerWorkerEntity.getWorkerData().getProfession();

        brain.setTaskList(Activity.CORE,0, ImmutableList.of(
                new LookAroundTask(45,90),
                new WanderAroundTask(),
                OpenDoorsTask.create(),
                ForgetCompletedPointOfInterestTask.create(villagerProfession.heldWorkstation(),MemoryModuleType.JOB_SITE),
                ForgetCompletedPointOfInterestTask.create(villagerProfession.acquirableWorkstation(),MemoryModuleType.POTENTIAL_JOB_SITE),
                FindPointOfInterestTask.create(villagerProfession == VillagerProfession.NONE ? registryEntry ->{
                    return registryEntry.isIn(ModTags.PIGLIN_ACQUIRABLE_JOB_SITE);
                }  : villagerProfession.acquirableWorkstation(),MemoryModuleType.JOB_SITE,MemoryModuleType.POTENTIAL_JOB_SITE, true,Optional.empty()),
                new ModWalkTowardJobsiteTask(0.75f),
                WorkerGoToWorkTask.create(),
                ModLoseOnSiteLossTask.create()
        ));
    }

    private static void addIdleActivities(PillagerWorkerEntity pillagerWorkerEntity, Brain<PillagerWorkerEntity> brain) {
        brain.setTaskList(Activity.IDLE,10,ImmutableList.of(
                new RandomTask<>(ImmutableList.of(
                        Pair.of(StrollTask.create(0.6f),2),
                        Pair.of(FindEntityTask.create(EntityType.PILLAGER,8,MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(FindEntityTask.create(EntityType.VINDICATOR,8,MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(FindEntityTask.create(ModEntityType.PILLAGER_WORKER,8,MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE,0.6f,2,100),2),
                        Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE, 0.6F, 5), 2),
                        Pair.of(new WaitTask(30,60),1)
                )),
                FindInteractionTargetTask.create(EntityType.PLAYER,4)
        ));
    }

    private static void addWorkActivities(PillagerWorkerEntity pillagerWorkerEntity,Brain<PillagerWorkerEntity> brain){
        brain.setTaskList(Activity.WORK,
                ImmutableList.of(
                        Pair.of(5, new RandomTask<>(ImmutableList.of(
                                Pair.of(new ModWorkTask(),7),
                                Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE,0.4f,4),2),
                                Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE,0.8f,1,32),5)
                        ))),
                        Pair.of(10,FindInteractionTargetTask.create(EntityType.PLAYER,4)),
                        Pair.of(99,ScheduleActivityTask.create())
                ),ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT)));
    }

    protected static void tickActivities(PillagerWorkerEntity pillagerWorkerEntity){
        pillagerWorkerEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.WORK,Activity.IDLE));
    }

    static {
        SENSORS = List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.HURT_BY,
                ModBrains.ILLAGER_HOSTILE
        );

        MEMORY_MODULES = ImmutableList.of(
                //VILLAGE
                MemoryModuleType.HOME,
                MemoryModuleType.JOB_SITE,
                MemoryModuleType.POTENTIAL_JOB_SITE,
                MemoryModuleType.LAST_WORKED_AT_POI,
                MemoryModuleType.AVOID_TARGET,

                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.MOBS,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.NEAREST_PLAYERS,
                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.NEAREST_HOSTILE,

                //Navigation
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.PATH
        );
    }
}
