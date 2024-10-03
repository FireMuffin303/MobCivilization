package net.firemuffin303.civilizedmobs.common.entity.task;

import com.google.common.collect.ImmutableMap;
import net.firemuffin303.civilizedmobs.common.entity.CivilizedPiglinEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestStorage;

import java.util.Map;
import java.util.Optional;

public class ModWalkTowardJobsiteTask extends MultiTickTask<CivilizedPiglinEntity> {
    final float speed;
    public ModWalkTowardJobsiteTask(float speed) {
        super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE,MemoryModuleState.VALUE_PRESENT),1200);
        this.speed = speed;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, CivilizedPiglinEntity entity) {
        return entity.getBrain().getFirstPossibleNonCoreActivity().map(activity -> {
            return activity == Activity.IDLE || activity == Activity.WORK;
        }).orElse(true);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, CivilizedPiglinEntity entity, long time) {
        return entity.getBrain().hasMemoryModule(MemoryModuleType.POTENTIAL_JOB_SITE);
    }

    protected void keepRunning(ServerWorld serverWorld, CivilizedPiglinEntity civilizedPiglinEntity, long l) {
        LookTargetUtil.walkTowards(civilizedPiglinEntity, civilizedPiglinEntity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos(), this.speed, 1);
    }

    protected void finishRunning(ServerWorld serverWorld, CivilizedPiglinEntity civilizedPiglinEntity, long l) {
        Optional<GlobalPos> optional = civilizedPiglinEntity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        optional.ifPresent((pos) -> {
            BlockPos blockPos = pos.getPos();
            ServerWorld serverWorld2 = serverWorld.getServer().getWorld(pos.getDimension());
            if (serverWorld2 != null) {
                PointOfInterestStorage pointOfInterestStorage = serverWorld2.getPointOfInterestStorage();
                if (pointOfInterestStorage.test(blockPos, (poiType) -> true)) {
                    pointOfInterestStorage.releaseTicket(blockPos);
                }

                DebugInfoSender.sendPointOfInterest(serverWorld, blockPos);
            }
        });
        civilizedPiglinEntity.getBrain().forget(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
}
