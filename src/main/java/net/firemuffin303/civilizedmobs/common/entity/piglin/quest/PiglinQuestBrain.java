package net.firemuffin303.civilizedmobs.common.entity.piglin.quest;

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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class PiglinQuestBrain {
    protected static final List<SensorType<? extends Sensor<? super PiglinQuestEntity>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<PiglinQuestEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST;

    private static final UniformIntProvider GO_TO_ZOMBIFIED_MEMORY_DURATION;


    protected static Brain<?> create(PiglinQuestEntity piglinQuestEntity,Brain<PiglinQuestEntity> brain){
        addCoreActivities(piglinQuestEntity,brain);
        addIdleActivities(piglinQuestEntity, brain);
        addAvoidActivities(brain);
        addFightActivity(piglinQuestEntity,brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.doExclusively(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(PiglinQuestEntity piglinQuestEntity, Brain<PiglinQuestEntity> brain){
        brain.setTaskList(Activity.CORE,0,
                ImmutableList.of(
                        new LookAroundTask(45,90),
                        new WanderAroundTask(),
                        OpenDoorsTask.create(),
                        FindPointOfInterestTask.create(registryEntry -> registryEntry.matchesKey(PointOfInterestTypes.MEETING),MemoryModuleType.MEETING_POINT,true, Optional.empty()),
                        MemoryTransferTask.create(PiglinQuestBrain::getNearestZombifiedPiglin,MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,MemoryModuleType.AVOID_TARGET,GO_TO_ZOMBIFIED_MEMORY_DURATION),
                        ForgetAttackTargetTask.create()
                ));
    }



    private static void addIdleActivities(PiglinQuestEntity piglinQuestEntity,Brain<PiglinQuestEntity> brain){
        brain.setTaskList(Activity.IDLE,10,ImmutableList.of(
                new RandomTask<>(ImmutableList.of(
                        Pair.of(StrollTask.create(0.6f),2),
                        com.mojang.datafixers.util.Pair.of(FindEntityTask.create(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        com.mojang.datafixers.util.Pair.of(FindEntityTask.create(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        com.mojang.datafixers.util.Pair.of(FindEntityTask.create(ModEntityType.PIGLIN_WORKER, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(FindEntityTask.create(ModEntityType.PIGLIN_LEADER_ENTITY,8, MemoryModuleType.INTERACTION_TARGET,0.6f,2),2),
                        Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.MEETING_POINT,0.6f,2,100),2),
                        Pair.of(GoToIfNearbyTask.create(MemoryModuleType.MEETING_POINT,0.6f,5),2),
                        com.mojang.datafixers.util.Pair.of(new WaitTask(30,60),1)
                )),
                GoToRememberedPositionTask.createPosBased(MemoryModuleType.NEAREST_REPELLENT,1.0f,8,false),
                UpdateAttackTargetTask.create(PiglinQuestBrain::getPreferredTarget),
                FindInteractionTargetTask.create(EntityType.PLAYER,4)
        ));
    }

    private static void addAvoidActivities(Brain<PiglinQuestEntity> brain){
        brain.setTaskList(Activity.AVOID,10,
                ImmutableList.of(
                        GoToRememberedPositionTask.createEntityBased(MemoryModuleType.AVOID_TARGET,1.0f,12,true)
                ),MemoryModuleType.AVOID_TARGET);
    }

    private static void addFightActivity(PiglinQuestEntity piglinQuestEntity,Brain<PiglinQuestEntity> brain){
        brain.setTaskList(Activity.FIGHT,10,ImmutableList.of(
                ForgetAttackTargetTask.create(target -> !isPreferredTarget(piglinQuestEntity,target)),
                AttackTask.create(5,0.75f),
                RangedApproachTask.create(1.0f),
                MeleeAttackTask.create(20),
                ForgetTask.create(PiglinQuestBrain::getNearestZombifiedPiglin,MemoryModuleType.ATTACK_TARGET)
        ),MemoryModuleType.ATTACK_TARGET);
    }

    protected static void tickActivities(PiglinQuestEntity piglinQuestEntity){
        piglinQuestEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT,Activity.AVOID,Activity.IDLE));
    }

    private static boolean getNearestZombifiedPiglin(PiglinQuestEntity piglin) {
        Brain<PiglinQuestEntity> brain = piglin.getBrain();
        if (brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            LivingEntity livingEntity = (LivingEntity)brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
            return piglin.isInRange(livingEntity, 6.0);
        } else {
            return false;
        }
    }

    private static boolean isPreferredTarget(PiglinQuestEntity piglinQuestEntity,LivingEntity target){
        return getPreferredTarget(piglinQuestEntity).filter(preferredTarget -> {
            return  preferredTarget == target;
        }).isPresent();
    }



    private static Optional<? extends LivingEntity> getPreferredTarget(PiglinQuestEntity piglinQuestEntity){
        Brain<PiglinQuestEntity> brain = piglinQuestEntity.getBrain();
        if(getNearestZombifiedPiglin(piglinQuestEntity)){
            return Optional.empty();
        }else{
            Optional<LivingEntity> optionalLivingEntity = LookTargetUtil.getEntity(piglinQuestEntity,MemoryModuleType.ANGRY_AT);
            if(optionalLivingEntity.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(piglinQuestEntity, optionalLivingEntity.get())){
                return optionalLivingEntity;
            }

            Optional<MobEntity> nemesis = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            if(nemesis.isPresent()){
                return nemesis;
            }else{
                Optional<PlayerEntity> optionalPlayerEntity = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
                if(optionalPlayerEntity.isPresent() && Sensor.testAttackableTargetPredicate(piglinQuestEntity, optionalPlayerEntity.get())){
                    return optionalPlayerEntity;
                }
                return Optional.empty();
            }

        }
    }

    static {
        SENSORS = List.of(
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY,
                ModBrains.PIGLIN_REPELLENT,
                ModBrains.PIGLIN_ZOMBIFIED,
                ModBrains.PIGLIN_NEMESIS,
                ModBrains.PIGLIN_DETECT_GOLD_ARMOR_PLAYER
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
        GO_TO_ZOMBIFIED_MEMORY_DURATION = TimeHelper.betweenSeconds(5, 7);
    }
}
