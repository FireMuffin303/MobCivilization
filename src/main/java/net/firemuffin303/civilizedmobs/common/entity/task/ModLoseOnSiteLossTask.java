package net.firemuffin303.civilizedmobs.common.entity.task;

import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.common.entity.WorkerData;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.village.VillagerProfession;

public class ModLoseOnSiteLossTask {

    public static Task<WorkerPiglinEntity> create() {
        return TaskTriggerer.task((context) -> {
            return context.group(context.queryMemoryAbsent(MemoryModuleType.JOB_SITE)).apply(context, (jobSite) -> {
                return (world, entity, time) -> {
                    WorkerData workerData = entity.getWorkerData();
                    if (workerData.getProfession() != VillagerProfession.NONE && entity.getExperience() == 0 && workerData.getLevel() <= 1) {
                        entity.setWorkerData(entity.getWorkerData().withProfession(VillagerProfession.NONE));
                        entity.reinitializeBrain(world);
                        return true;
                    } else {
                        return false;
                    }
                };
            });
        });
    }
}
