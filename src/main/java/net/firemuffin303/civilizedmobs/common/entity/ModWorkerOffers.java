package net.firemuffin303.civilizedmobs.common.entity;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Util;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

import java.util.List;
import java.util.Map;

public class ModWorkerOffers {
    public static float tradeMultiplier = 0.05f;
    public static final Map<VillagerProfession,Map<Integer, List<TradeOffer>>> PIGLIN_TRADES = Util.make(Maps.newHashMap(),(villagerProfessionMapHashMap -> {
        villagerProfessionMapHashMap.put(VillagerProfession.ARMORER, Util.make(Maps.newHashMap(), (integerTradeOfferHashMap) -> {
            integerTradeOfferHashMap.put(1, List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT, 5), new ItemStack(Items.ENDER_PEARL), 12, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT, 2), new ItemStack(Items.BLAZE_POWDER), 10, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT, 1), new ItemStack(Items.DIAMOND), 11, 2, tradeMultiplier)
            ));
            integerTradeOfferHashMap.put(2, List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.SPRUCE_SAPLING), 16, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.ACACIA_SAPLING), 16, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.BIRCH_SAPLING), 16, 2, tradeMultiplier)
            ));

            integerTradeOfferHashMap.put(3, List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.BIRCH_BOAT), 16, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.OAK_BOAT), 16, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.ACACIA_BOAT), 16, 2, tradeMultiplier)
            ));

            integerTradeOfferHashMap.put(4, List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.FISHING_ROD), 16, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.DIAMOND_PICKAXE), 16, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.GOLDEN_AXE), 16, 2, tradeMultiplier)
            ));

            integerTradeOfferHashMap.put(5, List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.BLACK_BED), 16, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.LIME_BED), 16, 2, tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_BLOCK), new ItemStack(Items.BEDROCK), 16, 2, tradeMultiplier)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.BUTCHER, Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,2),new ItemStack(Items.PORKCHOP),12,2,tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,3),new ItemStack(Items.LEATHER),13,2,tradeMultiplier)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,2),new ItemStack(Items.BEEF),12,2,tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,3),new ItemStack(Items.SADDLE),13,2,tradeMultiplier)
            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,2),new ItemStack(Items.DIAMOND),12,2,tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,3),new ItemStack(Items.EMERALD),13,2,tradeMultiplier)
            ));


            integerListHashMap.put(4,List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,2),new ItemStack(Items.EGG),12,2,tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,3),new ItemStack(Items.ECHO_SHARD),13,2,tradeMultiplier)
            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,2),new ItemStack(Items.FEATHER),12,2,tradeMultiplier),
                    new TradeOffer(new ItemStack(Items.GOLD_INGOT,3),new ItemStack(Items.GLASS),13,2,tradeMultiplier)
            ));
        }));
        }
    ));
}