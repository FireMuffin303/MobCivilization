package net.firemuffin303.civilizedmobs.api;

import net.firemuffin303.civilizedmobs.common.entity.ModWorkerOffers;
import net.firemuffin303.civilizedmobs.common.entity.pillager.IllagerTradeOffers;
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
        List<TradeOffers.Factory> newFactory = new ArrayList<>();
        factory.accept(newFactory);
        Map<Integer,List<TradeOffers.Factory>> fa = ModWorkerOffers.PIGLIN_TRADES.computeIfAbsent(profession,(key) -> new HashMap<>());
        List<TradeOffers.Factory> factories = new ArrayList<>(fa.computeIfAbsent(level, key -> new ArrayList<>()));
        factories.addAll(newFactory);
        ModWorkerOffers.PIGLIN_TRADES.computeIfAbsent(profession,(key) -> new HashMap<>()).put(level,newFactory);
    }

    public static void addPiglinQuestTrade(int level, Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> newFactory = new ArrayList<>();
        factory.accept(newFactory);
        List<TradeOffers.Factory> factories = ModWorkerOffers.PIGLIN_QUEST_OFFER.computeIfAbsent(level,(key) -> new ArrayList<>());
        factories.addAll(newFactory);
        ModWorkerOffers.PIGLIN_QUEST_OFFER.put(level,factories);
    }

    //Pillager
    public static void addPillagerWorkerTrade(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> newFactory = new ArrayList<>();
        factory.accept(newFactory);
        Map<Integer,List<TradeOffers.Factory>> fa = ModWorkerOffers.PILLAGER_TRADES.computeIfAbsent(profession,(key) -> new HashMap<>());
        List<TradeOffers.Factory> factories = new ArrayList<>(fa.computeIfAbsent(level, key -> new ArrayList<>()));
        factories.addAll(newFactory);
        ModWorkerOffers.PILLAGER_TRADES.computeIfAbsent(profession,(key) -> new HashMap<>()).put(level,newFactory);
    }

    public static void addPillagerQuestTrade(int level, Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> newFactory = new ArrayList<>();
        factory.accept(newFactory);
        List<TradeOffers.Factory> factories = ModWorkerOffers.PILLAGER_QUSET_OFFER.computeIfAbsent(level,(key) -> new ArrayList<>());
        factories.addAll(newFactory);
        ModWorkerOffers.PILLAGER_QUSET_OFFER.put(level,factories);
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


    //Wither
    public static void addWitherWorkerTrade(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> newFactory = new ArrayList<>();
        factory.accept(newFactory);
        Map<Integer,List<TradeOffers.Factory>> fa = ModWorkerOffers.WITHER_TRADES.computeIfAbsent(profession,(key) -> new HashMap<>());
        List<TradeOffers.Factory> factories = new ArrayList<>(fa.computeIfAbsent(level, key -> new ArrayList<>()));
        factories.addAll(newFactory);
        ModWorkerOffers.WITHER_TRADES.computeIfAbsent(profession,(key) -> new HashMap<>()).put(level,newFactory);
    }

    public static void addWitherQuestTrade(int level, Consumer<List<TradeOffers.Factory>> factory){
        List<TradeOffers.Factory> newFactory = new ArrayList<>();
        factory.accept(newFactory);
        List<TradeOffers.Factory> factories = ModWorkerOffers.WITHER_QUSET_OFFER.computeIfAbsent(level,(key) -> new ArrayList<>());
        factories.addAll(newFactory);
        ModWorkerOffers.WITHER_QUSET_OFFER.put(level,factories);
    }
}
