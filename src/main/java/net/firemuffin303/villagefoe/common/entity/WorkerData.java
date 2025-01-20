package net.firemuffin303.villagefoe.common.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.village.VillagerProfession;

public class WorkerData {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 5;
    private static final int[] LEVEL_BASE_EXPERIENCE = new int[]{0, 10, 70, 150, 250};
    public static final Codec<WorkerData> CODEC = RecordCodecBuilder.create(instance ->{
       return instance.group(Registries.VILLAGER_PROFESSION.getCodec().fieldOf("worker_profession").orElseGet(() -> VillagerProfession.NONE)
                               .forGetter(data -> data.profession),
               Codec.INT.fieldOf("level").orElse(1).forGetter(data -> data.level))
               .apply(instance, WorkerData::new);
    });

    private final VillagerProfession profession;
    private final int level;

    public WorkerData(VillagerProfession profession, int level){
        this.profession = profession;
        this.level = Math.max(1,level);
    }

    public WorkerData withProfession(VillagerProfession civilziedProfession){
        return new WorkerData(civilziedProfession,this.level);
    }

    public WorkerData withLevel(int level){
        return new WorkerData(this.profession,level);
    }

    public static int getUpperLevelExperience(int level) {
        return canLevelUp(level) ? LEVEL_BASE_EXPERIENCE[level] : 0;
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    public int getLevel() {
        return level;
    }

    public static boolean canLevelUp(int level) {
        return level >= MIN_LEVEL && level < MAX_LEVEL;
    }
}
