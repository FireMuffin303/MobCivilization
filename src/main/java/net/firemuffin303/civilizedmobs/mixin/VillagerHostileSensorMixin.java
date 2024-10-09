package net.firemuffin303.civilizedmobs.mixin;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostileSensorMixin {

    @Inject(method = "matches", at = @At("TAIL"), cancellable = true)
    public void civilizedMob$matches(LivingEntity entity, LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        ItemStack itemStack = target.getEquippedStack(EquipmentSlot.HEAD);
        if(CivilizedMobs.isHoldingOminousBanner(itemStack) && target.squaredDistanceTo(entity) <= 12f * 12f){
            cir.setReturnValue(true);
        }
    }

}
