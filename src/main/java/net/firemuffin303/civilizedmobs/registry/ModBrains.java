package net.firemuffin303.civilizedmobs.registry;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.brain.PiglinDetectGoldArmorPlayer;
import net.firemuffin303.civilizedmobs.common.entity.brain.PiglinNemesisSensor;
import net.firemuffin303.civilizedmobs.common.entity.brain.PiglinRepellentSensor;
import net.firemuffin303.civilizedmobs.common.entity.brain.PiglinZombifiedSensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBrains {
    public static final SensorType<PiglinRepellentSensor> PIGLIN_REPELLENT = Registry.register(Registries.SENSOR_TYPE,new Identifier(CivilizedMobs.MOD_ID,"piglin_repellent"),new SensorType<>(PiglinRepellentSensor::new));
    public static final SensorType<PiglinNemesisSensor> PIGLIN_NEMESIS = Registry.register(Registries.SENSOR_TYPE,new Identifier(CivilizedMobs.MOD_ID,"piglin_nemesis"),new SensorType<>(PiglinNemesisSensor::new));
    public static final SensorType<PiglinDetectGoldArmorPlayer> PIGLIN_DETECT_GOLD_ARMOR_PLAYER = Registry.register(Registries.SENSOR_TYPE,new Identifier(CivilizedMobs.MOD_ID,"piglin_detect_gold_armor_player"),new SensorType<>(PiglinDetectGoldArmorPlayer::new));
    public static final SensorType<PiglinZombifiedSensor> PIGLIN_ZOMBIFIED = Registry.register(Registries.SENSOR_TYPE,new Identifier(CivilizedMobs.MOD_ID,"piglin_zombified"),new SensorType<>(PiglinZombifiedSensor::new));

    public static void init(){}
}
