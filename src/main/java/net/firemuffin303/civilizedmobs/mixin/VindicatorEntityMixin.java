package net.firemuffin303.civilizedmobs.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VindicatorEntity.class)
public class VindicatorEntityMixin extends IllagerEntity {
    protected VindicatorEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    public void civil$initGoals(CallbackInfo ci){
        this.targetSelector.getGoals().removeIf(prioritizedGoal -> prioritizedGoal.getGoal() instanceof ActiveTargetGoal<?>);
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, livingEntity -> {
            ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
            if(itemStack != null && itemStack.getNbt() != null){
                boolean bl = !itemStack.getNbt().equals(Raid.getOminousBanner().getNbt());
                return bl;
            }
            return true;
        }));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Shadow
    @Override
    public void addBonusForWave(int wave, boolean unused) {

    }

    @Shadow
    @Override
    public SoundEvent getCelebratingSound() {
        return null;
    }
}