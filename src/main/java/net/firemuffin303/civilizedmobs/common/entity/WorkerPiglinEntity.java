package net.firemuffin303.civilizedmobs.common.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.registry.ModEntityInteraction;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.poi.PointOfInterestType;
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
import java.util.function.BiPredicate;

//TODO : Turn Worker Logic to Interface for easier entity Setup;
public class WorkerPiglinEntity extends AbstractPiglinEntity implements GeoEntity, InteractionObserver, Merchant,WorkerContainer {
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

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WorkerPiglinEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
        this.gossip = new VillagerGossips();
        ((MobNavigation)this.getNavigation()).setCanPathThroughDoors(true);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive()){
            boolean bl = this.getOffers().isEmpty();
            // If TraderOffer is Empty, Player cannot trade.
            if(bl){
                if(!this.getWorld().isClient){
                    this.playSound(SoundEvents.ENTITY_PIGLIN_ANGRY,this.getSoundVolume(),this.getSoundPitch());
                }
                return ActionResult.success(this.getWorld().isClient);
            }else{
                // If TraderOffer is present, prepare Offers, set Customer and Show Trade UI.
                if(!this.getWorld().isClient && this.getCustomer() == null  && this.offers != null && !this.offers.isEmpty()){
                    this.prepareOffersFor(player);
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
        super.onDeath(damageSource);
        this.resetCustomer();
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if(nbt.contains("WorkerData",10)){
            DataResult<WorkerData> dataDataResult = WorkerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE,nbt.get("WorkerData")));
            Logger logger = CivilizedMobs.LOGGER;
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
        Logger logger = CivilizedMobs.LOGGER;
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
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,5.0);
    }

    public static boolean canSpawn(EntityType<PiglinEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return !world.getBlockState(pos.down()).isOf(Blocks.NETHER_WART_BLOCK);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    //--- Piglin ---

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
        Map<Integer,List<TradeOffer>> integerListMap = ModWorkerOffers.PIGLIN_TRADES.get(workerData.getProfession());
        if(integerListMap == null){ return; }
        List<TradeOffer> tradeOffers = integerListMap.get(workerData.getLevel());
        if(!tradeOffers.isEmpty()){
            TradeOfferList tradeOfferList = this.getOffers();
            this.fillRecipesFromPool(this,tradeOfferList,tradeOffers,2);
        }
    }

    // Gossip
    public int getReputation(PlayerEntity player) {
        return this.gossip.getReputationFor(player.getUuid(), (gossipType) -> {
            return true;
        });
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

    private void prepareOffersFor(PlayerEntity player) {
        int i = this.getReputation(player);
        if (i != 0) {
            for (TradeOffer tradeOffer : this.getOffers()) {
                tradeOffer.increaseSpecialPrice(-MathHelper.floor((float) i * tradeOffer.getPriceMultiplier()));
            }
        }

        if (player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)) {
            StatusEffectInstance statusEffectInstance = player.getStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE);
            int j = statusEffectInstance.getAmplifier();

            for (TradeOffer tradeOffer2 : this.getOffers()) {
                double d = 0.3 + 0.0625 * (double) j;
                int k = (int) Math.floor(d * (double) tradeOffer2.getOriginalFirstBuyItem().getCount());
                tradeOffer2.increaseSpecialPrice(-Math.max(k, 1));
            }
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

    //------- Gecko Lib-------

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

    static {
        POINTS_OF_INTEREST = ImmutableMap.of(MemoryModuleType.JOB_SITE, (civilizedPiglinEntity, registryEntry) -> {
            return civilizedPiglinEntity.getWorkerData().getProfession().heldWorkstation().test(registryEntry);
        });

        PIGLIN_DATA = DataTracker.registerData(WorkerPiglinEntity.class, ModEntityType.WORKER_DATA);
    }



}

