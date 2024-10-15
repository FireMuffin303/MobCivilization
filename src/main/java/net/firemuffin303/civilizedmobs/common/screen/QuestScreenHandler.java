package net.firemuffin303.civilizedmobs.common.screen;

import net.firemuffin303.civilizedmobs.common.SimpleQuestContainer;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestContainer;
import net.firemuffin303.civilizedmobs.common.quest.QuestList;
import net.firemuffin303.civilizedmobs.registry.ModScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.UUID;

public class QuestScreenHandler extends ScreenHandler {
    private int xpProgress;
    private int level;
    private QuestContainer questContainer;
    private final Inventory inventory = new SimpleInventory(2);

    public QuestScreenHandler(int syncId,PlayerInventory playerInventory){
        this(syncId,playerInventory,new SimpleQuestContainer());
    }

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

    public void setQuestList(UUID uuid, QuestList questList) {
        this.questContainer.getQuestData().setQuestList(uuid,questList);
    }

    public QuestList getQuestList() {
        return this.questContainer.getQuestData().getQuestList();
    }

    public void sendQuest(UUID uuid,int index) {
        this.questContainer.getQuestData().getQuestList(uuid,).remove(index);
    }
}
