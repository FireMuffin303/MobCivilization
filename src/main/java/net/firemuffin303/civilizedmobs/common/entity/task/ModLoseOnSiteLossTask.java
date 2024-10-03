package net.firemuffin303.civilizedmobs.common.entity.task;

import net.firemuffin303.civilizedmobs.common.entity.CivilizedOffers;
import net.firemuffin303.civilizedmobs.common.entity.CivilizedPiglinEntity;
import net.firemuffin303.civilizedmobs.common.entity.CivilziedProfession;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinData;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;

import java.util.List;

import static net.firemuffin303.civilizedmobs.common.entity.CivilizedOffers.PIGLIN_TEST_TRADE;

public class ModLoseOnSiteLossTask {

    public static Task<CivilizedPiglinEntity> create() {
        return TaskTriggerer.task((context) -> {
            return context.group(context.queryMemoryAbsent(MemoryModuleType.JOB_SITE)).apply(context, (jobSite) -> {
                return (world, entity, time) -> {
                    WorkerPiglinData villagerData = entity.getPiglinData();
                    if (villagerData.getProfession() != VillagerProfession.NONE && entity.getExperience() == 0 && villagerData.getLevel() <= 1) {
                        entity.setPiglinData(entity.getPiglinData().withProfession(VillagerProfession.NONE));
                        //entity.reinitializeBrain(world);
                        return true;
                    } else {
                        return false;
                    }
                };
            });
        });
    }
}
