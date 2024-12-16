package net.firemuffin303.civilizedmobs.common.entity.pillager.quest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class PillagerQuestBrain {
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
    protected static final List<SensorType<? extends Sensor<? super PillagerQuestEntity>>> SENSORS;
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<PillagerQuestEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST;

    protected static Brain<?> create(PillagerQuestEntity pillagerQuestEntity, Brain<PillagerQuestEntity> brain){
        addCoreActivities(pillagerQuestEntity,brain);
        addIdleActivities(pillagerQuestEntity, brain);
        addFightActivity(pillagerQuestEntity,brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.doExclusively(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(PillagerQuestEntity pillagerQuestEntity, Brain<PillagerQuestEntity> brain){
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

    private static void addIdleActivities(PillagerQuestEntity pillagerQuestEntity,Brain<PillagerQuestEntity> brain){
        brain.setTaskList(Activity.IDLE,10,ImmutableList.of(
                new RandomTask<>(ImmutableList.of(
                        Pair.of(StrollTask.create(0.6f),2),
                        Pair.of(FindEntityTask.create(EntityType.PILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(FindEntityTask.create(EntityType.VINDICATOR,8, MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(FindEntityTask.create(EntityType.EVOKER,8, MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(FindEntityTask.create(ModEntityType.PILLAGER_WORKER,8, MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(FindEntityTask.create(ModEntityType.PILLAGER_LEADER,8, MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.MEETING_POINT,0.6f,2,100),2),
                        Pair.of(GoToIfNearbyTask.create(MemoryModuleType.MEETING_POINT,0.6f,5),2),
                        com.mojang.datafixers.util.Pair.of(new WaitTask(30,60),1)
                )),
                FindInteractionTargetTask.create(EntityType.PLAYER,4),
                createUpdateAttackTarget()
        ));
    }

    private static void addFightActivity(PillagerQuestEntity piglinQuestEntity,Brain<PillagerQuestEntity> brain){
        brain.setTaskList(Activity.FIGHT,10,ImmutableList.of(
                ForgetAttackTargetTask.create(target -> !isPreferredTarget(piglinQuestEntity,target)),
                TaskTriggerer.runIf((world, entity, time) -> entity.isHolding(Items.CROSSBOW),AttackTask.create(5,0.75f)),
                RangedApproachTask.create(1.0f),
                MeleeAttackTask.create(20),
                new CrossbowAttackTask<>()
        ),MemoryModuleType.ATTACK_TARGET);
    }

    private static Task<PillagerQuestEntity> createUpdateAttackTarget(){
        return UpdateAttackTargetTask.create(entity -> getPreferredTarget(entity).isPresent(), PillagerQuestBrain::getPreferredTarget);
    }

    private static Optional<? extends LivingEntity> getPreferredTarget(PillagerQuestEntity pillagerQuestEntity){
        Brain<PillagerQuestEntity> brain = pillagerQuestEntity.getBrain();
        Optional<LivingEntity> optionalLivingEntity = LookTargetUtil.getEntity(pillagerQuestEntity,MemoryModuleType.ANGRY_AT);
        if(optionalLivingEntity.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(pillagerQuestEntity, optionalLivingEntity.get())){
            return optionalLivingEntity;
        }
        Optional<LivingEntity> optionalHurtBy = brain.getOptionalRegisteredMemory(MemoryModuleType.HURT_BY_ENTITY);
        if(optionalHurtBy.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(pillagerQuestEntity, optionalHurtBy.get())){
            return optionalHurtBy;
        }

        Optional<LivingEntity> optionalHostile = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_HOSTILE);

        if(optionalHostile.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(pillagerQuestEntity, optionalHostile.get())){
            return optionalHostile;
        }

        return Optional.empty();
    }

    private static boolean isPreferredTarget(PillagerQuestEntity witherSkeletonWorkerEntity,LivingEntity target){
        return getPreferredTarget(witherSkeletonWorkerEntity).filter(preferredTarget -> {
            return  preferredTarget == target;
        }).isPresent();
    }

    protected static void tickActivities(PillagerQuestEntity pillagerQuestEntity){
        pillagerQuestEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT,Activity.IDLE));
    }

    static {
        SENSORS = ImmutableList.of(
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY,
                ModBrains.ILLAGER_HOSTILE
        );

        MEMORY_MODULES = ImmutableList.of(
                MemoryModuleType.HOME,
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.MOBS,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleType.NEAREST_PLAYERS,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                MemoryModuleType.HURT_BY,
                MemoryModuleType.HURT_BY_ENTITY,
                //Navigation
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.PATH,

                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.ANGRY_AT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,

                MemoryModuleType.MEETING_POINT
        );

        POINTS_OF_INTEREST = ImmutableMap.of(MemoryModuleType.MEETING_POINT,(pillagerQuestEntity, registryEntry) -> registryEntry.matchesKey(PointOfInterestTypes.MEETING));
    }
}
