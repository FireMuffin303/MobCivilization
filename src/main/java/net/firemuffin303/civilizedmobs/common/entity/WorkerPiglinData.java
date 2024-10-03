package net.firemuffin303.civilizedmobs.common.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.registry.Registries;
import net.minecraft.village.VillagerProfession;

public class WorkerPiglinData {
    public static final Codec<WorkerPiglinData> CODEC = RecordCodecBuilder.create(instance ->{
       return instance.group(Registries.VILLAGER_PROFESSION.getCodec().fieldOf("civil_profession").orElseGet(() -> VillagerProfession.NONE)
                               .forGetter(data -> data.profession),
               Codec.INT.fieldOf("level").orElse(1).forGetter(data -> data.level))
               .apply(instance, WorkerPiglinData::new);
    });

    private final VillagerProfession profession;
    private final int level;

    public WorkerPiglinData(VillagerProfession profession, int level){
        this.profession = profession;
        this.level = level;
    }

    public WorkerPiglinData withProfession(VillagerProfession civilziedProfession){
        return new WorkerPiglinData(civilziedProfession,this.level);
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    public int getLevel() {
        return level;
    }
}
