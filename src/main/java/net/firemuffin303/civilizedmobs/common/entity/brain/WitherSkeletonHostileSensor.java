package net.firemuffin303.civilizedmobs.common.entity.brain;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleLivingEntitySensor;

public class WitherSkeletonHostileSensor extends NearestVisibleLivingEntitySensor {
    @Override
    protected boolean matches(LivingEntity entity, LivingEntity target) {
        return false;
    }

    @Override
    protected MemoryModuleType<LivingEntity> getOutputMemoryModule() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }
}
