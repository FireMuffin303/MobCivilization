package net.firemuffin303.civilizedmobs.common.entity.witherSkelton.quest;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.ModWorkerOffers;
import net.firemuffin303.civilizedmobs.common.entity.brain.WitherSkeletonNemesisSensor;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestContainer;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiPredicate;

public class WitherSkeletonQuestEntity extends WitherSkeletonEntity implements Merchant, QuestContainer, GeoEntity {
    private long lastRestockTime;
    protected QuestData questData;
    private int levelUpTimer;
    private boolean levelingUp;
    @Nullable private PlayerEntity lastCustomer;
    @Nullable private PlayerEntity customer;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WitherSkeletonQuestEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.questData = new QuestData(this,ModWorkerOffers.WITHER_QUSET_OFFER);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE,16.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE,-1);
    }

    public static DefaultAttributeContainer.Builder createAttribute(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH,80.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.349999994039535)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,5.0);
    }

    //Cancel Goals
    @Override
    protected void initGoals() {}

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
            if(!this.getWorld().isClient && this.getCustomer() == null && (WitherSkeletonNemesisSensor.isWearingSkeletonHead(player) || player.getAbilities().creativeMode)){
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
        this.getWorld().getProfiler().push("witherSkeletonQuestBrain");
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

        WitherSkeletonQuestBrain.tickActivities(this);
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

    public void releaseTicketFor( MemoryModuleType<GlobalPos> pos) {
        if (this.getWorld() instanceof ServerWorld) {
            MinecraftServer minecraftServer = ((ServerWorld)this.getWorld()).getServer();
            this.getBrain().getOptionalRegisteredMemory(pos).ifPresent((posx) -> {
                ServerWorld serverWorld = minecraftServer.getWorld(posx.getDimension());
                if (serverWorld != null) {
                    PointOfInterestStorage pointOfInterestStorage = serverWorld.getPointOfInterestStorage();
                    Optional<RegistryEntry<PointOfInterestType>> optional = pointOfInterestStorage.getType(posx.getPos());
                    BiPredicate<WitherSkeletonQuestEntity, RegistryEntry<PointOfInterestType>> biPredicate = WitherSkeletonQuestBrain.POINTS_OF_INTEREST.get(pos);
                    if (optional.isPresent() && biPredicate.test(this,optional.get())) {
                        pointOfInterestStorage.releaseTicket(posx.getPos());
                        DebugInfoSender.sendPointOfInterest(serverWorld, posx.getPos());
                    }

                }
            });
        }
    }

    //Brain
    @Override
    protected Brain.Profile<WitherSkeletonQuestEntity> createBrainProfile() {
        return Brain.createProfile(WitherSkeletonQuestBrain.MEMORY_MODULES,WitherSkeletonQuestBrain.SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return WitherSkeletonQuestBrain.create(this,this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<WitherSkeletonQuestEntity> getBrain() {
        return (Brain<WitherSkeletonQuestEntity>) super.getBrain();
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    //Data
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

    //Quest
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
        return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }

    //Geo
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
        return this.geoCache;
    }
}
