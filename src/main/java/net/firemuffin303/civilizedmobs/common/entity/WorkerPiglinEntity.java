package net.firemuffin303.civilizedmobs.common.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
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
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

public class WorkerPiglinEntity extends AbstractPiglinEntity implements GeoEntity, Merchant,WorkerContainer {
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<WorkerPiglinEntity, RegistryEntry<PointOfInterestType>>> POINTS_OF_INTEREST;
    private static final TrackedData<WorkerData> PIGLIN_DATA;

    @Nullable
    private PlayerEntity customer;

    @Nullable
    protected TradeOfferList offers;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WorkerPiglinEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
        this.offers = new TradeOfferList();
        this.offers.add((new TradeOffer(new ItemStack(Items.GOLD_INGOT),new ItemStack(Items.GOLD_BLOCK),1,2,1.0f)));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive()){
            if(!this.getWorld().isClient && !this.offers.isEmpty()){
                this.setCustomer(player);
                this.sendOffers(player,this.getDisplayName(),this.getWorkerData().getLevel());
            }
            return ActionResult.success(this.getWorld().isClient);
        }
        return super.interactMob(player,hand);
    }

    public void setOffers(TradeOfferList offers){
        this.offers = offers;
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
        CivilPiglinBrain.tickActivities(this);
        super.mobTick();
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
        this.customer = customer;
    }

    @Override
    public @Nullable PlayerEntity getCustomer() {
        return this.customer;
    }

    @Override
    public TradeOfferList getOffers() {
        if(this.offers == null){
            this.offers = new TradeOfferList();
        }
        return this.offers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {

    }

    @Override
    public void trade(TradeOffer offer) {

    }

    @Override
    public void onSellingItem(ItemStack stack) {

    }

    @Override
    public int getExperience() {
        return 0;
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
        return SoundEvents.ENTITY_VILLAGER_YES;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }

    // ------ Worker Data ------
    @Override
    public void setWorkerData(WorkerData workerData) {
        this.dataTracker.set(PIGLIN_DATA, workerData);
    }

    @Override
    public WorkerData getWorkerData() {
        return this.dataTracker.get(PIGLIN_DATA);
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

