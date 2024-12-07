package net.firemuffin303.civilizedmobs.common.entity.pillager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.ModPanicTask;
import net.firemuffin303.civilizedmobs.common.entity.task.*;
import net.firemuffin303.civilizedmobs.registry.ModBrains;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.firemuffin303.civilizedmobs.registry.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.List;
import java.util.Optional;

public class PillagerWorkerBrain {
    protected static final List<SensorType<? extends Sensor<? super PillagerWorkerEntity>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;

    protected static Brain<?> create(PillagerWorkerEntity pillagerWorkerEntity,Brain<PillagerWorkerEntity> brain){
        addCoreActivities(pillagerWorkerEntity,brain);
        addIdleActivities(pillagerWorkerEntity,brain);
        addWorkActivities(pillagerWorkerEntity,brain);
        addPanicActivities(pillagerWorkerEntity,brain);
        addRestActivities(pillagerWorkerEntity,brain);

        brain.setSchedule(ModBrains.PILLAGER_WORKER_DEFAULT);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.doExclusively(Activity.IDLE);
        brain.refreshActivities(pillagerWorkerEntity.getWorld().getTimeOfDay(),pillagerWorkerEntity.getWorld().getTime());
        return brain;
    }

    private static void addCoreActivities(PillagerWorkerEntity pillagerWorkerEntity,Brain<PillagerWorkerEntity> brain){
        VillagerProfession villagerProfession = pillagerWorkerEntity.getWorkerData().getProfession();

        brain.setTaskList(Activity.CORE,0, ImmutableList.of(
                new StayAboveWaterTask(0.8f),
                new LookAroundTask(45,90),
                new ModPanicTask(),
                new WanderAroundTask(),
                WakeUpTask.create(),
                OpenDoorsTask.create(),
                ForgetCompletedPointOfInterestTask.create(villagerProfession.heldWorkstation(),MemoryModuleType.JOB_SITE),
                ForgetCompletedPointOfInterestTask.create(villagerProfession.acquirableWorkstation(),MemoryModuleType.POTENTIAL_JOB_SITE),
                FindPointOfInterestTask.create(villagerProfession == VillagerProfession.NONE ? registryEntry ->{
                    return registryEntry.isIn(ModTags.ILLAGER_ACQUIRABLE_JOB_SITE);
                }  : villagerProfession.acquirableWorkstation(),MemoryModuleType.JOB_SITE,MemoryModuleType.POTENTIAL_JOB_SITE, true,Optional.empty()),
                FindPointOfInterestTask.create(registryEntry -> {
                    return registryEntry.matchesKey(PointOfInterestTypes.HOME);
                },MemoryModuleType.HOME,true,Optional.of((byte)14)),
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
                FindInteractionTargetTask.create(EntityType.PLAYER,4),
                ScheduleActivityTask.create()
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

    private static void addPanicActivities(PillagerWorkerEntity pillagerWorkerEntity,Brain<PillagerWorkerEntity> brain){
        brain.setTaskList(Activity.PANIC,ImmutableList.of(
                Pair.of(0,StopPanickingTask.create()),
                Pair.of(1,GoToRememberedPositionTask.createEntityBased(MemoryModuleType.NEAREST_HOSTILE,0.75f,6,false)),
                Pair.of(1, GoToRememberedPositionTask.createEntityBased(MemoryModuleType.HURT_BY_ENTITY, 0.75f, 6, false)),
                Pair.of(3, FindWalkTargetTask.create(1.5f, 2, 2))
                ));
    }

    private static void addRestActivities(PillagerWorkerEntity pillagerWorkerEntity, Brain<PillagerWorkerEntity> brain){
        brain.setTaskList(Activity.REST,ImmutableList.of(
                Pair.of(2, ModWalkTowardTask.create(MemoryModuleType.HOME,0.6f,1,150,1200)),
                Pair.of(3,ForgetCompletedPointOfInterestTask.create((poiType) ->{
                    return poiType.matchesKey(PointOfInterestTypes.HOME);
                },MemoryModuleType.HOME)),
                Pair.of(3,new SleepTask()),
                Pair.of(4,new RandomTask<>(ImmutableMap.of(MemoryModuleType.HOME,MemoryModuleState.VALUE_ABSENT),ImmutableList.of(
                        Pair.of(WalkHomeTask.create(0.6f),1),
                        Pair.of(new WaitTask(20,40),2)
                ))),
                Pair.of(99,ScheduleActivityTask.create())

        ));
    }

    protected static void tickActivities(PillagerWorkerEntity pillagerWorkerEntity){
        pillagerWorkerEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.REST,Activity.WORK,Activity.IDLE));
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
                //Work
                MemoryModuleType.JOB_SITE,
                MemoryModuleType.POTENTIAL_JOB_SITE,
                MemoryModuleType.LAST_WORKED_AT_POI,
                //Sleep
                MemoryModuleType.LAST_SLEPT,
                MemoryModuleType.LAST_WOKEN,

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
