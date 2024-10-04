package net.firemuffin303.civilizedmobs.common.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.registry.Registries;
import net.minecraft.village.VillagerProfession;

public class WorkerData {
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
        this.level = level;
    }

    public WorkerData withProfession(VillagerProfession civilziedProfession){
        return new WorkerData(civilziedProfession,this.level);
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    public int getLevel() {
        return level;
    }
}
