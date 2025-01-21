package net.firemuffin303.villagefoe.common.entity.witherSkelton.worker;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.common.entity.LeaderSummoner;
import net.firemuffin303.villagefoe.common.entity.WorkerContainer;
import net.firemuffin303.villagefoe.common.entity.WorkerData;
import net.firemuffin303.villagefoe.common.entity.brain.WitherSkeletonNemesisSensor;
import net.firemuffin303.villagefoe.common.entity.witherSkelton.WitherSkeletonTradeOffers;
import net.firemuffin303.villagefoe.common.entity.witherSkelton.quest.WitherSkeletonQuestEntity;
import net.firemuffin303.villagefoe.registry.ModBrains;
import net.firemuffin303.villagefoe.registry.ModEntityInteraction;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.*;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterest;
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
import java.util.stream.Stream;

public class WitherSkeletonWorkerEntity extends WitherSkeletonEntity implements InteractionObserver, Merchant, WorkerContainer, LeaderSummoner, GeoEntity {
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<WitherSkeletonWorkerEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST =
            ImmutableMap.of(MemoryModuleType.JOB_SITE,(witherSkeletonEntity,registryEntity) ->{
                return witherSkeletonEntity.getWorkerData().getProfession().heldWorkstation().test(registryEntity);
            });
    private static final TrackedData<WorkerData> WORKER_DATA = DataTracker.registerData(WitherSkeletonWorkerEntity.class, ModEntityType.WORKER_DATA);

    @Nullable
    private PlayerEntity customer;
    @Nullable
    private PlayerEntity lastCustomer;

    @Nullable
    private TradeOfferList offers;

    private int levelUpTimer;
    private boolean levelingUp;

    private long lastRestockTime;

    private int experience;
    private VillagerGossips gossip;
    private long lastGossipDecayTime;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WitherSkeletonWorkerEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.gossip = new VillagerGossips();
        ((MobNavigation)this.getNavigation()).setCanPathThroughDoors(true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(WORKER_DATA,new WorkerData(VillagerProfession.NONE,1));
    }

    //Cancel Goal AI
    @Override
    protected void initGoals() {}

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive()){
            boolean bl = this.getOffers().isEmpty();
            // If TraderOffer is Empty, Player cannot trade.
            if(bl || player.isCreative() ? false : !WitherSkeletonNemesisSensor.isWearingSkeletonHead(player)){
                if(!this.getWorld().isClient){
                    this.playSound(SoundEvents.ENTITY_WITHER_SKELETON_HURT,this.getSoundVolume(),this.getSoundPitch());
                }
                return ActionResult.success(this.getWorld().isClient);
            }else{
                // If TraderOffer is present, prepare Offers, set Customer and Show Trade UI.
                if(!this.getWorld().isClient && this.getCustomer() == null  && this.offers != null && !this.offers.isEmpty()){
                    this.prepareOffersFor(player, StatusEffects.HERO_OF_THE_VILLAGE);
                    this.setCustomer(player);
                    this.sendOffers(player,this.getDisplayName(),this.getWorkerData().getLevel());
                }
                return ActionResult.success(this.getWorld().isClient);
            }

        }
        return super.interactMob(player,hand);
    }

    @Override
    protected void mobTick() {
        this.getWorld().getProfiler().push("witherSkeletonWorkerBrain");
        this.getBrain().tick((ServerWorld)this.getWorld(), this);
        this.getWorld().getProfiler().pop();

        if (this.customer == null  && this.levelUpTimer > 0) {
            --this.levelUpTimer;
            if (this.levelUpTimer <= 0) {
                if (this.levelingUp) {
                    this.levelUp();
                    this.levelingUp = false;
                }

                this.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 0));
            }

            if (this.lastCustomer != null && this.getWorld() instanceof ServerWorld) {
                ((ServerWorld)this.getWorld()).handleInteraction(EntityInteraction.TRADE, this.lastCustomer, this);
                this.getWorld().sendEntityStatus(this, (byte)14);
                this.lastCustomer = null;
            }
        }

        WitherSkeletonWorkerBrain.tickActivities(this);
        Optional<LivingEntity> attackTarget = this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET);
        if(attackTarget != null){
            this.setAttacking(attackTarget.isPresent());
        }

        super.mobTick();
    }

    private void levelUp() {
        this.setWorkerData(this.getWorkerData().withLevel(this.getWorkerData().getLevel()+1));
        this.fillTrade();
    }

    @Override
    public void tick() {
        super.tick();
        this.decayGossip();
    }

    private void decayGossip() {
        long l = this.getWorld().getTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = l;
        } else if (l >= this.lastGossipDecayTime + 24000L) {
            this.gossip.decay();
            this.lastGossipDecayTime = l;
        }
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return this.dataTracker.get(WORKER_DATA).getProfession() == VillagerProfession.NONE && super.canImmediatelyDespawn(distanceSquared);
    }

    @Override
    public boolean cannotDespawn() {
        return this.dataTracker.get(WORKER_DATA).getProfession() != VillagerProfession.NONE || super.cannotDespawn();
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.releaseTicketFor(this, MemoryModuleType.JOB_SITE,POINTS_OF_INTEREST);
        this.releaseTicketFor(this,MemoryModuleType.POTENTIAL_JOB_SITE,POINTS_OF_INTEREST);
        this.resetCustomer();
        super.onDeath(damageSource);
    }

    @Override
    public @Nullable Entity moveToWorld(ServerWorld destination) {
        this.resetCustomer();
        return super.moveToWorld(destination);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if(nbt.contains("WorkerData",10)){
            DataResult<WorkerData> dataDataResult = WorkerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE,nbt.get("WorkerData")));
            Logger logger = VillageFoe.LOGGER;
            Objects.requireNonNull(logger);
            dataDataResult.resultOrPartial(logger::error).ifPresent(this::setWorkerData);
        }

        if(nbt.contains("Offers",10)){
            this.offers = new TradeOfferList(nbt.getCompound("Offers"));
        }

        this.lastRestockTime = nbt.getLong("LastRestock");
        this.experience = nbt.getInt("Xp");

        NbtList nbtList = nbt.getList("Gossips", 10);
        this.gossip.deserialize(new Dynamic<>(NbtOps.INSTANCE, nbtList));
        this.lastGossipDecayTime = nbt.getLong("LastGossipDecay");

        if (this.getWorld() instanceof ServerWorld) {
            this.reinitializeBrain((ServerWorld)this.getWorld());
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        DataResult<NbtElement> dataResult = WorkerData.CODEC.encodeStart(NbtOps.INSTANCE,this.getWorkerData());
        Logger logger = VillageFoe.LOGGER;
        Objects.requireNonNull(logger);
        dataResult.resultOrPartial(logger::error).ifPresent(nbtElement -> {
            nbt.put("WorkerData",nbtElement);
        });

        TradeOfferList tradeOffers = this.getOffers();
        if(!tradeOffers.isEmpty()){
            nbt.put("Offers",tradeOffers.toNbt());
        }

        nbt.putLong("LastRestock",this.lastRestockTime);
        nbt.putInt("Xp",this.experience);
        nbt.put("Gossips", this.gossip.serialize(NbtOps.INSTANCE));
        nbt.putLong("LastGossipDecay", this.lastGossipDecayTime);
    }

    //Brains
    @Override
    protected Brain.Profile<WitherSkeletonWorkerEntity> createBrainProfile() {
        return Brain.createProfile(WitherSkeletonWorkerBrain.MEMORY_MODULES,WitherSkeletonWorkerBrain.SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return WitherSkeletonWorkerBrain.create(this,this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<WitherSkeletonWorkerEntity> getBrain() {
        return (Brain<WitherSkeletonWorkerEntity>) super.getBrain();
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }
    //----- Summon Leader ------
    public void summonLeader(ServerWorld serverWorld, long worldTime){
        if(canSummonLeader()){
            Box box = this.getBoundingBox().expand(10d,10d,10d);
            Stream<PointOfInterest> pointOfInterestStream = serverWorld.getPointOfInterestStorage().getInSquare(
                    registryEntry -> registryEntry.matchesKey(PointOfInterestTypes.MEETING),
                    this.getBlockPos(),
                    8,
                    PointOfInterestStorage.OccupationStatus.ANY
            );

            List<WitherSkeletonEntity> witherSkeletonEntities = serverWorld.getNonSpectatingEntities(WitherSkeletonEntity.class,box);

            if(pointOfInterestStream.findFirst().isPresent() && witherSkeletonEntities.size() > 1){
                if(LargeEntitySpawnHelper.trySpawnAt(ModEntityType.WITHER_SKELETON_LEADER,SpawnReason.MOB_SUMMONED,serverWorld,this.getBlockPos(),10,8,6,WitherSkeletonQuestEntity.WITHER_LEADER).isPresent()){
                    this.brain.remember(ModBrains.LEADER_DETECTED_RECENTLY,true,12000L);
                    for (WitherSkeletonEntity witherSkeletonEntity:witherSkeletonEntities){
                        if(witherSkeletonEntity instanceof WitherSkeletonWorkerEntity witherSkeletonWorkerEntity){
                            witherSkeletonWorkerEntity.brain.remember(ModBrains.LEADER_DETECTED_RECENTLY,true,12000L);
                        }
                    }

                }
            }
        }

    }

    public boolean canSummonLeader(){
        return !this.brain.hasMemoryModule(ModBrains.LEADER_DETECTED_RECENTLY);
    }

    //----------------------------
    //WorkerData
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
        return this.getWorld().getTime() > this.lastRestockTime + (10L * 20L);
    }

    @Override
    public void restock() {
        for(TradeOffer tradeOffer : this.getOffers()){
            tradeOffer.resetUses();
            this.lastRestockTime = this.getWorld().getTime();
        }
    }

    @Override
    public void fillTrade() {
        WorkerData workerData = this.getWorkerData();
        Map<Integer, List<TradeOffers.Factory>> integerListMap = WitherSkeletonTradeOffers.WITHER_TRADES.get(workerData.getProfession());
        if(integerListMap == null){ return; }
        List<TradeOffers.Factory> factories = integerListMap.get(workerData.getLevel());
        if(!factories.isEmpty()){
            TradeOfferList tradeOfferList = this.getOffers();
            this.fillRecipesFromPool(this,tradeOfferList,factories,2);
        }
    }

    @Override
    public int getReputation(PlayerEntity player) {
        return this.gossip.getReputationFor(player.getUuid(),villageGossipType -> true);
    }

    @Override
    public TradeOfferList getMerchantOffers() {
        return this.getOffers();
    }

    @Override
    public void reinitializeBrain(ServerWorld world) {
        Brain<WitherSkeletonWorkerEntity> brain = this.getBrain();
        brain.stopAllTasks(world,this);
        this.brain = brain.copy();
        WitherSkeletonWorkerBrain.create(this,this.getBrain());
    }

    //--Interaction Observer

    @Override
    public void onInteractionWith(EntityInteraction interaction, Entity entity) {
        if(interaction == ModEntityInteraction.WITHER_SKELETON_KILLED){
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MAJOR_NEGATIVE,5);
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MINOR_NEGATIVE,8);
        } else if (interaction == ModEntityInteraction.WITHER_KILLED) {
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MAJOR_NEGATIVE,50);
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MINOR_NEGATIVE,50);
        } else if (interaction == ModEntityInteraction.WORKER_PIGLIN_KILLED || interaction == ModEntityInteraction.WOLF_KILLED) {
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MAJOR_POSITIVE,5);
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.MINOR_POSITIVE,2);
        } else if (interaction == EntityInteraction.TRADE) {
            this.gossip.startGossip(entity.getUuid(),VillageGossipType.TRADING,2);
        }
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
        this.clearSpecialPrices();
    }

    private void clearSpecialPrices() {
        for (TradeOffer tradeOffer : this.getOffers()) {
            tradeOffer.clearSpecialPrice();
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
        return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }

    @Override
    public boolean canRefreshTrades() {
        return true;
    }

    //Geo
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
