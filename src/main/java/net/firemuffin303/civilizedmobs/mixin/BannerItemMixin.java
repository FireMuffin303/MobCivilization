package net.firemuffin303.civilizedmobs.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BannerItem.class)
public class BannerItemMixin implements Equipment {

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }
}
