package net.firemuffin303.civilizedmobs.mixin.ominousBannerInteraction;

import net.firemuffin303.civilizedmobs.common.entity.brain.IllagerHostileSensor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public class IronGolemEntityMixin extends GolemEntity {
    protected IronGolemEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals",at = @At("TAIL"))
    public void civil$initGoals(CallbackInfo ci){
        this.targetSelector.add(3,new ActiveTargetGoal<>(this,PlayerEntity.class,10,true,false, livingEntity -> {
            return IllagerHostileSensor.isHoldingOminousBanner((PlayerEntity) livingEntity);
        }));
    }

}
