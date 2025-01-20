package net.firemuffin303.villagefoe.common.entity.quest;

import net.minecraft.entity.player.PlayerEntity;

public interface QuestContainer {
    void setQuestData(QuestData questData);

    QuestData getQuestData();

    default void prepareOffersFor(PlayerEntity player) {
        this.getQuestData().getQuestList(player);
    }
}
