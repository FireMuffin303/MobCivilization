package net.firemuffin303.civilizedmobs.common.entity.piglin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.List;

public class PiglinQuestBrain {
    protected static final List<SensorType<? extends Sensor<? super PiglinQuestEntity>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
    private static final UniformIntProvider GO_TO_ZOMBIFIED_MEMORY_DURATION;


    protected static Brain<?> create(PiglinQuestEntity piglinQuestEntity,Brain<PiglinQuestEntity> brain){
        addCoreActivities(piglinQuestEntity,brain);
        addIdleActivities(piglinQuestEntity, brain);
        addAvoidActivities(brain);
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
                        MemoryTransferTask.create(PiglinQuestBrain::getNearestZombifiedPiglin,MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,MemoryModuleType.AVOID_TARGET,GO_TO_ZOMBIFIED_MEMORY_DURATION)
                ));
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

    private static void addIdleActivities(PiglinQuestEntity piglinQuestEntity,Brain<PiglinQuestEntity> brain){
        brain.setTaskList(Activity.IDLE,10,ImmutableList.of(
                new RandomTask<>(ImmutableList.of(
                        Pair.of(StrollTask.create(0.6f),2),
                        com.mojang.datafixers.util.Pair.of(FindEntityTask.create(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        com.mojang.datafixers.util.Pair.of(FindEntityTask.create(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        com.mojang.datafixers.util.Pair.of(FindEntityTask.create(ModEntityType.CIVIL_PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        com.mojang.datafixers.util.Pair.of(new WaitTask(30,60),1)
                )),
                FindInteractionTargetTask.create(EntityType.PLAYER,4)
        ));
    }

    private static void addAvoidActivities(Brain<PiglinQuestEntity> brain){
        brain.setTaskList(Activity.AVOID,10,
                ImmutableList.of(
                        GoToRememberedPositionTask.createEntityBased(MemoryModuleType.AVOID_TARGET,1.0f,12,true)
                ),MemoryModuleType.AVOID_TARGET);
    }

    protected static void tickActivities(PiglinQuestEntity piglinQuestEntity){
        piglinQuestEntity.getBrain().resetPossibleActivities(ImmutableList.of(Activity.AVOID,Activity.IDLE));
    }

    static {
        SENSORS = List.of(
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY
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

                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.ANGRY_AT,
                MemoryModuleType.ATTACK_TARGET
        );

        GO_TO_ZOMBIFIED_MEMORY_DURATION = TimeHelper.betweenSeconds(5, 7);
    }
}
