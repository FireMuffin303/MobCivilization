package net.firemuffin303.civilizedmobs.mixin;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {

    @Shadow protected abstract void sayNo();

    @Inject(method = "interactMob",at = @At("HEAD"), cancellable = true)
    public void civilizedMob$interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);
        if(CivilizedMobs.isHoldingOminousBanner(itemStack)){
            this.sayNo();
            cir.setReturnValue(ActionResult.success(((VillagerEntity)(Object)this).getWorld().isClient));
        }
    }
}
