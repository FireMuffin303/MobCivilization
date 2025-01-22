package net.firemuffin303.villagefoe.common.entity.brain;

import com.mojang.logging.LogUtils;
import dev.emi.trinkets.api.TrinketsApi;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.registry.ModTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleLivingEntitySensor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.village.raid.Raid;

public class IllagerHostileSensor extends NearestVisibleLivingEntitySensor {

    @Override
    protected boolean matches(LivingEntity entity, LivingEntity target) {
        return isHostile(target) && entity.squaredDistanceTo(target) <= (12.0f * 12.0f);
    }

    public static boolean isHostile(LivingEntity livingEntity){
        if(livingEntity instanceof PlayerEntity player){
            return !IllagerHostileSensor.isHoldingOminousBanner(player) && !player.getAbilities().creativeMode;
        }
        return livingEntity.getType().isIn(ModTags.ILLAGER_NEMESIS);
    }

    public static boolean isHoldingOminousBanner(PlayerEntity player){
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);
        boolean isWearingOminousBanner = isItemStackOminousBanner(itemStack);

        if(VillageFoe.isTrinketsInstall){
            if(TrinketsApi.getTrinketComponent(player).isPresent()){
                if(TrinketsApi.getTrinketComponent(player).get().isEquipped(IllagerHostileSensor::isItemStackOminousBanner)){
                    isWearingOminousBanner = true;
                }
            }
        }
        return isWearingOminousBanner;

    }

    private static boolean isItemStackOminousBanner(ItemStack itemStack){
        if(itemStack.isOf(Items.WHITE_BANNER) && itemStack.getNbt() != null && itemStack.hasNbt() && Raid.getOminousBanner().getNbt() != null && Raid.getOminousBanner().hasNbt()){
            NbtList nbtList = itemStack.getNbt().getCompound("BlockEntityTag").getList("Patterns",10);
            NbtList nbtList1 = Raid.getOminousBanner().getNbt().getCompound("BlockEntityTag").getList("Patterns",10);
            return nbtList.equals(nbtList1);
        }
        return false;
    }

    @Override
    protected MemoryModuleType<LivingEntity> getOutputMemoryModule() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }
}
