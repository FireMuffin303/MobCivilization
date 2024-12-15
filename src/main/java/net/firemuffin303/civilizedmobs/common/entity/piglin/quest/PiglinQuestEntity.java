package net.firemuffin303.civilizedmobs.common.entity.piglin.quest;

import com.mojang.serialization.Dynamic;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.ModWorkerOffers;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestContainer;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestData;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiPredicate;

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
        this.questData = new QuestData(this,ModWorkerOffers.PIGLIN_QUEST_OFFER);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE,16.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE,-1);
    }

    //--- Attribute & Spawn ---
    public static DefaultAttributeContainer.Builder createAttribute(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH,80.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.349999994039535)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,5.0);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.equipStack(EquipmentSlot.MAINHAND,new ItemStack(Items.GOLDEN_AXE));
        return super.initialize(world,difficulty,spawnReason,entityData,entityNbt);
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

    //-- Interact Logic --

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive()){
            if(!this.getWorld().isClient && this.getCustomer() == null && (PiglinBrain.wearsGoldArmor(player) || player.getAbilities().creativeMode)){
                this.prepareOffersFor(player);
                this.setCustomer(player);
                this.sendOffers(player,this.getDisplayName(),this.questData.getTrust(this.getCustomer().getUuid()).getLevel());
            }else if(this.getWorld().isClient){
                this.playSound(SoundEvents.ENTITY_PIGLIN_ANGRY,this.getSoundVolume(),this.getSoundPitch());
                return ActionResult.success(this.getWorld().isClient);
            }
        }

        return ActionResult.success(this.getWorld().isClient);
    }

    protected void mobTick() {
        this.getWorld().getProfiler().push("piglinQuestBrain");
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


        PiglinQuestBrain.tickActivities(this);
        super.mobTick();
    }

    private void levelUp() {
        QuestData.Trustful trustful = this.questData.getTrust(this.lastCustomer.getUuid());
        trustful.setLevel(trustful.getLevel()+1);
        this.questData.fillTrade(trustful.getTradeOffers(),this.lastCustomer,2);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.releaseTicketFor(MemoryModuleType.MEETING_POINT);
        super.onDeath(damageSource);
    }

    //Brain


    @Override
    protected Brain.Profile<PiglinQuestEntity> createBrainProfile() {
        return Brain.createProfile(PiglinQuestBrain.MEMORY_MODULES,PiglinQuestBrain.SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return PiglinQuestBrain.create(this,this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<PiglinQuestEntity> getBrain() {
        return (Brain<PiglinQuestEntity>) super.getBrain();
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

    public void releaseTicketFor( MemoryModuleType<GlobalPos> pos) {
        if (this.getWorld() instanceof ServerWorld) {
            MinecraftServer minecraftServer = ((ServerWorld)this.getWorld()).getServer();
            this.getBrain().getOptionalRegisteredMemory(pos).ifPresent((posx) -> {
                ServerWorld serverWorld = minecraftServer.getWorld(posx.getDimension());
                if (serverWorld != null) {
                    PointOfInterestStorage pointOfInterestStorage = serverWorld.getPointOfInterestStorage();
                    Optional<RegistryEntry<PointOfInterestType>> optional = pointOfInterestStorage.getType(posx.getPos());
                    BiPredicate<PiglinQuestEntity, RegistryEntry<PointOfInterestType>> biPredicate = PiglinQuestBrain.POINTS_OF_INTEREST.get(pos);
                    if (optional.isPresent() && biPredicate.test(this,optional.get())) {
                        pointOfInterestStorage.releaseTicket(posx.getPos());
                        DebugInfoSender.sendPointOfInterest(serverWorld, posx.getPos());
                    }

                }
            });
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
        return SoundEvents.ENTITY_PIGLIN_ADMIRING_ITEM;
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
        //geoAnimatableAnimationState.setAnimation(RawAnimation.begin().then("animation.civil_piglin.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }


}
