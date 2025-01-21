package net.firemuffin303.villagefoe.common.entity.witherSkelton.worker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.firemuffin303.villagefoe.common.entity.task.*;
import net.firemuffin303.villagefoe.registry.ModBrains;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.firemuffin303.villagefoe.registry.ModTags;
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

public class WitherSkeletonWorkerBrain {
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
    protected static final List<SensorType<? extends Sensor<? super WitherSkeletonWorkerEntity>>> SENSORS;

    protected static Brain<?> create(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity, Brain<WitherSkeletonWorkerEntity> brain){
        addCoreActivities(witherSkeletonWorkerEntity,brain);
        addIdleActivities(witherSkeletonWorkerEntity,brain);
        addWorkActivities(witherSkeletonWorkerEntity,brain);
        addFightActivity(witherSkeletonWorkerEntity,brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.doExclusively(Activity.IDLE);
        brain.refreshActivities(witherSkeletonWorkerEntity.getWorld().getTimeOfDay(),witherSkeletonWorkerEntity.getWorld().getTime());
        return brain;
    }

    private static void addCoreActivities(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity,Brain<WitherSkeletonWorkerEntity> brain){
        VillagerProfession villagerProfession = witherSkeletonWorkerEntity.getWorkerData().getProfession();
        brain.setTaskList(Activity.CORE,0,
                ImmutableList.of(
                        new LookAroundTask(45, 90),
                        new WanderAroundTask(),
                        OpenDoorsTask.create(),
                        ForgetCompletedPointOfInterestTask.create(villagerProfession.heldWorkstation(),MemoryModuleType.JOB_SITE),
                        ForgetCompletedPointOfInterestTask.create(villagerProfession.acquirableWorkstation(),MemoryModuleType.POTENTIAL_JOB_SITE),

                        FindPointOfInterestTask.create(villagerProfession == VillagerProfession.NONE ? registryEntry -> registryEntry.isIn(ModTags.WITHER_SKELETON_ACQUIRABLE_JOB_SITE) : villagerProfession.acquirableWorkstation(),MemoryModuleType.JOB_SITE,MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty()),
                        new ModWalkTowardJobsiteTask(0.75f),
                        WorkerGoToWorkTask.create(),
                        ModLoseOnSiteLossTask.create(),

                        ForgetAngryAtTargetTask.create(),
                        ForgetAttackTargetTask.create()
                )
        );
    }

    private static void addIdleActivities(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity,Brain<WitherSkeletonWorkerEntity> brain){
        brain.setTaskList(Activity.IDLE,10,ImmutableList.of(
                new RandomTask<>(ImmutableList.of(
                        Pair.of(StrollTask.create(0.6F), 2),
                        Pair.of(FindEntityTask.create(EntityType.WITHER_SKELETON, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(FindEntityTask.create(ModEntityType.WITHER_SKELETON_WORKER, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(new NeedLeaderTask<>(EntityType.WITHER_SKELETON,ModEntityType.WITHER_SKELETON_WORKER),2),
                        Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE, 0.6F, 2, 100), 2),
                        Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE, 0.6F, 5), 2),
                        Pair.of(new WaitTask(30, 60), 1))
                    ),
                createUpdateAttackTarget()
                )
        );
    }

    private static void addWorkActivities(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity,Brain<WitherSkeletonWorkerEntity> brain){
        brain.setTaskList(Activity.WORK,
                ImmutableList.of(
                        Pair.of(5,new RandomTask<>(
                                        ImmutableList.of(
                                                Pair.of(new ModWorkTask(),7),
                                                Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE,0.4f,4),2),
                                                Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE,0.8f,1,32),5)
                                        )
                                )
                        ),
                        Pair.of(10,createUpdateAttackTarget())
                ),
                ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT))
        );
    }

    private static void addFightActivity(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity,Brain<WitherSkeletonWorkerEntity> brain){
        brain.setTaskList(Activity.FIGHT,10,ImmutableList.of(
                ForgetAttackTargetTask.create(target -> !isPreferredTarget(witherSkeletonWorkerEntity,target)),
                AttackTask.create(5,1f),
                RangedApproachTask.create(1.2f),
                MeleeAttackTask.create(20)
        ),MemoryModuleType.ATTACK_TARGET);
    }

    protected static void tickActivities(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity) {
        witherSkeletonWorkerEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT,Activity.WORK,Activity.IDLE));
    }

    private static Optional<? extends LivingEntity> getPreferredTarget(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity){
        Brain<WitherSkeletonWorkerEntity> brain = witherSkeletonWorkerEntity.getBrain();
        Optional<LivingEntity> optionalLivingEntity = LookTargetUtil.getEntity(witherSkeletonWorkerEntity,MemoryModuleType.ANGRY_AT);
        if(optionalLivingEntity.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(witherSkeletonWorkerEntity, optionalLivingEntity.get())){
            return optionalLivingEntity;
        }
        Optional<LivingEntity> optionalHostile = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_HOSTILE);

        if(optionalHostile.isPresent()){
            return optionalHostile;
        }

        return Optional.empty();
    }

    private static boolean isPreferredTarget(WitherSkeletonWorkerEntity witherSkeletonWorkerEntity,LivingEntity target){
        return getPreferredTarget(witherSkeletonWorkerEntity).filter(preferredTarget -> {
            return  preferredTarget == target;
        }).isPresent();
    }

    private static Task<WitherSkeletonWorkerEntity> createUpdateAttackTarget(){
        return UpdateAttackTargetTask.create(entity -> getPreferredTarget(entity).isPresent(), WitherSkeletonWorkerBrain::getPreferredTarget);
    }

    static {
        SENSORS = List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY,
                ModBrains.WITHER_SKELETON_NEMESIS,
                ModBrains.WITHER_SKELETON_LEADER_LAST_SEEN

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
                MemoryModuleType.LAST_WORKED_AT_POI,
                ModBrains.LEADER_DETECTED_RECENTLY);
    }
}
