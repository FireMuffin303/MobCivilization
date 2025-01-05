package net.firemuffin303.civilizedmobs.common.entity.pillager;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;

public class IllagerTradeOffers {
    public static final Int2ObjectMap<TradeOffers.Factory[]> EVOKER_TRADES = new Int2ObjectArrayMap<>(
            ImmutableMap.of(
                    1, new TradeOffers.Factory[]{
                            new TradeOffers.SellItemFactory(Items.BLUE_WOOL,5,16,5,1),
                            new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_WOOL,5,16,5,1),
                            new TradeOffers.SellItemFactory(Items.DARK_OAK_SAPLING,2,4,5,1),
                            new TradeOffers.SellItemFactory(Items.IRON_INGOT,8,8,5,1),
                            new TradeOffers.SellItemFactory(Items.GLOWSTONE_DUST,2,8,10,1),
                            new TradeOffers.SellItemFactory(Items.NAUTILUS_SHELL,2,1,5,1),
                            new TradeOffers.SellItemFactory(Items.SCUTE,2,1,5,1),
                            new TradeOffers.SellItemFactory(Items.CARVED_PUMPKIN,2,1,5,1),
                            new TradeOffers.SellItemFactory(Items.REDSTONE,1,8,5,1),
                            new TradeOffers.SellItemFactory(Items.LAPIS_LAZULI,2,4,8,1),
                            new TradeOffers.SellItemFactory(Items.BLAZE_POWDER,8,2,3,1),
                            new TradeOffers.SellItemFactory(Items.EXPERIENCE_BOTTLE,3,2,16,3)
                    },
                    2,new TradeOffers.Factory[]{
                            new TradeOffers.SellItemFactory(Items.TOTEM_OF_UNDYING,64,1,1,10)
            }
            )
    );

    public static final Int2ObjectMap<TradeOffers.Factory[]> PILLAGER_TRADES = new Int2ObjectArrayMap<>(
            ImmutableMap.of(
                    1, new TradeOffers.Factory[]{
                            new TradeOffers.SellItemFactory(Items.ARROW,4,10,5,1),
                            new TradeOffers.SellItemFactory(Items.CROSSBOW,10,1,5,1),
                            new TradeOffers.ProcessItemFactory(Items.GRAVEL,1,Items.FLINT,1,16,1),
                            new TradeOffers.SellItemFactory(Items.IRON_INGOT,8,8,5,1),
                            new TradeOffers.SellItemFactory(Items.GLOWSTONE_DUST,2,8,10,1),
                            new TradeOffers.SellItemFactory(Items.NAUTILUS_SHELL,2,1,5,1),
                            new TradeOffers.SellItemFactory(Items.SCUTE,2,1,5,1),
                            new TradeOffers.SellItemFactory(Items.CARVED_PUMPKIN,2,1,5,1),
                            new TradeOffers.SellItemFactory(Items.REDSTONE,1,8,5,1),
                            new TradeOffers.SellItemFactory(Items.LAPIS_LAZULI,2,4,8,1),
                            new TradeOffers.SellItemFactory(Items.BLAZE_POWDER,8,2,3,1),
                    },
                    2,new TradeOffers.Factory[]{
                            new TradeOffers.SellEnchantedToolFactory(Items.CROSSBOW,16,1,10)
                    }
            )
    );

    public static final Int2ObjectMap<TradeOffers.Factory[]> VINDICATOR_TRADES = new Int2ObjectArrayMap<>(
            ImmutableMap.of(
                    1, new TradeOffers.Factory[]{
                            new TradeOffers.SellItemFactory(Items.OAK_LOG,4,16,16,1),
                            new TradeOffers.SellItemFactory(Items.BIRCH_LOG,4,16,16,1),
                            new TradeOffers.SellItemFactory(Items.DARK_OAK_LOG,4,16,16,1),
                            new TradeOffers.SellItemFactory(Items.OAK_SAPLING,2,2,16,1),
                            new TradeOffers.SellItemFactory(Items.BIRCH_SAPLING,2,2,16,1),
                            new TradeOffers.SellItemFactory(Items.DARK_OAK_SAPLING,2,4,16,1),
                            new TradeOffers.SellItemFactory(Items.IRON_AXE,5,1,5,1),
                            new TradeOffers.SellItemFactory(Items.IRON_SWORD,5,1,5,1),
                            new TradeOffers.SellItemFactory(Items.CARVED_PUMPKIN,2,1,5,1),
                    },
                    2,new TradeOffers.Factory[]{
                            new TradeOffers.SellEnchantedToolFactory(Items.IRON_AXE,16,1,10)
                    }
            )
    );

    public static final Int2ObjectMap<TradeOffers.Factory[]> ILLUSIONER_TRADES = new Int2ObjectArrayMap<>(
            ImmutableMap.of(
                    1, new TradeOffers.Factory[]{
                            new TradeOffers.SellItemFactory(Items.ARROW,4,10,5,1),
                            new TradeOffers.SellItemFactory(Items.BOW,10,1,5,1),
                            new TradeOffers.SellItemFactory(Items.STRING,8,8,5,1),
                            new TradeOffers.SellItemFactory(Items.CARVED_PUMPKIN,2,1,5,1),
                            new TradeOffers.SellItemFactory(Items.REDSTONE,1,8,5,1),
                            new TradeOffers.SellItemFactory(Items.LAPIS_LAZULI,2,4,8,1),
                            new TradeOffers.SellItemFactory(Items.BLAZE_POWDER,8,2,3,1),
                    },
                    2,new TradeOffers.Factory[]{
                            new TradeOffers.SellEnchantedToolFactory(Items.BOW,16,1,10)
                    }
            )
    );


}
