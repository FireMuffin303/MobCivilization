package net.firemuffin303.civilizedmobs.common.entity.quest;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.quest.QuestList;
import net.firemuffin303.civilizedmobs.common.quest.QuestPool;
import net.firemuffin303.civilizedmobs.common.quest.QuestPoolTypes;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

import static net.firemuffin303.civilizedmobs.CivilizedMobs.LOGGER;

public class QuestData {
    Map<UUID,Trustful> entityTrust = Maps.newHashMap();


    public QuestData(){}

    //--- Quest List ---
    public void setQuestList(UUID uuid, QuestList quests){
        Trustful trustful = this.getTrust(uuid);
        trustful.questList = quests;
    }

    public QuestList getQuestList(Entity entity){
        Trustful trustful = this.getTrust(entity.getUuid());
        if(trustful.questList == null){
            trustful.questList = new QuestList();
            this.fillQuest(trustful.questList,entity.getUuid(),entity.getWorld().getRandom(),entity.getWorld().getRegistryManager(),2);

        }
        return trustful.questList;
    }

    private void fillQuest(QuestList questList, UUID uuid, Random random, DynamicRegistryManager dynamicRegistryManager, int amount){

        Registry<QuestPool> questPools = dynamicRegistryManager.getOptional(CivilizedMobs.QUEST_POOL).orElseThrow();
        QuestPool questPool = questPools.getOrThrow(QuestPoolTypes.PIGLIN_QUEST_KEY);
        QuestList quests = questPool.get(this.entityTrust.get(uuid).level);

        for(int i = 0;i < amount; ++i){
            questList.add(quests.get(random.nextInt(quests.size())));
        }
    }

    //--- Player Reputation ---
    public void increaseXp(UUID target, int value){
        Trustful trustful = this.getTrust(target);
        trustful.setXp(trustful.getXp() + value);
    }

    public Map<UUID, Trustful> getEntityTrust() {
        return entityTrust;
    }

    public Trustful getTrust(UUID target) {
        return entityTrust.computeIfAbsent(target,(uuid) -> new Trustful());
    }

    private Stream<QuestEntry> entries() {
        return this.entityTrust.entrySet().stream().map(uuidTrustfulEntry -> {
            return new QuestEntry(uuidTrustfulEntry.getKey(),uuidTrustfulEntry.getValue().xp,uuidTrustfulEntry.getValue().level,uuidTrustfulEntry.getValue().questList);
        });
    }

    public void writeData(NbtCompound nbtCompound){
        nbtCompound.put("QuestData",this.serialize(NbtOps.INSTANCE));

    }

    public void readData(NbtCompound nbtCompound){
        NbtList nbtList = nbtCompound.getList("QuestData",10);
        this.deserialize(new Dynamic<>(NbtOps.INSTANCE,nbtList));
    }

    public <T> T serialize(DynamicOps<T> ops) {
        Optional<T> var10000 = QuestData.QuestEntry.LIST_CODEC.encodeStart(ops, this.entries().toList()).resultOrPartial((error) -> {
            LOGGER.warn("Failed to serialize gossips: {}", error);
        });
        Objects.requireNonNull(ops);
        return var10000.orElseGet(ops::emptyList);
    }

    public void deserialize(Dynamic<?> dynamic) {
        QuestEntry.LIST_CODEC.decode(dynamic).resultOrPartial((error) -> LOGGER.warn("Failed to deserialize gossips: {}", error))
                .stream().flatMap(pair -> pair.getFirst().stream()).forEach(entityTrust -> {
            Trustful trustful = this.getTrust(entityTrust.target);
            trustful.setXp(entityTrust.xp);
            trustful.setLevel(entityTrust.level);
            trustful.setQuestList(entityTrust.questList);
        });
    }

    private record QuestEntry(UUID target,int xp,int level,QuestList questList){
        public static final Codec<QuestEntry> CODEC = RecordCodecBuilder.create(questEntryInstance -> {
            return questEntryInstance.group(
                    Uuids.INT_STREAM_CODEC.fieldOf("target").forGetter(QuestEntry::target),
                    Codec.INT.fieldOf("xp").forGetter(QuestEntry::xp),
                    Codec.INT.fieldOf("level").forGetter(QuestEntry::level),
                    QuestList.CODEC.fieldOf("questList").forGetter(QuestEntry::questList)
            ).apply(questEntryInstance,QuestEntry::new);
        });

        public static final Codec<List<QuestEntry>> LIST_CODEC;

        QuestEntry(UUID target,int xp, int level,QuestList questList){
            this.target = target;
            this.xp = xp;
            this.level = level;
            this.questList = questList;
        }

        public int getXp() {
            return xp;
        }

        public int getLevel() {
            return level;
        }

        public UUID getTarget() {
            return target;
        }

        public QuestList getQuestList(){return  questList;}

        static {
            LIST_CODEC = CODEC.listOf();
        }
    }

    public static class Trustful{
        private int level;
        private int xp;
        @Nullable
        QuestList questList;

        public final int MAX_LEVEL = 5;
        private final int[] MAX_XP_PER_LEVEL = {30,50,60,70,80};

        public Trustful(){
            this.level = 0;
            this.xp = 0;
        }

        public boolean canLevelUp(){
            if (this.level >= 5 ) { return false;}
            return this.xp > MAX_XP_PER_LEVEL[this.level];
        }

        public void setLevel(int value){
            this.level = value;
        }

        public void setXp(int xp) {
            this.xp = xp;
        }

        public void setQuestList(@Nullable QuestList questList) {
            this.questList = questList;
        }

        public @Nullable QuestList getQuestList() {
            return questList;
        }

        public int getLevel() {
            return level;
        }

        public int getXp() {
            return xp;
        }


    }
}
