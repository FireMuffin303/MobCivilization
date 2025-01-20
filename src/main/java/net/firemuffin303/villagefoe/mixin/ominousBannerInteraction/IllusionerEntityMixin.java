package net.firemuffin303.villagefoe.mixin.ominousBannerInteraction;

import net.firemuffin303.villagefoe.common.entity.brain.IllagerHostileSensor;
import net.firemuffin303.villagefoe.common.entity.pillager.IllagerTradeInteraction;
import net.firemuffin303.villagefoe.common.entity.pillager.IllagerTradeOffers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
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

@Mixin(IllusionerEntity.class)
public abstract class IllusionerEntityMixin extends SpellcastingIllagerEntity implements Merchant {

    @Unique
    private IllagerTradeInteraction illagerTradeInteraction = new IllagerTradeInteraction((IllusionerEntity) (Object)this, IllagerTradeOffers.ILLUSIONER_TRADES);

    protected IllusionerEntityMixin(EntityType<? extends SpellcastingIllagerEntity> entityType, World world) {
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

    public void writeCustomDataToNbt(NbtCompound nbt){
        this.illagerTradeInteraction.save(nbt);
    }

    public void readCustomDataFromNbt(NbtCompound nbt){
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
        return SoundEvents.ENTITY_ILLUSIONER_AMBIENT;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }
}
