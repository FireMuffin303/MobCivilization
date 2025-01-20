package net.firemuffin303.villagefoe.common.entity.task;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;

public class PillagerWorkerPanic extends ModPanicTask{

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, LivingEntity entity, long time) {
        return !entity.isHolding(Items.CROSSBOW) && super.shouldKeepRunning(world, entity, time);
    }

    @Override
    protected void run(ServerWorld world, LivingEntity entity, long time) {
        if(!entity.isHolding(Items.CROSSBOW)){
            super.run(world, entity, time);
        }
    }
}
