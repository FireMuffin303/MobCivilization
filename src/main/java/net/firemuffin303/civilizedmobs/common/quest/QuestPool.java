package net.firemuffin303.civilizedmobs.common.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record QuestPool(QuestList level1,QuestList level2, QuestList level3,QuestList level4, QuestList level5) {

    public static final Codec<QuestPool> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      QuestList.CODEC.fieldOf("level1").forGetter(QuestPool::level1),
      QuestList.CODEC.fieldOf("level2").forGetter(QuestPool::level2),
      QuestList.CODEC.fieldOf("level3").forGetter(QuestPool::level3),
      QuestList.CODEC.fieldOf("level4").forGetter(QuestPool::level4),
      QuestList.CODEC.fieldOf("level5").forGetter(QuestPool::level5)
    ).apply(instance,QuestPool::new));

    public QuestPool(QuestList level1, QuestList level2, QuestList level3, QuestList level4, QuestList level5){
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
        this.level4 = level4;
        this.level5 = level5;
    }

    public QuestList get(int level) {
        switch (level){
            case 0 -> {
                return level1;
            }
            case 1 ->{
                return level2;
            }
            case 2 -> {
                return this.level3();
            }
            case 3 -> {
                return this.level4();
            }
            case 4 -> {
                return this.level5();
            }
        }
        throw new RuntimeException("Level out of Bound");
    }
}
