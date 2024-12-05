package net.firemuffin303.civilizedmobs.common.entity.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import dev.emi.trinkets.api.TrinketsApi;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleLivingEntitySensor;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.raid.Raid;

import java.util.Map;

public class IllagerHostileSensor extends NearestVisibleLivingEntitySensor {

    @Override
    protected boolean matches(LivingEntity entity, LivingEntity target) {
        return isHostile(target) && entity.squaredDistanceTo(target) <= 12.0f;
    }

    public static boolean isHostile(LivingEntity livingEntity){
        if(livingEntity instanceof PlayerEntity player){
            return !IllagerHostileSensor.isHoldingOminousBanner(player);
        }
        return livingEntity instanceof IronGolemEntity;
    }

    public static boolean isHoldingOminousBanner(PlayerEntity player){
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);
        boolean isWearingOminousBanner = !itemStack.isEmpty() && itemStack.getNbt() != null && itemStack.getNbt().equals(Raid.getOminousBanner().getNbt());
        if(CivilizedMobs.isTrinketsInstall){
            if(TrinketsApi.getTrinketComponent(player).isPresent()){
                isWearingOminousBanner |= !TrinketsApi.getTrinketComponent(player).get().getEquipped(itemStack1 -> {
                   return  itemStack.getNbt() != null && itemStack.getNbt().equals(Raid.getOminousBanner().getNbt());
                }).isEmpty();
            }
        }

        return isWearingOminousBanner;
    }

    @Override
    protected MemoryModuleType<LivingEntity> getOutputMemoryModule() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }
}
