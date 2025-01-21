package net.firemuffin303.villagefoe.registry;

import com.mojang.serialization.Codec;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.common.entity.brain.*;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.ScheduleBuilder;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModBrains {
    public static final SensorType<PiglinRepellentSensor> PIGLIN_REPELLENT = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_repellent"),new SensorType<>(PiglinRepellentSensor::new));
    public static final SensorType<PiglinNemesisSensor> PIGLIN_NEMESIS = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_nemesis"),new SensorType<>(PiglinNemesisSensor::new));
    public static final SensorType<PiglinDetectGoldArmorPlayer> PIGLIN_DETECT_GOLD_ARMOR_PLAYER = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_detect_gold_armor_player"),new SensorType<>(PiglinDetectGoldArmorPlayer::new));
    public static final SensorType<PiglinZombifiedSensor> PIGLIN_ZOMBIFIED = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_zombified"),new SensorType<>(PiglinZombifiedSensor::new));
    public static final SensorType<SeenLeaderSensor> PIGLIN_LEADER_LAST_SEEN = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_leader_last_seen"),new SensorType<>(() -> new SeenLeaderSensor(ModEntityType.PIGLIN_LEADER_ENTITY)));


    public static final SensorType<IllagerHostileSensor> ILLAGER_HOSTILE = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"illager_hostile"),new SensorType<>(IllagerHostileSensor::new));
    public static final SensorType<SeenLeaderSensor> PILLAGER_LEADER_LAST_SEEN = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"pillager_leader_last_seen"),new SensorType<>(() -> new SeenLeaderSensor(ModEntityType.PILLAGER_LEADER)));


    public static final SensorType<WitherSkeletonNemesisSensor> WITHER_SKELETON_NEMESIS = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"wither_skeleton_nemesis"),new SensorType<>(WitherSkeletonNemesisSensor::new));
    public static final SensorType<SeenLeaderSensor> WITHER_SKELETON_LEADER_LAST_SEEN = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"wither_skeleton_leader_last_seen"),new SensorType<>(() -> new SeenLeaderSensor(ModEntityType.WITHER_SKELETON_LEADER)));

    public static final MemoryModuleType<Boolean> LEADER_DETECTED_RECENTLY = Registry.register(Registries.MEMORY_MODULE_TYPE,new Identifier(VillageFoe.MOD_ID,"leader_detected_recently"),new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static final Schedule PILLAGER_WORKER_DEFAULT = Registry.register(Registries.SCHEDULE,new Identifier(VillageFoe.MOD_ID,"pillager_worker_default"),new ScheduleBuilder(new Schedule())
            .withActivity(10, Activity.IDLE).withActivity(2000,Activity.WORK).withActivity(12000,Activity.REST).build());

    public static void init(){}
}
