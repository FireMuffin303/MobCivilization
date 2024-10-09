package net.firemuffin303.civilizedmobs.common.entity.task;

import com.google.common.collect.ImmutableMap;
import net.firemuffin303.civilizedmobs.common.entity.WorkerContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

public class ModWorkTask extends MultiTickTask<LivingEntity> {
    private long lastCheckedTime;
    public ModWorkTask() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE,MemoryModuleState.VALUE_PRESENT,MemoryModuleType.LOOK_TARGET,MemoryModuleState.REGISTERED));
    }

    @Override
    protected boolean shouldRun(ServerWorld serverWorld, LivingEntity entity) {
        if (serverWorld.getTime() - this.lastCheckedTime < 300L) {
            return false;
        } else if (serverWorld.random.nextInt(2) != 0) {
            return false;
        } else {
            this.lastCheckedTime = serverWorld.getTime();
            GlobalPos globalPos = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).get();
            return globalPos.getDimension() == serverWorld.getRegistryKey() && globalPos.getPos().isWithinDistance(entity.getPos(), 1.73);
        }
    }

    @Override
    protected void run(ServerWorld world, LivingEntity entity, long time) {
        Brain<LivingEntity> brain = (Brain<LivingEntity>) entity.getBrain();
        brain.remember(MemoryModuleType.LAST_WORKED_AT_POI, time);
        brain.getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE).ifPresent((pos) -> {
            brain.remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(pos.getPos()));
        });
        if(entity instanceof WorkerContainer workerContainer){

            if (workerContainer.shouldRestock()) {
                workerContainer.playWorkSound(entity);
                workerContainer.restock();
            }
        }



    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, LivingEntity livingEntity, long l) {
        Optional<GlobalPos> optional = livingEntity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.JOB_SITE);
        if (!optional.isPresent()) {
            return false;
        } else {
            GlobalPos globalPos = (GlobalPos)optional.get();
            return globalPos.getDimension() == serverWorld.getRegistryKey() && globalPos.getPos().isWithinDistance(livingEntity.getPos(), 1.73);
        }
    }
}
