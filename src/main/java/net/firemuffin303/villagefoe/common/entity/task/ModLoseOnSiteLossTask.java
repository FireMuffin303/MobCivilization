package net.firemuffin303.villagefoe.common.entity.task;

import net.firemuffin303.villagefoe.common.entity.WorkerContainer;
import net.firemuffin303.villagefoe.common.entity.WorkerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.village.Merchant;
import net.minecraft.village.VillagerProfession;

public class ModLoseOnSiteLossTask {

    public static Task<LivingEntity> create() {
        return TaskTriggerer.task((context) -> context.group(context.queryMemoryAbsent(MemoryModuleType.JOB_SITE)).apply(context, (jobSite) -> (world, entity, time) -> {
            if(entity instanceof WorkerContainer workerContainer && entity instanceof Merchant merchant){
                WorkerData workerData = workerContainer.getWorkerData();
                if (workerData.getProfession() != VillagerProfession.NONE && merchant.getExperience() == 0 && workerData.getLevel() <= 1) {
                    workerContainer.setWorkerData(workerContainer.getWorkerData().withProfession(VillagerProfession.NONE));
                    workerContainer.reinitializeBrain(world);
                    return true;
                }
            }
            return false;

        }));
    }
}
