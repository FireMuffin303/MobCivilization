package net.firemuffin303.civilizedmobs.common.entity.quest;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface QuestContainer {
    void setQuestData(QuestData questData);

    QuestData getQuestData();

    default void prepareOffersFor(PlayerEntity player) {
        this.getQuestData().getQuestList(player);
    }
}
