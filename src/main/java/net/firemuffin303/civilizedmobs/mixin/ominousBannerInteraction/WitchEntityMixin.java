package net.firemuffin303.civilizedmobs.mixin.ominousBannerInteraction;

import net.firemuffin303.civilizedmobs.common.entity.brain.IllagerHostileSensor;
import net.firemuffin303.civilizedmobs.common.entity.pillager.IllagerTradeInteraction;
import net.firemuffin303.civilizedmobs.common.entity.pillager.IllagerTradeOffers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMixin extends RaiderEntity implements Merchant {

    @Shadow public abstract void tickMovement();

    @Unique
    private IllagerTradeInteraction illagerTradeInteraction = new IllagerTradeInteraction((WitchEntity) (Object)this, IllagerTradeOffers.WITCH_TRADES);


    protected WitchEntityMixin(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
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

    @Unique
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.illagerTradeInteraction.save(nbt);
    }

    @Unique
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
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
        return SoundEvents.ENTITY_WITCH_CELEBRATE;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }
}
