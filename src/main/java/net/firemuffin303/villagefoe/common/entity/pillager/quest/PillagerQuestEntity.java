package net.firemuffin303.villagefoe.common.entity.pillager.quest;

import com.mojang.serialization.Dynamic;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.common.entity.brain.IllagerHostileSensor;
import net.firemuffin303.villagefoe.common.entity.pillager.IllagerTradeOffers;
import net.firemuffin303.villagefoe.common.entity.quest.QuestContainer;
import net.firemuffin303.villagefoe.common.entity.quest.QuestData;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiPredicate;

public class PillagerQuestEntity extends IllagerEntity implements GeoEntity, Merchant, QuestContainer, CrossbowUser {
    protected QuestData questData;
    protected long lastRestockTime;
    private int levelUpTimer;
    private boolean levelingUp;
    @Nullable private PlayerEntity lastCustomer;
    @Nullable private PlayerEntity customer;

    private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(PillagerQuestEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public PillagerQuestEntity(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
        this.questData = new QuestData(this, IllagerTradeOffers.PILLAGER_QUSET_OFFER);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE,16.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE,-1);
    }

    //Cancel Goals
    @Override
    protected void initGoals() {}

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CHARGING,false);
    }

    public static DefaultAttributeContainer.Builder createAttribute(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH,80.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.349999994039535)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,5.0);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive()){
            if(!this.getWorld().isClient && this.getCustomer() == null && (IllagerHostileSensor.isHoldingOminousBanner(player) || player.getAbilities().creativeMode)){
                this.prepareOffersFor(player);
                this.setCustomer(player);
                this.sendOffers(player,this.getDisplayName(),this.questData.getTrust(this.getCustomer().getUuid()).getLevel());
            }else if(this.getWorld().isClient){
                this.playSound(SoundEvents.ENTITY_WITHER_SKELETON_HURT,this.getSoundVolume(),this.getSoundPitch());
                return ActionResult.success(this.getWorld().isClient);
            }
        }

        return ActionResult.success(this.getWorld().isClient);
    }

    @Override
    protected void mobTick() {
        this.getWorld().getProfiler().push("pillagerQuestBrain");
        this.getBrain().tick((ServerWorld)this.getWorld(), this);
        this.getWorld().getProfiler().pop();
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

        PillagerQuestBrain.tickActivities(this);
        super.mobTick();
    }

    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == Items.CROSSBOW;
    }

    //--- Brain ---
    @Override
    protected Brain.Profile<PillagerQuestEntity> createBrainProfile() {
        return Brain.createProfile(PillagerQuestBrain.MEMORY_MODULES,PillagerQuestBrain.SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return PillagerQuestBrain.create(this,this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<PillagerQuestEntity> getBrain() {
        return (Brain<PillagerQuestEntity>) super.getBrain();
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.equipStack(EquipmentSlot.MAINHAND,new ItemStack(Items.CROSSBOW));
        this.setPatrolLeader(true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
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

    @Override
    public void onDeath(DamageSource damageSource) {
        this.releaseTicketFor(MemoryModuleType.MEETING_POINT);
        super.onDeath(damageSource);
    }

    public void releaseTicketFor( MemoryModuleType<GlobalPos> pos) {
        if (this.getWorld() instanceof ServerWorld) {
            MinecraftServer minecraftServer = ((ServerWorld)this.getWorld()).getServer();
            this.getBrain().getOptionalRegisteredMemory(pos).ifPresent((posx) -> {
                ServerWorld serverWorld = minecraftServer.getWorld(posx.getDimension());
                if (serverWorld != null) {
                    PointOfInterestStorage pointOfInterestStorage = serverWorld.getPointOfInterestStorage();
                    Optional<RegistryEntry<PointOfInterestType>> optional = pointOfInterestStorage.getType(posx.getPos());
                    BiPredicate<PillagerQuestEntity, RegistryEntry<PointOfInterestType>> biPredicate = PillagerQuestBrain.POINTS_OF_INTEREST.get(pos);
                    if (optional.isPresent() && biPredicate.test(this,optional.get())) {
                        pointOfInterestStorage.releaseTicket(posx.getPos());
                        DebugInfoSender.sendPointOfInterest(serverWorld, posx.getPos());
                    }
                }
            });
        }
    }

    //----- Sound -------
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PILLAGER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }
    //----------

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
        return SoundEvents.ENTITY_EVOKER_CELEBRATE;
    }


    // ----QuestData----
    @Override
    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    @Override
    public QuestData getQuestData() {
        return this.questData;
    }

    //Merchant
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
        return SoundEvents.ENTITY_PILLAGER_CELEBRATE;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }

    //--------------

    public boolean shouldRestock() {
        return this.getWorld().getTime() > this.lastRestockTime + (this.getWorld().getGameRules().getInt(VillageFoe.QUEST_RESTOCK_TIME) * 20L);
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

    //Crossbow
    @Override
    public void setCharging(boolean charging) {
        this.dataTracker.set(CHARGING,charging);
    }

    @Override
    public void shoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray) {
        this.shoot(this, target, projectile, multiShotSpray, 1.6F);
    }

    @Override
    public void postShoot() {

    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.brain.getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        this.shoot(this,1.6f);
    }

    public boolean isCharging() {
        return this.dataTracker.get(CHARGING);
    }

    //GeckoLib

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }


}
