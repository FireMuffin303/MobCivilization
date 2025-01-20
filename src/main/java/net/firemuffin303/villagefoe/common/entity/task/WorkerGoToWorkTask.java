package net.firemuffin303.villagefoe.common.entity.task;

import net.firemuffin303.villagefoe.common.entity.WorkerContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.VillagerProfession;

import java.util.Optional;

public class WorkerGoToWorkTask {
    public static Task<LivingEntity> create() {
        return TaskTriggerer.task((context) -> {
            return context.group(context.queryMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE), context.queryMemoryOptional(MemoryModuleType.JOB_SITE)).apply(context, (potentialJobSite, jobSite) -> {
                return (world, entity, time) -> {
                    GlobalPos globalPos = (GlobalPos)context.getValue(potentialJobSite);
                    if (!globalPos.getPos().isWithinDistance(entity.getPos(), 2.0)) {
                        return false;
                    } else if(entity instanceof WorkerContainer workerContainer) {
                        potentialJobSite.forget();
                        jobSite.remember(globalPos);
                        world.sendEntityStatus(entity, (byte)14);
                        if (workerContainer.getWorkerData().getProfession() != VillagerProfession.NONE) {
                            return true;
                        } else {
                            MinecraftServer minecraftServer = world.getServer();
                            Optional.ofNullable(minecraftServer.getWorld(globalPos.getDimension())).flatMap((jobSiteWorld) -> {
                                return jobSiteWorld.getPointOfInterestStorage().getType(globalPos.getPos());
                            }).flatMap((poiType) -> {
                                return Registries.VILLAGER_PROFESSION.stream().filter((profession) -> {
                                    return profession.heldWorkstation().test(poiType);
                                }).findFirst();
                            }).ifPresent((profession) -> {
                                workerContainer.setWorkerData(workerContainer.getWorkerData().withProfession(profession));
                                workerContainer.reinitializeBrain(world);
                            });
                            return true;
                        }
                    }else {
                        return false;
                    }
                };
            });
        });
    }
}
