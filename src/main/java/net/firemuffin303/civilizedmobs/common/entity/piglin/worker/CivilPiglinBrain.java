package net.firemuffin303.civilizedmobs.common.entity.piglin.worker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.firemuffin303.civilizedmobs.common.entity.task.ModLoseOnSiteLossTask;
import net.firemuffin303.civilizedmobs.common.entity.task.ModWorkTask;
import net.firemuffin303.civilizedmobs.common.entity.task.WorkerGoToWorkTask;
import net.firemuffin303.civilizedmobs.common.entity.task.ModWalkTowardJobsiteTask;
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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class CivilPiglinBrain {
    protected static final List<SensorType<? extends Sensor<? super WorkerPiglinEntity>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<WorkerPiglinEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST;
    private static final UniformIntProvider GO_TO_ZOMBIFIED_MEMORY_DURATION;
    private static final UniformIntProvider GO_TO_NEMESIS_MEMORY_DURATION;
    private static final UniformIntProvider GO_TO_NON_GOLD_ARMOR_PLAYER_MEMORY_DURATION = UniformIntProvider.create(5,8);


    protected static Brain<?> create(WorkerPiglinEntity workerPiglinEntity, Brain<WorkerPiglinEntity> brain) {
        addCoreActivities(workerPiglinEntity, brain);
        addIdleActivities(workerPiglinEntity, brain);
        addWorkActivities(workerPiglinEntity,brain);
        addAvoidActivity(brain);
        addFightActivity(workerPiglinEntity, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.doExclusively(Activity.IDLE);
        brain.refreshActivities(workerPiglinEntity.getWorld().getTimeOfDay(),workerPiglinEntity.getWorld().getTime());
        return brain;
    }


    private static void addCoreActivities(WorkerPiglinEntity workerPiglinEntity, Brain<WorkerPiglinEntity> brain) {
        VillagerProfession villagerProfession = workerPiglinEntity.getWorkerData().getProfession();

        brain.setTaskList(Activity.CORE, 0,
                ImmutableList.of(new LookAroundTask(45, 90),
                        new WanderAroundTask(),
                        OpenDoorsTask.create(),
                        ForgetCompletedPointOfInterestTask.create(villagerProfession.heldWorkstation(),MemoryModuleType.JOB_SITE),
                        ForgetCompletedPointOfInterestTask.create(villagerProfession.acquirableWorkstation(),MemoryModuleType.POTENTIAL_JOB_SITE),

                        FindPointOfInterestTask.create(villagerProfession == VillagerProfession.NONE ? registryEntry -> registryEntry.isIn(ModTags.PIGLIN_ACQUIRABLE_JOB_SITE) : villagerProfession.acquirableWorkstation(),MemoryModuleType.JOB_SITE,MemoryModuleType.POTENTIAL_JOB_SITE, true,Optional.empty()),
                        new ModWalkTowardJobsiteTask(0.75f),
                        WorkerGoToWorkTask.create(),
                        ModLoseOnSiteLossTask.create(),
                        ForgetAngryAtTargetTask.create(),
                        MemoryTransferTask.create(CivilPiglinBrain::getNearestZombifiedPiglin,MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,MemoryModuleType.AVOID_TARGET,GO_TO_ZOMBIFIED_MEMORY_DURATION),
                        //MemoryTransferTask.create(workerPiglinEntity1 -> !workerPiglinEntity1.isHolding(Items.GOLDEN_SWORD) && getNemesis(workerPiglinEntity1),MemoryModuleType.NEAREST_VISIBLE_NEMESIS,MemoryModuleType.AVOID_TARGET,GO_TO_NEMESIS_MEMORY_DURATION),
                        ForgetAttackTargetTask.create()
                ));
    }

    private static boolean getNearestZombifiedPiglin(WorkerPiglinEntity piglin) {
        Brain<WorkerPiglinEntity> brain = piglin.getBrain();
        if (brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            LivingEntity livingEntity = (LivingEntity)brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
            return piglin.isInRange(livingEntity, 6.0);
        } else {
            return false;
        }
    }

    private static void addIdleActivities(WorkerPiglinEntity workerPiglinEntity, Brain<WorkerPiglinEntity> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(
                new RandomTask<>(ImmutableList.of(
                        Pair.of(StrollTask.create(0.6F), 2),
                        Pair.of(FindEntityTask.create(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(FindEntityTask.create(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(FindEntityTask.create(ModEntityType.CIVIL_PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(FindEntityTask.create(ModEntityType.PIGLIN_QUEST_ENTITY, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE, 0.6F, 2, 100), 2),
                        Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE, 0.6F, 5), 2),
                        Pair.of(new WaitTask(30, 60), 1))),
                GoToRememberedPositionTask.createPosBased(MemoryModuleType.NEAREST_REPELLENT,1.0f,8,false),
                createUpdateAttackTarget(),
                createAvoidNonPlayerGoldArmor(),
                createAvoidNemesisTask(),

                FindInteractionTargetTask.create(EntityType.PLAYER, 4)));
    }

    private static void addWorkActivities(WorkerPiglinEntity workerPiglinEntity,Brain<WorkerPiglinEntity> civilizedPiglinEntityBrain){
        civilizedPiglinEntityBrain.setTaskList(Activity.WORK,
                ImmutableList.of(
                        Pair.of(5,new RandomTask<>(
                                ImmutableList.of(
                                        Pair.of(new ModWorkTask(),7),
                                        Pair.of(GoToIfNearbyTask.create(MemoryModuleType.JOB_SITE,0.4f,4),2),
                                        Pair.of(GoToNearbyPositionTask.create(MemoryModuleType.JOB_SITE,0.8f,1,32),5)

                                        )
                            )
                        ),
                        Pair.of(10,GoToRememberedPositionTask.createPosBased(MemoryModuleType.NEAREST_REPELLENT,1.0f,8,false)),
                        Pair.of(10,FindInteractionTargetTask.create(EntityType.PLAYER,4)),
                        Pair.of(10,createAvoidNonPlayerGoldArmor()),
                        Pair.of(10,createUpdateAttackTarget()),
                        Pair.of(10,createAvoidNemesisTask()),
                        Pair.of(99,ScheduleActivityTask.create())
                ),
                ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT))
        );
    }

    private static void addAvoidActivity(Brain<WorkerPiglinEntity> workerPiglinEntityBrain){
        workerPiglinEntityBrain.setTaskList(Activity.AVOID,10,
                ImmutableList.of(
                        GoToRememberedPositionTask.createEntityBased(MemoryModuleType.AVOID_TARGET,1.0f,12,true)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static void addFightActivity(WorkerPiglinEntity workerPiglinEntity,Brain<WorkerPiglinEntity> workerPiglinEntityBrain){
        workerPiglinEntityBrain.setTaskList(Activity.FIGHT,10,ImmutableList.of(
                ForgetAttackTargetTask.create(target -> !isPreferredTarget(workerPiglinEntity,target)),
                AttackTask.create(5,0.75f),
                RangedApproachTask.create(1.0f),
                MeleeAttackTask.create(20),
                ForgetTask.create(CivilPiglinBrain::getNearestZombifiedPiglin,MemoryModuleType.ATTACK_TARGET)
        ),MemoryModuleType.ATTACK_TARGET);
    }

    protected static void tickActivities(WorkerPiglinEntity piglin) {
        piglin.getBrain().resetPossibleActivities(ImmutableList.of(Activity.FIGHT,Activity.AVOID,Activity.WORK,Activity.IDLE));
    }

    private static boolean isOutofRangeFromJobSite(WorkerPiglinEntity workerPiglinEntity,Brain<WorkerPiglinEntity> workerPiglinEntityBrain){
        if(workerPiglinEntityBrain.hasMemoryModule(MemoryModuleType.JOB_SITE)){
            return !workerPiglinEntity.getPos().isInRange(workerPiglinEntityBrain.getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).get().getPos().toCenterPos(), 4);
        }
        return false;
    }

    private static Task<WorkerPiglinEntity> createAvoidNonPlayerGoldArmor(){
        return MemoryTransferTask.create( workerPiglinEntity1 -> getPlayerNotWearingGold(workerPiglinEntity1) && !workerPiglinEntity1.isHolding(Items.GOLDEN_SWORD) ,MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,MemoryModuleType.AVOID_TARGET,GO_TO_NON_GOLD_ARMOR_PLAYER_MEMORY_DURATION);
    }

    private static Task<WorkerPiglinEntity> createUpdateAttackTarget(){
        return UpdateAttackTargetTask.create(workerPiglinEntity1 -> workerPiglinEntity1.isHolding(Items.GOLDEN_SWORD) && getPreferredTarget(workerPiglinEntity1).isPresent(),CivilPiglinBrain::getPreferredTarget);
    }

    private static Task<WorkerPiglinEntity> createAvoidNemesisTask(){
        return MemoryTransferTask.create( workerPiglinEntity1 -> {
            Brain<WorkerPiglinEntity> brain1 =  workerPiglinEntity1.getBrain();
            if(brain1.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_NEMESIS)){
                LivingEntity livingEntity = brain1.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS).get();
                return workerPiglinEntity1.isInRange(livingEntity,6.0) && !workerPiglinEntity1.isHolding(Items.GOLDEN_SWORD);
            }
            return false;
        }  ,MemoryModuleType.NEAREST_VISIBLE_NEMESIS,MemoryModuleType.AVOID_TARGET,GO_TO_NEMESIS_MEMORY_DURATION);
    }

    private static boolean getPlayerNotWearingGold(WorkerPiglinEntity piglin) {
        Brain<WorkerPiglinEntity> brain = piglin.getBrain();
        if (brain.hasMemoryModule(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD)) {
            LivingEntity livingEntity = (LivingEntity)brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD).get();
            return piglin.isInRange(livingEntity, 6.0);
        } else {
            return false;
        }
    }

    private static boolean getNemesis(WorkerPiglinEntity piglin) {
        Brain<WorkerPiglinEntity> brain = piglin.getBrain();
        if (brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_NEMESIS)) {
            LivingEntity livingEntity = (LivingEntity)brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS).get();
            return piglin.isInRange(livingEntity, 6.0);
        } else {
            return false;
        }
    }

    private static boolean isPreferredTarget(WorkerPiglinEntity workerPiglinEntity,LivingEntity target){
        return getPreferredTarget(workerPiglinEntity).filter(preferredTarget -> {
            return  preferredTarget == target;
        }).isPresent();
    }

    private static Optional<? extends LivingEntity> getPreferredTarget(WorkerPiglinEntity workerPiglinEntity){
        Brain<WorkerPiglinEntity> brain = workerPiglinEntity.getBrain();
        if(getNearestZombifiedPiglin(workerPiglinEntity)){
            return Optional.empty();
        }else{
            Optional<LivingEntity> optionalLivingEntity = LookTargetUtil.getEntity(workerPiglinEntity,MemoryModuleType.ANGRY_AT);
            if(optionalLivingEntity.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(workerPiglinEntity, optionalLivingEntity.get())){
                return optionalLivingEntity;
            }

            Optional<MobEntity> nemesis = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            if(nemesis.isPresent()){
                return nemesis;
            }else{
                Optional<PlayerEntity> optionalPlayerEntity = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
                if(optionalPlayerEntity.isPresent() && Sensor.testAttackableTargetPredicate(workerPiglinEntity, optionalPlayerEntity.get())){
                    return optionalPlayerEntity;
                }
                return Optional.empty();
            }

        }
    }



    static {
        SENSORS = List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY,
                ModBrains.PIGLIN_REPELLENT,
                ModBrains.PIGLIN_DETECT_GOLD_ARMOR_PLAYER,
                ModBrains.PIGLIN_ZOMBIFIED,
                ModBrains.PIGLIN_NEMESIS
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
                //---Piglin---
                MemoryModuleType.NEAREST_REPELLENT,
                MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,
                MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
                MemoryModuleType.AVOID_TARGET,

                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.ANGRY_AT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,

                MemoryModuleType.LAST_WORKED_AT_POI);

        POINTS_OF_INTEREST = ImmutableMap.of(MemoryModuleType.JOB_SITE,(civilizedPiglinEntity, registryEntry) -> {
            return civilizedPiglinEntity.getWorkerData().getProfession().heldWorkstation().test(registryEntry);
        },MemoryModuleType.POTENTIAL_JOB_SITE,(workerPiglinEntity, registryEntry) -> {
            Predicate<RegistryEntry<PointOfInterestType>> IS_ACQUIRABLE_JOB_SITE = poi -> poi.isIn(ModTags.PIGLIN_ACQUIRABLE_JOB_SITE);
            return  IS_ACQUIRABLE_JOB_SITE.test(registryEntry);
        });

        GO_TO_ZOMBIFIED_MEMORY_DURATION = TimeHelper.betweenSeconds(5, 7);
        GO_TO_NEMESIS_MEMORY_DURATION = TimeHelper.betweenSeconds(6,9);

    }
}
