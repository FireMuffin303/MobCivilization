package net.firemuffin303.villagefoe.registry;

import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.common.entity.brain.*;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.ScheduleBuilder;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBrains {
    public static final SensorType<PiglinRepellentSensor> PIGLIN_REPELLENT = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_repellent"),new SensorType<>(PiglinRepellentSensor::new));
    public static final SensorType<PiglinNemesisSensor> PIGLIN_NEMESIS = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_nemesis"),new SensorType<>(PiglinNemesisSensor::new));
    public static final SensorType<PiglinDetectGoldArmorPlayer> PIGLIN_DETECT_GOLD_ARMOR_PLAYER = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_detect_gold_armor_player"),new SensorType<>(PiglinDetectGoldArmorPlayer::new));
    public static final SensorType<PiglinZombifiedSensor> PIGLIN_ZOMBIFIED = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"piglin_zombified"),new SensorType<>(PiglinZombifiedSensor::new));

    public static final SensorType<IllagerHostileSensor> ILLAGER_HOSTILE = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"illager_hostile"),new SensorType<>(IllagerHostileSensor::new));

    public static final SensorType<WitherSkeletonNemesisSensor> WITHER_SKELETON_NEMESIS = Registry.register(Registries.SENSOR_TYPE,new Identifier(VillageFoe.MOD_ID,"wither_skeleton_nemesis"),new SensorType<>(WitherSkeletonNemesisSensor::new));

    public static final Schedule PILLAGER_WORKER_DEFAULT = Registry.register(Registries.SCHEDULE,new Identifier(VillageFoe.MOD_ID,"pillager_worker_default"),new ScheduleBuilder(new Schedule())
            .withActivity(10, Activity.IDLE).withActivity(2000,Activity.WORK).withActivity(12000,Activity.REST).build());

    public static void init(){}
}
