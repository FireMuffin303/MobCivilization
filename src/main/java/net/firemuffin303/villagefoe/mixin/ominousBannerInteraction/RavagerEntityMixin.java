package net.firemuffin303.villagefoe.mixin.ominousBannerInteraction;

import net.firemuffin303.villagefoe.common.entity.brain.IllagerHostileSensor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({RavagerEntity.class})
public abstract class RavagerEntityMixin extends RaiderEntity {
    protected RavagerEntityMixin(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);

        if(IllagerHostileSensor.isHoldingOminousBanner(player) && !this.hasPassengers() && this.getTarget() != player){
            if(!this.getWorld().isClient){
                player.startRiding(this);
            }
            return ActionResult.success(this.getWorld().isClient);
        }
        return super.interactMob(player, hand);
    }

    /*
    @Inject(method = "canLead" , at = @At("TAIL"), cancellable = true)
    public void civilized$canLead(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }
    */
}
