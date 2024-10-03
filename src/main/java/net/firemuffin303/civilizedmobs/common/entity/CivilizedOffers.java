package net.firemuffin303.civilizedmobs.common.entity;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Util;
import net.minecraft.village.TradeOffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CivilizedOffers {
    public static final Map<Integer, List<TradeOffer>> PIGLIN_TEST_TRADE = Util.make(Maps.newHashMap(),(integerTradeOfferHashMap -> {
        integerTradeOfferHashMap.put(1,List.of(
                new TradeOffer(new ItemStack(Items.GOLD_INGOT,5),new ItemStack(Items.ENDER_PEARL),12,2,1.0f),
                new TradeOffer(new ItemStack(Items.GOLD_INGOT,2),new ItemStack(Items.BLAZE_POWDER),10,2,1.0f)));
        integerTradeOfferHashMap.put(2,List.of(
                new TradeOffer(new ItemStack(Items.GOLD_BLOCK),new ItemStack(Items.SPRUCE_SAPLING),16,2,1.0f)
        ));
    }));
}
