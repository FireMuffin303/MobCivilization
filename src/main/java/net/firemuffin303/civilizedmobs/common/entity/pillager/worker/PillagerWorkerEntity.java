package net.firemuffin303.civilizedmobs.common.entity.pillager.worker;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.ModWorkerOffers;
import net.firemuffin303.civilizedmobs.common.entity.WorkerContainer;
import net.firemuffin303.civilizedmobs.common.entity.WorkerData;
import net.firemuffin303.civilizedmobs.common.entity.brain.IllagerHostileSensor;
import net.firemuffin303.civilizedmobs.registry.ModEntityInteraction;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.firemuffin303.civilizedmobs.registry.ModTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.MobNavigation;
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
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.*;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class PillagerWorkerEntity extends IllagerEntity implements InteractionObserver, Merchant, WorkerContainer, CrossbowUser, GeoEntity {
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<PillagerWorkerEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST =
            ImmutableMap.of(
                    MemoryModuleType.JOB_SITE,(workerEntity,registryEntry) ->{
                        return workerEntity.getWorkerData().getProfession().heldWorkstation().test(registryEntry);
                    },
                    MemoryModuleType.HOME,(workerEntity,registryEntry) ->{
                        return registryEntry.matchesKey(PointOfInterestTypes.HOME);
                    },
                    MemoryModuleType.POTENTIAL_JOB_SITE, (workerEntity,registryEntry) -> {
                        return registryEntry.isIn(ModTags.ILLAGER_ACQUIRABLE_JOB_SITE);
                    }
            );


    private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(PillagerWorkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<WorkerData> WORKER_DATA = DataTracker.registerData(PillagerWorkerEntity.class, ModEntityType.WORKER_DATA);

    @Nullable
    private PlayerEntity customer;
    @Nullable
    private PlayerEntity lastCustomer;

    @Nullable
    private TradeOfferList offers;

    private int levelUpTimer;
    private boolean levelingUp;

    private long lastRestockTime;
    private long dailyRestockTime;
    private int restockToday;

    private int experience;
    private VillagerGossips gossip;
    private long lastGossipDecayTime;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public PillagerWorkerEntity(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
        this.gossip = new VillagerGossips();
        ((MobNavigation)this.getNavigation()).setCanPathThroughDoors(true);
        this.getNavigation().setCanSwim(true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE,16.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE,-1);
    }

    public static DefaultAttributeContainer.Builder createPillagerAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355).add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
    }

    //InitGoals is here because we need to replace Goal AI with Brain Instead.
    @Override
    protected void initGoals() {
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive()){
            boolean bl = this.getOffers().isEmpty();
            // If TraderOffer is Empty, Player cannot trade.
            if(bl || player.isCreative() ? false : !IllagerHostileSensor.isHoldingOminousBanner(player)){
                if(!this.getWorld().isClient){
                    this.playSound(SoundEvents.ENTITY_PIGLIN_ANGRY,this.getSoundVolume(),this.getSoundPitch());
                }
                return ActionResult.success(this.getWorld().isClient);
            }else{
                // If TraderOffer is present, prepare Offers, set Customer and Show Trade UI.
                if(!this.getWorld().isClient && this.getCustomer() == null  && this.offers != null && !this.offers.isEmpty()){
                    this.prepareOffersFor(player,StatusEffects.BAD_OMEN);
                    this.setCustomer(player);
                    this.sendOffers(player,this.getDisplayName(),this.getWorkerData().getLevel());
                }
                return ActionResult.success(this.getWorld().isClient);
            }

        }
        return super.interactMob(player,hand);
    }


    @Override
    public void wakeUp() {
        super.wakeUp();
        this.brain.remember(MemoryModuleType.LAST_WOKEN,this.getWorld().getTime());
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    public int getReputation(PlayerEntity player) {
        return this.gossip.getReputationFor(player.getUuid(), (gossipType) -> true);
    }

    @Override
    public TradeOfferList getMerchantOffers() {
        return this.getOffers();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CHARGING,false);
        this.dataTracker.startTracking(WORKER_DATA,new WorkerData(VillagerProfession.NONE,1));
    }

    @Override
    protected void mobTick() {
        ServerWorld serverWorld = (ServerWorld) this.getWorld();
        serverWorld.getProfiler().push("workerBrain");
        this.getBrain().tick(serverWorld, this);
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

        if (this.lastCustomer != null && this.getWorld() instanceof ServerWorld) {
            ((ServerWorld)this.getWorld()).handleInteraction(EntityInteraction.TRADE, this.lastCustomer, this);
            this.getWorld().sendEntityStatus(this, (byte)14);
            this.lastCustomer = null;
        }

        Brain<PillagerWorkerEntity> brain = this.getBrain();
        this.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));

        //We don't need tickActivities for Schedule AI
        //PillagerWorkerBrain.tickActivities(this);
        super.mobTick();
    }

    @Override
    public void tick() {
        super.tick();

        long l = this.getWorld().getTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = l;
        } else if (l >= this.lastGossipDecayTime + 24000L) {
            this.gossip.decay();
            this.lastGossipDecayTime = l;
        }
    }

    //Data
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        //WorkerData
        DataResult<NbtElement> dataResult = WorkerData.CODEC.encodeStart(NbtOps.INSTANCE,this.getWorkerData());
        Logger logger = CivilizedMobs.LOGGER;
        Objects.requireNonNull(logger);
        dataResult.resultOrPartial(logger::error).ifPresent(nbtElement -> {
            nbt.put("WorkerData",nbtElement);
        });

        //Offers
        TradeOfferList tradeOffers = this.getOffers();
        if(!tradeOffers.isEmpty()){
            nbt.put("Offers",tradeOffers.toNbt());
        }

        nbt.putLong("LastRestock",this.lastRestockTime);
        nbt.putLong("DailyRestockTime",this.dailyRestockTime);
        nbt.putInt("RestockToday",this.restockToday);
        nbt.putInt("Xp",this.experience);


        nbt.putLong("LastGossipDecay",this.lastGossipDecayTime);
        nbt.put("Gossips", this.gossip.serialize(NbtOps.INSTANCE));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if(nbt.contains("WorkerData",10)){
            DataResult<WorkerData> dataDataResult = WorkerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE,nbt.get("WorkerData")));
            Objects.requireNonNull(CivilizedMobs.LOGGER);
            dataDataResult.resultOrPartial(CivilizedMobs.LOGGER::error).ifPresent(this::setWorkerData);
        }

        if(nbt.contains("Offers",10)){
            this.offers = new TradeOfferList(nbt.getCompound("Offers"));
        }

        this.lastRestockTime = nbt.getLong("LastRestock");
        this.dailyRestockTime = nbt.getLong("DailyRestockTime");
        this.restockToday = nbt.getInt("RestockToday");
        this.experience = nbt.getInt("Xp");

        NbtList nbtList = nbt.getList("Gossips", 10);
        this.gossip.deserialize(new Dynamic<>(NbtOps.INSTANCE, nbtList));
        this.lastGossipDecayTime = nbt.getLong("LastGossipDecay");


        if (this.getWorld() instanceof ServerWorld) {
            this.reinitializeBrain((ServerWorld)this.getWorld());
        }
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.equipStack(EquipmentSlot.MAINHAND,new ItemStack(Items.CROSSBOW));
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    //Disable Despawn
    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    //--- Brain ---
    @Override
    protected Brain.Profile<PillagerWorkerEntity> createBrainProfile() {
        return Brain.createProfile(PillagerWorkerBrain.MEMORY_MODULES,PillagerWorkerBrain.SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return PillagerWorkerBrain.create(this,this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<PillagerWorkerEntity> getBrain() {
        return (Brain<PillagerWorkerEntity>) super.getBrain();
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.releaseTicketFor(this,MemoryModuleType.JOB_SITE);
        this.releaseTicketFor(this,MemoryModuleType.POTENTIAL_JOB_SITE);
        this.releaseTicketFor(this,MemoryModuleType.HOME);

        this.setCustomer(null);
        for (TradeOffer tradeOffer : this.getOffers()) {
            tradeOffer.clearSpecialPrice();
        }

        super.onDeath(damageSource);
    }

    public void releaseTicketFor(PillagerWorkerEntity entity, MemoryModuleType<GlobalPos> pos) {
        if (entity.getWorld() instanceof ServerWorld) {
            MinecraftServer minecraftServer = ((ServerWorld)entity.getWorld()).getServer();
            entity.getBrain().getOptionalRegisteredMemory(pos).ifPresent((posx) -> {
                ServerWorld serverWorld = minecraftServer.getWorld(posx.getDimension());
                if (serverWorld != null) {
                    PointOfInterestStorage pointOfInterestStorage = serverWorld.getPointOfInterestStorage();
                    Optional<RegistryEntry<PointOfInterestType>> optional = pointOfInterestStorage.getType(posx.getPos());
                    BiPredicate<PillagerWorkerEntity, RegistryEntry<PointOfInterestType>> biPredicate = POINTS_OF_INTEREST.get(pos);
                    if (optional.isPresent() && biPredicate.test(entity,optional.get())) {
                        pointOfInterestStorage.releaseTicket(posx.getPos());
                        DebugInfoSender.sendPointOfInterest(serverWorld, posx.getPos());
                    }

                }
            });
        }
    }

    //Crossbow User

    @Override
    public void setCharging(boolean charging) {
        this.dataTracker.set(CHARGING,charging);
    }
    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == Items.CROSSBOW;
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.brain.getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    public void shoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray) {
        this.shoot(this, target, projectile, multiShotSpray, 1.6F);
    }

    @Override
    public void postShoot() {

    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        this.shoot(this,1.6f);
    }

    public boolean isCharging() {
        return this.dataTracker.get(CHARGING);
    }

    //------

    // Raid
    @Override
    public void addBonusForWave(int wave, boolean unused) {

    }

    @Override
    public SoundEvent getCelebratingSound() {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }

    //Worker

    @Override
    public void setWorkerData(WorkerData workerData) {
        WorkerData workerData1 = this.getWorkerData();
        if(workerData1.getProfession() != workerData.getProfession()){
            this.offers = null;
        }
        this.dataTracker.set(WORKER_DATA,workerData);
    }

    @Override
    public WorkerData getWorkerData() {
        return this.dataTracker.get(WORKER_DATA);
    }


    @Override
    public boolean shouldRestock() {
        long restockTimeMoreThanTenMins = this.lastRestockTime + 12000L;
        long worldTime = this.getWorld().getTime();
        long timeOfDay = this.getWorld().getTimeOfDay();

        boolean isLongerThanTenMin = worldTime > restockTimeMoreThanTenMins;

        if(this.dailyRestockTime > 0L){
            long o = this.dailyRestockTime / 24000L;
            long p = timeOfDay / 24000L;
            isLongerThanTenMin |= p > o;
        }

        this.dailyRestockTime = worldTime;
        if(isLongerThanTenMin){
            this.lastRestockTime = worldTime;
            this.restockToday = 0;
        }
        return this.restockToday == 0 || this.restockToday < 2 && this.getWorld().getTime() > this.lastRestockTime + 2400L;
    }

    @Override
    public void restock() {
        for(TradeOffer tradeOffer : this.getOffers()){
            tradeOffer.resetUses();
            this.lastRestockTime = this.getWorld().getTime();
            ++this.restockToday;
        }
    }

    @Override
    public void fillTrade() {
        WorkerData workerData = this.getWorkerData();
        Map<Integer, List<TradeOffers.Factory>> integerListMap = ModWorkerOffers.PILLAGER_TRADES.get(workerData.getProfession());
        if(integerListMap == null){ return; }
        List<TradeOffers.Factory> factories = integerListMap.get(workerData.getLevel());
        if(!factories.isEmpty()){
            TradeOfferList tradeOfferList = this.getOffers();
            this.fillRecipesFromPool(this,tradeOfferList,factories,2);
        }
    }

    @Override
    public void reinitializeBrain(ServerWorld world) {
        Brain<PillagerWorkerEntity> brain = this.getBrain();
        brain.stopAllTasks(world,this);
        this.brain = brain.copy();
        PillagerWorkerBrain.create(this,this.getBrain());
    }

    private void levelUp() {
        this.setWorkerData(this.getWorkerData().withLevel(this.getWorkerData().getLevel()+1));
        this.fillTrade();
    }

    //Interaction Observer
    @Override
    public void onInteractionWith(EntityInteraction interaction, Entity entity) {
        if(interaction == EntityInteraction.VILLAGER_KILLED){
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MAJOR_POSITIVE,5);
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MINOR_POSITIVE,8);

        } else if (interaction == EntityInteraction.GOLEM_KILLED) {
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MAJOR_POSITIVE,10);
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MINOR_POSITIVE,15);

        } else if (interaction == EntityInteraction.TRADE) {
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.TRADING,2);

        }else if(interaction == ModEntityInteraction.ILLAGER_HURT){
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MAJOR_NEGATIVE,25);

        }else if(interaction == ModEntityInteraction.ILLAGER_KILLED){
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MAJOR_NEGATIVE,30);

        } else if (interaction == ModEntityInteraction.ILLAGER_LEADER_KILLED) {
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MAJOR_NEGATIVE,50);
        }
    }

    //--------
    //Merchant
    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        boolean bl = this.getCustomer() != null && customer == null;
        this.customer = customer;
        if(bl){
            this.setCustomer(null);
            for (TradeOffer tradeOffer : this.getOffers()) {
                tradeOffer.clearSpecialPrice();
            }
        }

    }

    @Override
    public @Nullable PlayerEntity getCustomer() {
        return this.customer;
    }

    @Override
    public TradeOfferList getOffers() {
        if(this.offers == null){
            this.offers = new TradeOfferList();
            this.fillTrade();
        }
        return this.offers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {

    }

    @Override
    public void trade(TradeOffer offer) {
        int xp = 3 + this.random.nextInt(4);
        offer.use();
        this.experience += offer.getMerchantExperience();
        this.lastCustomer = this.getCustomer();
        int level = this.getWorkerData().getLevel();
        if(VillagerData.canLevelUp(level) && this.experience >= WorkerData.getUpperLevelExperience(level)){
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
        return this.experience;
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

    //GeoEntity
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }


}
