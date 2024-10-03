package net.firemuffin303.civilizedmobs.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VexEntity.class)
public class VexEntityMixin extends HostileEntity {

    protected VexEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    public void civilMobs$initGoals(CallbackInfo ci){
        this.targetSelector.getGoals().removeIf(prioritizedGoal -> prioritizedGoal.getGoal() instanceof ActiveTargetGoal<?>);
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class,10,true,false,(entity) ->{
            ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.HEAD);
            if(itemStack != null && itemStack.getNbt() != null){
                return (!itemStack.getNbt().equals(Raid.getOminousBanner().getNbt()));
            }
            return true;
        }));
    }
}
