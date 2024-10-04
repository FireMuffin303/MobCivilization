package net.firemuffin303.civilizedmobs.common.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.task.WorkerGoToWorkTask;
import net.firemuffin303.civilizedmobs.common.entity.task.ModWalkTowardJobsiteTask;
import net.firemuffin303.civilizedmobs.registry.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class CivilPiglinBrain {
    protected static final List<SensorType<? extends Sensor<? super WorkerPiglinEntity>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<WorkerPiglinEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST;


    protected static Brain<?> create(WorkerPiglinEntity workerPiglinEntity, Brain<WorkerPiglinEntity> brain) {
        addCoreActivities(workerPiglinEntity, brain);
        addIdleActivities(workerPiglinEntity, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(WorkerPiglinEntity workerPiglinEntity, Brain<WorkerPiglinEntity> brain) {
        VillagerProfession villagerProfession = workerPiglinEntity.getWorkerData().getProfession();

        brain.setTaskList(Activity.CORE, 0,
                ImmutableList.of(new LookAroundTask(45, 90),
                        new WanderAroundTask(),
                        OpenDoorsTask.create(),
                        //ForgetCompletedPointOfInterestTask.create(civilziedProfession.heldWorkstation(),MemoryModuleType.JOB_SITE),
                        //ForgetCompletedPointOfInterestTask.create(civilziedProfession.acquirableWorkstation(),MemoryModuleType.POTENTIAL_JOB_SITE),

                        FindPointOfInterestTask.create(villagerProfession == VillagerProfession.NONE ? registryEntry -> registryEntry.isIn(ModTags.PIGLIN_ACQUIRABLE_JOB_SITE) : villagerProfession.acquirableWorkstation(),MemoryModuleType.JOB_SITE,MemoryModuleType.POTENTIAL_JOB_SITE, true,Optional.empty()),
                        new ModWalkTowardJobsiteTask(0.5f),
                        WorkerGoToWorkTask.create(),
                        //ModLoseOnSiteLossTask.create(),
                        ForgetAngryAtTargetTask.create()));
    }

    private static void addIdleActivities(WorkerPiglinEntity workerPiglinEntity, Brain<WorkerPiglinEntity> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(getIdleTasks(), FindInteractionTargetTask.create(EntityType.PLAYER, 4)));
    }

    private static void addWorkActivities(Brain<WorkerPiglinEntity> civilizedPiglinEntityBrain){
        civilizedPiglinEntityBrain.setTaskList(Activity.WORK,10,
                ImmutableList.of()
        );
    }

    private static RandomTask<WorkerPiglinEntity> getIdleTasks() {
        return new RandomTask(ImmutableList.of(
                        Pair.of(StrollTask.create(0.6F), 2),
                        Pair.of(FindEntityTask.create(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                Pair.of(FindEntityTask.create(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE, 0.6F, 2, 100), 2),
                Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE, 0.6F, 5), 2),
                Pair.of(new WaitTask(30, 60), 1)));
    }

    protected static void tickActivities(WorkerPiglinEntity piglin) {
        piglin.getBrain().resetPossibleActivities(ImmutableList.of(Activity.IDLE));
    }

    static {
        SENSORS = List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY);
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

                MemoryModuleType.LAST_WORKED_AT_POI);

        POINTS_OF_INTEREST = ImmutableMap.of(MemoryModuleType.JOB_SITE,(civilizedPiglinEntity, registryEntry) -> {
            return civilizedPiglinEntity.getWorkerData().getProfession().heldWorkstation().test(registryEntry);
        },MemoryModuleType.POTENTIAL_JOB_SITE,(workerPiglinEntity, registryEntry) -> {
            return VillagerProfession.IS_ACQUIRABLE_JOB_SITE.test(registryEntry);
        });
    }
}
