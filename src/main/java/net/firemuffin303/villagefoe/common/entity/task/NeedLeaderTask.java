package net.firemuffin303.villagefoe.common.entity.task;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import net.firemuffin303.villagefoe.common.entity.LeaderSummoner;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public class NeedLeaderTask<T extends MobEntity & LeaderSummoner> extends MultiTickTask<T> {
    private final List<EntityType<?>> entityTypes;
    public NeedLeaderTask(EntityType<?>... entityTypes) {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET,MemoryModuleState.VALUE_PRESENT,MemoryModuleType.VISIBLE_MOBS,MemoryModuleState.VALUE_PRESENT));
        this.entityTypes = List.of(entityTypes);
    }

    @Override
    protected boolean shouldRun(ServerWorld world, T entity) {
        return this.entityTypes.stream().anyMatch(entityType -> LookTargetUtil.canSee(entity.getBrain(),MemoryModuleType.INTERACTION_TARGET, entityType));
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, T entity, long time) {
        return this.shouldRun(world,entity);
    }

    @Override
    protected void run(ServerWorld world, T entity, long time) {
        LivingEntity livingEntity = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.INTERACTION_TARGET).get();
        LookTargetUtil.lookAtAndWalkTowardsEachOther(entity, livingEntity, 0.5F);
    }

    @Override
    protected void keepRunning(ServerWorld world, T entity, long time) {
        LivingEntity livingEntity = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if(entity.squaredDistanceTo(livingEntity) > 5.0){
            LookTargetUtil.lookAtAndWalkTowardsEachOther(entity, livingEntity, 0.5F);
            entity.summonLeader(world,time);
        }
    }

    @Override
    protected void finishRunning(ServerWorld world, T entity, long time) {
        entity.getBrain().forget(MemoryModuleType.INTERACTION_TARGET);
    }
}
