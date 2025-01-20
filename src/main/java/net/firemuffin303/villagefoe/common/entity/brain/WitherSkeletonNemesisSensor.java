package net.firemuffin303.villagefoe.common.entity.brain;

import net.firemuffin303.villagefoe.registry.ModTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleLivingEntitySensor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class WitherSkeletonNemesisSensor extends NearestVisibleLivingEntitySensor {
    @Override
    protected boolean matches(LivingEntity entity, LivingEntity target) {
        if(target instanceof PlayerEntity player){
            return !isWearingSkeletonHead(player) && !player.getAbilities().creativeMode;
        }
        return target.getType().isIn(ModTags.WITHER_SKELTON_NEMESIS);
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
