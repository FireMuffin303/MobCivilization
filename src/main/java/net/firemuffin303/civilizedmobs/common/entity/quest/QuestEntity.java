package net.firemuffin303.civilizedmobs.common.entity.quest;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.quest.QuestList;
import net.firemuffin303.civilizedmobs.common.screen.QuestScreenHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.OptionalInt;

public class QuestEntity extends PathAwareEntity implements QuestContainer, GeoEntity {
    QuestData questData = new QuestData();

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public QuestEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(this.isAlive()){
            if(!this.getWorld().isClient){
                OptionalInt optionalInt = player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player1) -> {
                    return new QuestScreenHandler(syncId,playerInventory);
                }, Text.of(this.getDisplayName().getString())));

                if(optionalInt.isPresent()){
                    QuestList questList = this.questData.getQuestList(player.getUuid(),this.random,this.getWorld().getRegistryManager());
                    QuestData.Trustful trustful = this.questData.getTrust(player.getUuid());
                    PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());

                    packetByteBuf.writeInt(optionalInt.getAsInt());
                    questList.toPacket(packetByteBuf);
                    packetByteBuf.writeInt(trustful.getXp());
                    packetByteBuf.writeInt(trustful.getLevel());


                    ServerPlayNetworking.send((ServerPlayerEntity) player, CivilizedMobs.QUEST_SCREEN_PAYLOAD_ID,packetByteBuf);
                }


            }
            return ActionResult.success(this.getWorld().isClient);
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.questData.writeData(nbt);

    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.questData.readData(nbt);
    }

    //--- Quest Data ----
    @Override
    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    @Override
    public QuestData getQuestData() {
        return this.questData;
    }

    //GeoAnimatable
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
