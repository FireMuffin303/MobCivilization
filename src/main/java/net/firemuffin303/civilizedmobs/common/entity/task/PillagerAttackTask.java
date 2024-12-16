package net.firemuffin303.civilizedmobs.common.entity.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;

public class PillagerAttackTask extends MultiTickTask<LivingEntity> {
    public PillagerAttackTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, LivingEntity entity, long time) {
        return entity.isHolding(Items.CROSSBOW) && (entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_HOSTILE).isPresent() || entity.getBrain().getOptionalMemory(MemoryModuleType.HURT_BY_ENTITY).isPresent());
    }

    @Override
    protected void run(ServerWorld world, LivingEntity entity, long time) {
        if( entity.isHolding(Items.CROSSBOW) && (entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_HOSTILE).isPresent() || entity.getBrain().getOptionalMemory(MemoryModuleType.HURT_BY_ENTITY).isPresent())){
            Brain<?> brain = entity.getBrain();
            if (!brain.hasActivity(Activity.FIGHT)) {
                brain.forget(MemoryModuleType.PATH);
                brain.forget(MemoryModuleType.WALK_TARGET);
                brain.forget(MemoryModuleType.LOOK_TARGET);
                brain.forget(MemoryModuleType.BREED_TARGET);
                brain.forget(MemoryModuleType.INTERACTION_TARGET);
                Optional<LivingEntity> optionalHurtby = entity.getBrain().getOptionalMemory(MemoryModuleType.HURT_BY_ENTITY);
                brain.remember(MemoryModuleType.ATTACK_TARGET,optionalHurtby.isPresent() ? optionalHurtby : entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_HOSTILE));
            }
            brain.doExclusively(Activity.FIGHT);
        }
    }
}
