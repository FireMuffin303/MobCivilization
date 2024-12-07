package net.firemuffin303.civilizedmobs.mixin.ominousBannerInteraction;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Equipment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BannerItem.class)
public class BannerItemMixin implements Equipment {

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }
}
