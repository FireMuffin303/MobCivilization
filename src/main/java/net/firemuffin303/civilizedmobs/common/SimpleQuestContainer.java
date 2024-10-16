package net.firemuffin303.civilizedmobs.common;

import net.firemuffin303.civilizedmobs.common.entity.quest.QuestContainer;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestData;

public class SimpleQuestContainer implements QuestContainer {
    QuestData questData = new QuestData();
    @Override
    public void setQuestData(QuestData questData) {

    }

    @Override
    public QuestData getQuestData() {
        return this.questData;
    }
}
