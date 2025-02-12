package net.firemuffin303.villagefoe.common.entity.piglin.worker;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.common.entity.LeaderSummoner;
import net.firemuffin303.villagefoe.common.entity.piglin.PiglinTradeOffers;
import net.firemuffin303.villagefoe.common.entity.WorkerContainer;
import net.firemuffin303.villagefoe.common.entity.WorkerData;
import net.firemuffin303.villagefoe.common.entity.witherSkelton.quest.WitherSkeletonQuestEntity;
import net.firemuffin303.villagefoe.common.entity.witherSkelton.worker.WitherSkeletonWorkerEntity;
import net.firemuffin303.villagefoe.registry.ModBrains;
import net.firemuffin303.villagefoe.registry.ModEntityInteraction;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.firemuffin303.villagefoe.registry.ModTags;
import net.minecraft.block.Blocks;
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
import net.minecraft.entity.mob.*;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.*;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

//TODO : Turn Worker Logic to Interface for easier entity Setup;
public class WorkerPiglinEntity extends AbstractPiglinEntity implements GeoEntity, InteractionObserver, Merchant, LeaderSummoner, WorkerContainer,CrossbowUser {
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<WorkerPiglinEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST;
    private static final TrackedData<WorkerData> PIGLIN_DATA;
    private long lastRestockTime;
    private int experience;
    private int levelUpTimer;
    private boolean levelingUp;
    private VillagerGossips gossip;
    private long lastGossipDecayTime;

    @Nullable
    private PlayerEntity customer;

    @Nullable
    private PlayerEntity lastCustomer;

    @Nullable
    protected TradeOfferList offers;

    private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(WorkerPiglinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WorkerPiglinEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
        this.gossip = new VillagerGossips();
        ((MobNavigation)this.getNavigation()).setCanPathThroughDoors(true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE,16.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE,-1);
    }



    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive()){
            boolean bl = this.getOffers().isEmpty();
            // If TraderOffer is Empty, Player cannot trade.
            if(bl || player.isCreative() ? false : !PiglinBrain.wearsGoldArmor(player)){
                if(!this.getWorld().isClient){
                    this.playSound(SoundEvents.ENTITY_PIGLIN_ANGRY,this.getSoundVolume(),this.getSoundPitch());
                }
                return ActionResult.success(this.getWorld().isClient);
            }else{
                // If TraderOffer is present, prepare Offers, set Customer and Show Trade UI.
                if(!this.getWorld().isClient && this.getCustomer() == null  && this.offers != null && !this.offers.isEmpty()){
                    this.prepareOffersFor(player,StatusEffects.HERO_OF_THE_VILLAGE);
                    this.setCustomer(player);
                    this.sendOffers(player,this.getDisplayName(),this.getWorkerData().getLevel());
                }
                return ActionResult.success(this.getWorld().isClient);
            }

        }
        return super.interactMob(player,hand);
    }

    public void setOffers(TradeOfferList offers){
        this.offers = offers;
    }

    // Add Gossip Hurt when being hit.
    public void setAttacker(@Nullable LivingEntity attacker) {
        if (attacker != null && this.getWorld() instanceof ServerWorld) {
            ((ServerWorld)this.getWorld()).handleInteraction(ModEntityInteraction.WORKER_PIGLIN_HURT, attacker, this);
            if (this.isAlive() && attacker instanceof PlayerEntity) {
                this.getWorld().sendEntityStatus(this, (byte)13);
            }
        }

        super.setAttacker(attacker);
    }

    //--- Data ---

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PIGLIN_DATA,new WorkerData(VillagerProfession.NONE,1));
        this.dataTracker.startTracking(CHARGING,false);
    }

    protected void mobTick() {
        this.getWorld().getProfiler().push("piglinBrain");
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

        //Gossip Trade
        if (this.lastCustomer != null && this.getWorld() instanceof ServerWorld) {
            ((ServerWorld)this.getWorld()).handleInteraction(EntityInteraction.TRADE, this.lastCustomer, this);
            this.getWorld().sendEntityStatus(this, (byte)14);
            this.lastCustomer = null;
        }

        CivilPiglinBrain.tickActivities(this);
        super.mobTick();
    }

    @Override
    public void tick() {
        super.tick();
        this.decayGossip();
    }

    private void levelUp() {
        this.setWorkerData(this.getWorkerData().withLevel(this.getWorkerData().getLevel()+1));
        this.fillTrade();
    }

    @Override
    public @Nullable Entity moveToWorld(ServerWorld destination) {
        this.resetCustomer();
        return super.moveToWorld(destination);
    }

    private void resetCustomer() {
        this.setCustomer(null);
        this.clearSpecialPrices();
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.releaseTicketFor(this,MemoryModuleType.JOB_SITE);
        this.releaseTicketFor(this,MemoryModuleType.POTENTIAL_JOB_SITE);
        this.resetCustomer();
        super.onDeath(damageSource);
    }

    public void releaseTicketFor(WorkerPiglinEntity entity,MemoryModuleType<GlobalPos> pos) {
        if (entity.getWorld() instanceof ServerWorld) {
            MinecraftServer minecraftServer = ((ServerWorld)entity.getWorld()).getServer();
            entity.getBrain().getOptionalRegisteredMemory(pos).ifPresent((posx) -> {
                ServerWorld serverWorld = minecraftServer.getWorld(posx.getDimension());
                if (serverWorld != null) {
                    PointOfInterestStorage pointOfInterestStorage = serverWorld.getPointOfInterestStorage();
                    Optional<RegistryEntry<PointOfInterestType>> optional = pointOfInterestStorage.getType(posx.getPos());
                    BiPredicate<WorkerPiglinEntity, RegistryEntry<PointOfInterestType>> biPredicate = POINTS_OF_INTEREST.get(pos);
                    if (optional.isPresent() && biPredicate.test(entity,optional.get())) {
                        pointOfInterestStorage.releaseTicket(posx.getPos());
                        DebugInfoSender.sendPointOfInterest(serverWorld, posx.getPos());
                    }

                }
            });
        }
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

    //--- Brain ---

    @Override
    protected Brain.Profile<WorkerPiglinEntity> createBrainProfile() {
        return Brain.createProfile(CivilPiglinBrain.MEMORY_MODULES,CivilPiglinBrain.SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return CivilPiglinBrain.create(this,this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<WorkerPiglinEntity> getBrain() {
        return (Brain<WorkerPiglinEntity>) super.getBrain();
    }

    public void reinitializeBrain(ServerWorld world) {
        Brain<WorkerPiglinEntity> brain = this.getBrain();
        brain.stopAllTasks(world,this);
        this.brain = brain.copy();
        CivilPiglinBrain.create(this,this.getBrain());
    }

    //--- Attribute & Spawn ---

    public static DefaultAttributeContainer.Builder createAttribute(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH,16.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.349999994039535)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,2.0);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if(world.getRandom().nextFloat() < 0.5f){
            if(random.nextFloat() > 0.5f){
                this.equipStack(EquipmentSlot.MAINHAND,new ItemStack(Items.CROSSBOW));
            }else {
                this.equipStack(EquipmentSlot.MAINHAND,new ItemStack(Items.GOLDEN_SWORD));
            }
        }
        this.brain.remember(ModBrains.LEADER_DETECTED_RECENTLY,true,200L);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }

    public static boolean canSpawn(EntityType<PiglinEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return !world.getBlockState(pos.down()).isOf(Blocks.NETHER_WART_BLOCK);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        Entity entity = source.getAttacker();
        if (entity instanceof CreeperEntity creeperEntity) {
            if (creeperEntity.shouldDropHead()) {
                ItemStack itemStack = new ItemStack(Items.PIGLIN_HEAD);
                creeperEntity.onHeadDropped();
                this.dropStack(itemStack);
            }
        }
    }

    //--- Sound ---
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PIGLIN_DEATH;
    }

    //--- Piglin ---

    @Override
    protected boolean canHunt() {
        return false;
    }

    @Override
    public PiglinActivity getActivity() {
        if(this.dataTracker.get(CHARGING)){
            return PiglinActivity.CROSSBOW_CHARGE;
        }else if(this.isAttacking() && this.isHolding(Items.CROSSBOW)){
            return PiglinActivity.CROSSBOW_HOLD;
        }else if(this.isAttacking() && this.isHoldingTool()){
            return PiglinActivity.ATTACKING_WITH_MELEE_WEAPON;
        }
        return PiglinActivity.DEFAULT;
    }

    @Override
    protected void playZombificationSound() {

    }

    //------- Merchant -------
    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        boolean bl = this.getCustomer() != null && customer == null;
        this.customer = customer;
        if(bl){
            this.resetCustomer();
        }

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
        return SoundEvents.ENTITY_VILLAGER_YES;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }

    @Override
    public boolean canRefreshTrades() {
        return true;
    }

    // ------ Worker Data ------
    @Override
    public void setWorkerData(WorkerData workerData) {
        WorkerData workerData1 = this.getWorkerData();
        if(workerData1.getProfession() != workerData.getProfession()){
            this.offers = null;
        }
        this.dataTracker.set(PIGLIN_DATA, workerData);
    }

    @Override
    public WorkerData getWorkerData() {
        return this.dataTracker.get(PIGLIN_DATA);
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
        Map<Integer,List<TradeOffers.Factory>> integerListMap = PiglinTradeOffers.PIGLIN_TRADES.get(workerData.getProfession());
        if(integerListMap == null){ return; }
        List<TradeOffers.Factory> factories = integerListMap.get(workerData.getLevel());
        if(!factories.isEmpty()){
            TradeOfferList tradeOfferList = this.getOffers();
            this.fillRecipesFromPool(this,tradeOfferList,factories,2);
        }
    }

    // Gossip
    public int getReputation(PlayerEntity player) {
        return this.gossip.getReputationFor(player.getUuid(), (gossipType) -> {
            return true;
        });
    }

    @Override
    public TradeOfferList getMerchantOffers() {
        return this.getOffers();
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

    // ------ Interaction Observer ------

    @Override
    public void onInteractionWith(EntityInteraction interaction, Entity entity) {
        if (interaction == EntityInteraction.ZOMBIE_VILLAGER_CURED) {
            this.gossip.startGossip(entity.getUuid(), VillageGossipType.MAJOR_POSITIVE, 20);
            this.gossip.startGossip(entity.getUuid(), VillageGossipType.MINOR_POSITIVE, 25);
        } else if (interaction == EntityInteraction.TRADE) {
            this.gossip.startGossip(entity.getUuid(), VillageGossipType.TRADING, 2);
        } else if (interaction == ModEntityInteraction.WORKER_PIGLIN_HURT) {
            this.gossip.startGossip(entity.getUuid(), VillageGossipType.MINOR_NEGATIVE, 25);
        } else if (interaction == ModEntityInteraction.WORKER_PIGLIN_KILLED) {
            this.gossip.startGossip(entity.getUuid(), VillageGossipType.MAJOR_NEGATIVE, 25);
        }
    }

    //Crossbow


    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == Items.CROSSBOW;
    }

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
    public void attack(LivingEntity target, float pullProgress) {
        this.shoot(this,1.6f);
    }

    //------- Gecko Lib-------

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this,"controller",5,this::registerControllers));
    }

    private PlayState registerControllers(AnimationState<GeoAnimatable> geoAnimatableAnimationState) {
        geoAnimatableAnimationState.setAnimation(RawAnimation.begin().then("animation.civil_piglin.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    //------- Leader Summoner -----------
    @Override
    public void summonLeader(ServerWorld serverWorld, long worldTime) {
        if(canSummonLeader()){
            Box box = this.getBoundingBox().expand(10d,10d,10d);
            Stream<PointOfInterest> pointOfInterestStream = serverWorld.getPointOfInterestStorage().getInSquare(
                    registryEntry -> registryEntry.matchesKey(PointOfInterestTypes.MEETING),
                    this.getBlockPos(),
                    8,
                    PointOfInterestStorage.OccupationStatus.ANY
            );

            List<AbstractPiglinEntity> entities = serverWorld.getNonSpectatingEntities(AbstractPiglinEntity.class,box);

            if(pointOfInterestStream.findFirst().isPresent() && entities.size() > 1){
                if(LargeEntitySpawnHelper.trySpawnAt(ModEntityType.PIGLIN_LEADER_ENTITY,SpawnReason.MOB_SUMMONED,serverWorld,this.getBlockPos(),10,8,6, WitherSkeletonQuestEntity.WITHER_LEADER).isPresent()){
                    this.brain.remember(ModBrains.LEADER_DETECTED_RECENTLY,true,12000L);
                    for (AbstractPiglinEntity abstractPiglinEntity:entities){
                        if(abstractPiglinEntity instanceof WorkerPiglinEntity workerPiglinEntity){
                            workerPiglinEntity.brain.remember(ModBrains.LEADER_DETECTED_RECENTLY,true,12000L);
                        }
                    }

                }
            }
        }
    }

    @Override
    public boolean canSummonLeader() {
        return !this.brain.hasMemoryModule(ModBrains.LEADER_DETECTED_RECENTLY);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    static {
        POINTS_OF_INTEREST = ImmutableMap.of(
                MemoryModuleType.JOB_SITE, (civilizedPiglinEntity, registryEntry) -> {
                    return civilizedPiglinEntity.getWorkerData().getProfession().heldWorkstation().test(registryEntry);
        },
                MemoryModuleType.POTENTIAL_JOB_SITE,(workerPiglinEntity, registryEntry) -> {
                    Predicate<RegistryEntry<PointOfInterestType>> IS_ACQUIRABLE_JOB_SITE = poi -> poi.isIn(ModTags.PIGLIN_ACQUIRABLE_JOB_SITE);
                    return IS_ACQUIRABLE_JOB_SITE.test(registryEntry);
        });

        PIGLIN_DATA = DataTracker.registerData(WorkerPiglinEntity.class, ModEntityType.WORKER_DATA);
    }



}

