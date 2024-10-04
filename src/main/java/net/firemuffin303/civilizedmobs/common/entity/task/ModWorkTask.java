package net.firemuffin303.civilizedmobs.common.entity.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;

public class ModWorkTask extends MultiTickTask<LivingEntity> {
    public ModWorkTask() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE,MemoryModuleState.VALUE_PRESENT,MemoryModuleType.LOOK_TARGET,MemoryModuleState.REGISTERED));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, LivingEntity entity) {

        return super.shouldRun(world, entity);
    }
}
