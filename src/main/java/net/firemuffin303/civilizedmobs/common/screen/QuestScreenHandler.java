package net.firemuffin303.civilizedmobs.common.screen;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.SimpleQuestContainer;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestContainer;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestData;
import net.firemuffin303.civilizedmobs.common.quest.QuestList;
import net.firemuffin303.civilizedmobs.registry.ModScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class QuestScreenHandler extends ScreenHandler {
    private int xpProgress;
    private int level;
    private QuestList quests;
    private final QuestContainer questContainer;

    private final Inventory inventory = new SimpleInventory(2);

    public QuestScreenHandler(int syncID,PlayerInventory playerInventory){
        this(syncID,playerInventory,new SimpleQuestContainer());
    }

    //Real Use
    public QuestScreenHandler(int syncId, PlayerInventory playerInventory,QuestContainer questContainer) {
        super(ModScreenHandlerType.QUEST_SCREEN, syncId);
        this.questContainer = questContainer;

        this.addSlot(new Slot(this.inventory, 0, 136, 37));
        this.addSlot(new Slot(this.inventory, 1, 162, 37));
        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 108 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 108 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public void setXpProgress(int xpProgress) {
        this.xpProgress = xpProgress;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXpProgress() {
        return xpProgress;
    }

    public int getLevel() {
        return level;
    }

    public void setQuestList(QuestList questList) {
        this.quests = questList;
    }

    public QuestList getQuestList() {
        return this.quests;
    }

    public void sendQuest(PlayerEntity player,int index) {
        this.questContainer.getQuestData().getQuestList(player).remove(index);
        this.screenUpdate(player,this.questContainer.getQuestData());
    }

    public void screenUpdate(PlayerEntity player,QuestData questData){
        QuestData.Trustful trustful = questData.getTrust(player.getUuid());
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeInt(this.syncId);
        questData.getQuestList(player).toPacket(packetByteBuf);
        packetByteBuf.writeInt(trustful.getXp());
        packetByteBuf.writeInt(trustful.getLevel());
        ServerPlayNetworking.send((ServerPlayerEntity) player,CivilizedMobs.QUEST_SCREEN_PAYLOAD_ID,packetByteBuf);
    }
}
