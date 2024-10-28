package net.firemuffin303.civilizedmobs.common.entity.piglin;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.CivilPiglinBrain;
import net.firemuffin303.civilizedmobs.common.entity.WorkerData;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestContainer;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestData;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class PiglinQuestEntity extends AbstractPiglinEntity implements GeoEntity, Merchant, QuestContainer {
    private long lastRestockTime;
    protected QuestData questData;
    private int levelUpTimer;
    private boolean levelingUp;
    @Nullable private PlayerEntity lastCustomer;
    @Nullable private PlayerEntity customer;
    //-- Geo ---
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public PiglinQuestEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
        this.questData = new QuestData(this);
    }

    //--- Attribute & Spawn ---
    public static DefaultAttributeContainer.Builder createAttribute(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH,16.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.349999994039535)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,5.0);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    //-- Interact Logic --

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(!this.getWorld().isClient && this.getCustomer() == null){
            this.prepareOffersFor(player);
            this.setCustomer(player);
            this.sendOffers(player,this.getDisplayName(),this.questData.getTrust(this.getCustomer().getUuid()).getLevel());
        }
        return ActionResult.success(this.getWorld().isClient);
    }

    private void prepareOffersFor(PlayerEntity player) {
        this.questData.getQuestList(player);
    }

    protected void mobTick() {
        // Level up when no Customer and timer is up.
        if (this.customer == null  && this.levelUpTimer > 0) {
            --this.levelUpTimer;
            if (this.levelUpTimer <= 0) {
                if (this.levelingUp) {
                    this.levelUp();
                    this.levelingUp = false;
                }

                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
            }
        }


        super.mobTick();
    }

    private void levelUp() {
        QuestData.Trustful trustful = this.questData.getTrust(this.lastCustomer.getUuid());
        trustful.setLevel(trustful.getLevel()+1);
        this.questData.fillTrade(trustful.getTradeOffers(),this.lastCustomer,2);
    }

    //-- Data --
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.questData.writeData(nbt);
        nbt.putLong("LastRestock",this.lastRestockTime);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.lastRestockTime = nbt.getLong("LastRestock");
        this.questData.readData(nbt);
    }

    //--- Merchant ---
    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        boolean bl = this.getCustomer() != null && customer == null;
        this.customer = customer;
        if(bl){
            this.resetCustomer();
        }
    }

    private void resetCustomer() {
        this.setCustomer(null);
    }

    @Override
    public @Nullable PlayerEntity getCustomer() {
        return this.customer;
    }

    @Override
    public TradeOfferList getOffers() {
        return this.questData.getQuestList(this.customer);
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {

    }

    @Override
    public void trade(TradeOffer offer) {
        int xp = 3 + this.random.nextInt(4);
        offer.use();
        this.lastCustomer = this.getCustomer();
        QuestData.Trustful trustful = this.questData.getTrust(this.customer.getUuid());
        int level = trustful.getLevel();
        if(VillagerData.canLevelUp(level) && trustful.getXp() >= QuestData.Trustful.getUpperLevelExperience(level)) {
            this.levelUpTimer = 40;
            this.levelingUp = true;
            xp += 5;
        }

        if(offer.shouldRewardPlayerExperience()){
            this.getWorld().spawnEntity(new ExperienceOrbEntity(this.getWorld(),this.getX(),this.getY()+0.5d,this.getZ(),xp));
        }
        UUID uuid = this.customer.getUuid();
        this.questData.increaseXp(uuid,offer.getMerchantExperience());

    }

    @Override
    public void onSellingItem(ItemStack stack) {

    }

    @Override
    public int getExperience() {
        return this.questData.getTrust(this.customer.getUuid()).getXp();
    }

    @Override
    public void setExperienceFromServer(int experience) {

    }

    @Override
    public boolean isLeveledMerchant() {
        return true;
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_PIGLIN_ADMIRING_ITEM;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }
    //---------

    // --- Quest Container ---
    @Override
    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    @Override
    public QuestData getQuestData() {
        return this.questData;
    }

    //-- Piglin --
    @Override
    protected boolean canHunt() {
        return false;
    }

    @Override
    public PiglinActivity getActivity() {
        return null;
    }

    @Override
    protected void playZombificationSound() {

    }

    //-- GeoModel --
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this,"controller",5,this::registerControllers));
    }

    private PlayState registerControllers(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        geoAnimatableAnimationState.setAnimation(RawAnimation.begin().then("animation.civil_piglin.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }


}
