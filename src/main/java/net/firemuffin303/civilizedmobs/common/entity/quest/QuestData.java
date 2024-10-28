package net.firemuffin303.civilizedmobs.common.entity.quest;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.ModWorkerOffers;
import net.firemuffin303.civilizedmobs.common.quest.QuestList;
import net.firemuffin303.civilizedmobs.common.quest.QuestPool;
import net.firemuffin303.civilizedmobs.common.quest.QuestPoolTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registry;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static net.firemuffin303.civilizedmobs.CivilizedMobs.LOGGER;

public class QuestData {
    protected LivingEntity livingEntity;
    Map<UUID,Trustful> entityTrust = Maps.newHashMap();


    public QuestData(LivingEntity livingEntity){
        this.livingEntity = livingEntity;
    }

    //--- Quest List ---
    public void setQuestList(UUID uuid, TradeOfferList quests){
        Trustful trustful = this.getTrust(uuid);
        trustful.tradeOffers = quests;
    }

    public TradeOfferList getQuestList(Entity entity){
        Trustful trustful = this.getTrust(entity.getUuid());
        if(trustful.tradeOffers == null){
            trustful.tradeOffers = new TradeOfferList();
            this.fillTrade(trustful.tradeOffers,entity,2);
        }
        return trustful.tradeOffers;
    }



    public void fillTrade(TradeOfferList tradeOffers, Entity player, int amount){
        World world = player.getWorld();
        int level = getTrust(player.getUuid()).level;
        List<TradeOffers.Factory> tradeOfferList = ModWorkerOffers.PIGLIN_QUEST_OFFER.get(level);


        for(int i = 0;i < amount; ++i){
            TradeOffer quest = tradeOfferList.get(MathHelper.nextInt(this.livingEntity.getRandom(),0,tradeOfferList.size()-1)).create(this.livingEntity,this.livingEntity.getRandom());
            tradeOffers.add(quest);
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


    public void writeData(NbtCompound nbtCompound){
        this.toNBT(nbtCompound);

    }

    public void readData(NbtCompound nbtCompound){
        NbtList nbtList = nbtCompound.getList("QuestData",10);
        for(int i = 0;i < nbtList.size();i++){
            NbtCompound questEntry = nbtList.getCompound(i);
            UUID uuid = questEntry.getUuid("target");
            int xp = questEntry.getInt("xp");
            int level = questEntry.getInt("level");
            TradeOfferList tradeOfferList = new TradeOfferList(questEntry.getCompound("tradeOfferList"));

            Trustful trustful = new Trustful();
            trustful.setXp(xp);
            trustful.setLevel(level);
            trustful.setTradeList(tradeOfferList);

            this.entityTrust.put(uuid,trustful);
        }
    }


    public void toNBT(NbtCompound nbtCompound){
        NbtCompound questEntryNBT = new NbtCompound();
        NbtList questEntryList = new NbtList();
        AtomicInteger i = new AtomicInteger(0);
        this.entityTrust.forEach((uuid, trustful) -> {
            questEntryNBT.putUuid("target",uuid);
            questEntryNBT.putInt("xp",trustful.getXp());
            questEntryNBT.putInt("level",trustful.getLevel());
            questEntryNBT.put("tradeOfferList",trustful.tradeOffers.toNbt());
            questEntryList.add(i.get(),questEntryNBT);
            i.getAndIncrement();
        });

        nbtCompound.put("QuestData",questEntryList);

    }

    public static class Trustful{
        private int level;
        private int xp;
        TradeOfferList tradeOffers;

        public final int MAX_LEVEL = 5;
        private static final int[] MAX_XP_PER_LEVEL = {0,10,70,150,250};

        public Trustful(){
            this.level = 1;
            this.xp = 0;
        }

        public boolean canLevelUp(){
            if (this.level >= 5 ) { return false;}
            return this.xp > MAX_XP_PER_LEVEL[this.level];
        }

        public void setLevel(int value){
            this.level = Math.max(1,value);
        }

        public void setXp(int xp) {
            this.xp = xp;
        }

        public static int getUpperLevelExperience(int level) {
            return canLevelUp(level) ?  MAX_XP_PER_LEVEL[level] : 0;
        }

        public static int getLowerLevelExperience(int level) {
            return canLevelUp(level-1) ?  MAX_XP_PER_LEVEL[level-1] : 0;
        }

        public static boolean canLevelUp(int level) {
            return level >= 0 && level < 5;
        }

        public void setTradeList(TradeOfferList tradeOffers) {
            this.tradeOffers = tradeOffers;
        }

        public TradeOfferList getTradeOffers() {
            return tradeOffers;
        }

        public int getLevel() {
            return level;
        }

        public int getXp() {
            return xp;
        }


    }
}
