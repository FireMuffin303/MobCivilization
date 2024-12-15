package net.firemuffin303.civilizedmobs.common.entity.witherSkelton.quest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.firemuffin303.civilizedmobs.common.entity.piglin.quest.PiglinQuestEntity;
import net.firemuffin303.civilizedmobs.common.entity.witherSkelton.worker.WitherSkeletonWorkerBrain;
import net.firemuffin303.civilizedmobs.common.entity.witherSkelton.worker.WitherSkeletonWorkerEntity;
import net.firemuffin303.civilizedmobs.registry.ModBrains;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class WitherSkeletonQuestBrain {
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
    protected static final List<SensorType<? extends Sensor<? super WitherSkeletonQuestEntity>>> SENSORS;
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<WitherSkeletonQuestEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST;

    protected static Brain<?> create(WitherSkeletonQuestEntity witherSkeleQuestEntity, Brain<WitherSkeletonQuestEntity> brain){
        addCoreActivities(witherSkeleQuestEntity,brain);
        addIdleActivities(witherSkeleQuestEntity, brain);
        addFightActivity(witherSkeleQuestEntity,brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.doExclusively(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(WitherSkeletonQuestEntity witherSkeleQuestEntity, Brain<WitherSkeletonQuestEntity> brain){
        brain.setTaskList(Activity.CORE,0,
                ImmutableList.of(
                        new LookAroundTask(45,90),
                        new WanderAroundTask(),
                        OpenDoorsTask.create(),
                        ForgetCompletedPointOfInterestTask.create(registryEntry -> registryEntry.matchesKey(PointOfInterestTypes.MEETING),MemoryModuleType.MEETING_POINT),
                        FindPointOfInterestTask.create(registryEntry -> registryEntry.matchesKey(PointOfInterestTypes.MEETING),MemoryModuleType.MEETING_POINT,true, Optional.empty()),
                        ForgetAngryAtTargetTask.create(),
                        ForgetAttackTargetTask.create()
                ));
    }

    private static void addIdleActivities(WitherSkeletonQuestEntity witherSkeleQuestEntity,Brain<WitherSkeletonQuestEntity> brain){
        brain.setTaskList(Activity.IDLE,10,ImmutableList.of(
                new RandomTask<>(ImmutableList.of(
                        Pair.of(StrollTask.create(0.6f),2),
                        com.mojang.datafixers.util.Pair.of(FindEntityTask.create(EntityType.WITHER_SKELETON, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(FindEntityTask.create(ModEntityType.WITHER_SKELETON_WORKER,8, MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.MEETING_POINT,0.6f,2,100),2),
                        Pair.of(GoToIfNearbyTask.create(MemoryModuleType.MEETING_POINT,0.6f,5),2),
                        com.mojang.datafixers.util.Pair.of(new WaitTask(30,60),1)
                )),
                FindInteractionTargetTask.create(EntityType.PLAYER,4),
                createUpdateAttackTarget()
        ));
    }

    private static void addFightActivity(WitherSkeletonQuestEntity piglinQuestEntity,Brain<WitherSkeletonQuestEntity> brain){
        brain.setTaskList(Activity.FIGHT,10,ImmutableList.of(
                ForgetAttackTargetTask.create(target -> !isPreferredTarget(piglinQuestEntity,target)),
                AttackTask.create(5,1f),
                RangedApproachTask.create(1.0f),
                MeleeAttackTask.create(20)
        ),MemoryModuleType.ATTACK_TARGET);
    }

    private static Task<WitherSkeletonQuestEntity> createUpdateAttackTarget(){
        return UpdateAttackTargetTask.create(entity -> getPreferredTarget(entity).isPresent(), WitherSkeletonQuestBrain::getPreferredTarget);
    }

    private static Optional<? extends LivingEntity> getPreferredTarget(WitherSkeletonQuestEntity witherSkeletonWorkerEntity){
        Brain<WitherSkeletonQuestEntity> brain = witherSkeletonWorkerEntity.getBrain();
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

    private static boolean isPreferredTarget(WitherSkeletonQuestEntity witherSkeletonWorkerEntity,LivingEntity target){
        return getPreferredTarget(witherSkeletonWorkerEntity).filter(preferredTarget -> {
            return  preferredTarget == target;
        }).isPresent();
    }

    protected static void tickActivities(WitherSkeletonQuestEntity witherSkeletonQuestEntity){
        witherSkeletonQuestEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT,Activity.IDLE));
    }


    static {
        SENSORS = ImmutableList.of(
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY,
                ModBrains.WITHER_SKELETON_NEMESIS
        );

        MEMORY_MODULES = ImmutableList.of(
                MemoryModuleType.HOME,
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.MOBS,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.NEAREST_PLAYERS,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                //Navigation
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.PATH,
                //Piglin
                MemoryModuleType.AVOID_TARGET,
                MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
                MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,
                MemoryModuleType.NEAREST_REPELLENT,
                MemoryModuleType.NEAREST_VISIBLE_NEMESIS,

                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.ANGRY_AT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,

                MemoryModuleType.MEETING_POINT
        );

        POINTS_OF_INTEREST = ImmutableMap.of(MemoryModuleType.MEETING_POINT,(piglinQuestEntity, registryEntry) -> registryEntry.matchesKey(PointOfInterestTypes.MEETING));
    }
}
