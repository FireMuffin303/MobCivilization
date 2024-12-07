package net.firemuffin303.civilizedmobs.mixin.ominousBannerInteraction;

import net.firemuffin303.civilizedmobs.common.entity.brain.IllagerHostileSensor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostileSensorMixin {

    @Inject(method = "matches", at = @At("TAIL"), cancellable = true)
    public void civilizedMob$matches(LivingEntity entity, LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if(target instanceof PlayerEntity player){
            if(IllagerHostileSensor.isHoldingOminousBanner(player) && target.squaredDistanceTo(entity) <= 12f * 12f){
                cir.setReturnValue(true);
            }
        }
    }
}
