package net.firemuffin303.villagefoe.mixin.ominousBannerInteraction;

import net.firemuffin303.villagefoe.common.entity.brain.IllagerHostileSensor;
import net.firemuffin303.villagefoe.common.entity.pillager.IllagerTradeInteraction;
import net.firemuffin303.villagefoe.common.entity.pillager.IllagerTradeOffers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity implements Merchant {

    @Unique
    private IllagerTradeInteraction illagerTradeInteraction = new IllagerTradeInteraction((PillagerEntity) (Object)this, IllagerTradeOffers.PILLAGER_TRADES);

    protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand){
        ItemStack itemStack = player.getStackInHand(hand);
        if(!itemStack.isOf(Items.EVOKER_SPAWN_EGG) && this.isAlive() && !this.illagerTradeInteraction.hasCustomer() && (IllagerHostileSensor.isHoldingOminousBanner(player) || player.isCreative())){
            if(this.getOffers().isEmpty()){
                return ActionResult.success(this.getWorld().isClient);
            }else{
                if(!this.getWorld().isClient){
                    this.setCustomer(player);
                    this.sendOffers(player,this.getDisplayName(),1);
                }

                return ActionResult.success(this.getWorld().isClient);
            }
        }else {
            return super.interactMob(player, hand);
        }
    }

    @Inject(method = "writeCustomDataToNbt",at = @At("TAIL"))
    public void civilmob$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
        this.illagerTradeInteraction.save(nbt);
    }

    @Inject(method = "readCustomDataFromNbt",at =@At("TAIL"))
    public void civilMob$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
        this.illagerTradeInteraction.load(nbt);
    }

    //Merchant
    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        this.illagerTradeInteraction.setCustomer(customer);
    }

    @Override
    public @Nullable PlayerEntity getCustomer() {
        return this.illagerTradeInteraction.getCustomer();
    }

    @Override
    public TradeOfferList getOffers() {
        return this.illagerTradeInteraction.getOffers();
    }

    @Override
    public void trade(TradeOffer offer) {
        this.illagerTradeInteraction.trade(offer);
    }

    @Override
    public void onSellingItem(ItemStack stack) {

    }

    @Override
    public int getExperience() {
        return this.experiencePoints;
    }

    @Override
    public void setExperienceFromServer(int experience) {

    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_PILLAGER_CELEBRATE;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }
}
