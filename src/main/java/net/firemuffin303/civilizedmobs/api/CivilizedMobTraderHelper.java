package net.firemuffin303.civilizedmobs.api;

import net.firemuffin303.civilizedmobs.common.entity.piglin.PiglinTradeOffers;
import net.firemuffin303.civilizedmobs.common.entity.pillager.IllagerTradeOffers;
import net.firemuffin303.civilizedmobs.common.entity.witherSkelton.WitherSkeletonTradeOffers;
import net.minecraft.potion.Potion;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CivilizedMobTraderHelper {

    public static void addPiglinWorkerTrade(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factory){
        addWorkerTrade(PiglinTradeOffers.PIGLIN_TRADES,profession,level,factory);
    }

    public static void addPiglinQuestTrade(int level, Consumer<List<TradeOffers.Factory>> factory){
        addQuestTrade(PiglinTradeOffers.PIGLIN_QUEST_OFFER,level,factory);
    }

    //---------Pillager------------
    public static void addPillagerWorkerTrade(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factory){
        addWorkerTrade(IllagerTradeOffers.WORKER_TRADES,profession,level,factory);
    }

    public static void addPillagerQuestTrade(int level, Consumer<List<TradeOffers.Factory>> factory){
        addQuestTrade(IllagerTradeOffers.PILLAGER_QUSET_OFFER,level,factory);
    }

    public static void addEvokerTrade(int level, Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> list = new ArrayList<>();
        TradeOffers.Factory[] original = IllagerTradeOffers.EVOKER_TRADES.computeIfAbsent(level,(key) -> new TradeOffers.Factory[0]);
        factory.accept(list);
        TradeOffers.Factory[] allEntries = ArrayUtils.addAll(original,list.toArray(new TradeOffers.Factory[0]));
        IllagerTradeOffers.EVOKER_TRADES.put(level,allEntries);
    }

    public static void addPillagerTrader(int level,Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> list = new ArrayList<>();
        TradeOffers.Factory[] original = IllagerTradeOffers.PILLAGER_TRADES.computeIfAbsent(level,(key) -> new TradeOffers.Factory[0]);
        factory.accept(list);
        TradeOffers.Factory[] allEntries = ArrayUtils.addAll(original,list.toArray(new TradeOffers.Factory[0]));
        IllagerTradeOffers.PILLAGER_TRADES.put(level,allEntries);
    }

    public static void addVindicatorTrader(int level,Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> list = new ArrayList<>();
        TradeOffers.Factory[] original = IllagerTradeOffers.VINDICATOR_TRADES.computeIfAbsent(level,(key) -> new TradeOffers.Factory[0]);
        factory.accept(list);
        TradeOffers.Factory[] allEntries = ArrayUtils.addAll(original,list.toArray(new TradeOffers.Factory[0]));
        IllagerTradeOffers.VINDICATOR_TRADES.put(level,allEntries);
    }

    public static void addIllusionerTrader(int level,Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> list = new ArrayList<>();
        TradeOffers.Factory[] original = IllagerTradeOffers.ILLUSIONER_TRADES.computeIfAbsent(level,(key) -> new TradeOffers.Factory[0]);
        factory.accept(list);
        TradeOffers.Factory[] allEntries = ArrayUtils.addAll(original,list.toArray(new TradeOffers.Factory[0]));
        IllagerTradeOffers.ILLUSIONER_TRADES.put(level,allEntries);
    }

    public static void addWitchTrader(int level,Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> list = new ArrayList<>();
        TradeOffers.Factory[] original = IllagerTradeOffers.WITCH_TRADES.computeIfAbsent(level,(key) -> new TradeOffers.Factory[0]);
        factory.accept(list);
        TradeOffers.Factory[] allEntries = ArrayUtils.addAll(original,list.toArray(new TradeOffers.Factory[0]));
        IllagerTradeOffers.WITCH_TRADES.put(level,allEntries);
    }

    public static void addWitchSellPotion(Potion... potion){
        List<Potion> potions = new ArrayList<>(IllagerTradeOffers.WITCH_POTIONS);
        potions.addAll(List.of(potion));
        IllagerTradeOffers.WITCH_POTIONS = potions;
    }


    //--------Wither---------------
    public static void addWitherWorkerTrade(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factory){
        addWorkerTrade(WitherSkeletonTradeOffers.WITHER_TRADES,profession,level,factory);
    }

    public static void addWitherQuestTrade(int level, Consumer<List<TradeOffers.Factory>> factory){
        addQuestTrade(WitherSkeletonTradeOffers.WITHER_QUSET_OFFER,level,factory);
    }

    //--------------------
    private static void addWorkerTrade(Map<VillagerProfession,Map<Integer, List<TradeOffers.Factory>>> villagerProfessionMapMap ,VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> newFactory = new ArrayList<>();
        factory.accept(newFactory);
        Map<Integer,List<TradeOffers.Factory>> fa = villagerProfessionMapMap.computeIfAbsent(profession,(key) -> new HashMap<>());
        List<TradeOffers.Factory> factories = new ArrayList<>(fa.computeIfAbsent(level, key -> new ArrayList<>()));
        factories.addAll(newFactory);
        villagerProfessionMapMap.computeIfAbsent(profession,(key) -> new HashMap<>()).put(level,factories);
    }

    private static void addQuestTrade(Map<Integer,List<TradeOffers.Factory>> trade,int level, Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> newFactory = new ArrayList<>();
        factory.accept(newFactory);
        List<TradeOffers.Factory> factories = trade.computeIfAbsent(level,(key) -> new ArrayList<>());
        factories.addAll(newFactory);
        trade.put(level,factories);
    }
}
