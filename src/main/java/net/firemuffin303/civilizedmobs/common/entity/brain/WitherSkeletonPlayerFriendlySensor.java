package net.firemuffin303.civilizedmobs.common.entity.brain;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleLivingEntitySensor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class WitherSkeletonPlayerFriendlySensor extends NearestVisibleLivingEntitySensor {
    @Override
    protected boolean matches(LivingEntity entity, LivingEntity target) {
        if(target instanceof PlayerEntity player){
            return isWearingSkeletonHead(player);
        }
        return false;
    }

    @Override
    protected MemoryModuleType<LivingEntity> getOutputMemoryModule() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }

    public static boolean isWearingSkeletonHead(PlayerEntity player){
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);
        if(itemStack.isOf(Items.WITHER_SKELETON_SKULL)){
            return true;
        }
        return false;
    }
}
