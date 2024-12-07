package net.firemuffin303.civilizedmobs.common.entity.pillager.quest;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestContainer;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestData;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;

public class PillagerQuestEntity extends IllagerEntity implements GeoEntity, Merchant, QuestContainer, CrossbowUser {
    protected QuestData questData;
    protected long lastRestockTime;
    private int levelUpTimer;
    private boolean levelingUp;
    @Nullable private PlayerEntity lastCustomer;
    @Nullable private PlayerEntity customer;

    protected PillagerQuestEntity(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
        this.questData = new QuestData(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE,16.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE,-1);
    }

    public static DefaultAttributeContainer.Builder createAttribute(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH,80.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.349999994039535)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,5.0);
    }

    @Override
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

        if(this.shouldRestock()){
            this.restock();
        }


        super.mobTick();
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

    private void levelUp() {
        QuestData.Trustful trustful = this.questData.getTrust(this.lastCustomer.getUuid());
        trustful.setLevel(trustful.getLevel()+1);
        this.questData.fillTrade(trustful.getTradeOffers(),this.lastCustomer,2);
    }

    @Override
    public void addBonusForWave(int wave, boolean unused) {

    }

    @Override
    public SoundEvent getCelebratingSound() {
        return null;
    }

    @Override
    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    @Override
    public QuestData getQuestData() {
        return this.questData;
    }

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

        UUID uuid = this.customer.getUuid();
        this.questData.increaseXp(uuid,offer.getMerchantExperience());
        if(VillagerData.canLevelUp(level) && trustful.getXp() >= QuestData.Trustful.getUpperLevelExperience(level)) {
            this.levelUpTimer = 40;
            this.levelingUp = true;
            xp += 5;
        }

        if(offer.shouldRewardPlayerExperience()){
            this.getWorld().spawnEntity(new ExperienceOrbEntity(this.getWorld(),this.getX(),this.getY()+0.5d,this.getZ(),xp));
        }
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
        return null;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }

    public boolean shouldRestock() {
        return this.getWorld().getTime() > this.lastRestockTime + (this.getWorld().getGameRules().getInt(CivilizedMobs.QUEST_RESTOCK_TIME) * 20L);
    }

    public void restock(){
        List<UUID> uuids = this.questData.getEntityTrust().keySet().stream().toList();
        for(UUID uuid1 : uuids){
            PlayerEntity player = this.getWorld().getPlayerByUuid(uuid1);
            if(player != null){
                QuestData.Trustful trustful = this.questData.getTrust(uuid1);
                trustful.setTradeList(new TradeOfferList());
                this.questData.fillTrade(trustful.getTradeOffers(),player,trustful.getLevel() * 2);

                this.lastRestockTime = this.getWorld().getTime();
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    //Crossbow
    @Override
    public void setCharging(boolean charging) {

    }

    @Override
    public void shoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray) {

    }

    @Override
    public void postShoot() {

    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {

    }
}
