package net.firemuffin303.villagefoe.mixin.bedInteraction;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.firemuffin303.villagefoe.common.entity.pillager.worker.PillagerWorkerEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Debug(export = true)
@Mixin(BedBlock.class)
public abstract class BedBlockMixin {
    @ModifyReturnValue(method = "wakeVillager" ,at = @At(value = "RETURN",ordinal = 0))
    public boolean checkIfPillagerWorkerSleep(boolean original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos blockPos){
        List<PillagerWorkerEntity> list = world.getEntitiesByClass(PillagerWorkerEntity.class,new Box(blockPos), LivingEntity::isSleeping);
        if(list.isEmpty()){
            return original;
        }else{
            list.get(0).wakeUp();
            return true;
        }
    }
}
