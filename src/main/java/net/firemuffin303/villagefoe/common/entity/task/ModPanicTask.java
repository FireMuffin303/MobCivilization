package net.firemuffin303.villagefoe.common.entity.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;

public class ModPanicTask extends MultiTickTask<LivingEntity> {
    public ModPanicTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, LivingEntity entity, long time) {
        return entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_HOSTILE).isPresent() || entity.getBrain().getOptionalMemory(MemoryModuleType.HURT_BY).isPresent();
    }

    @Override
    protected void run(ServerWorld world, LivingEntity entity, long time) {
        if(entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_HOSTILE).isPresent() || entity.getBrain().getOptionalMemory(MemoryModuleType.HURT_BY).isPresent()){
            Brain<?> brain = entity.getBrain();
            if (!brain.hasActivity(Activity.PANIC)) {
                brain.forget(MemoryModuleType.PATH);
                brain.forget(MemoryModuleType.WALK_TARGET);
                brain.forget(MemoryModuleType.LOOK_TARGET);
                brain.forget(MemoryModuleType.BREED_TARGET);
                brain.forget(MemoryModuleType.INTERACTION_TARGET);
            }
            brain.doExclusively(Activity.PANIC);
        }
    }
}
