package net.firemuffin303.civilizedmobs.common.entity.pillager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.firemuffin303.civilizedmobs.registry.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.raid.Raid;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IllagerTradeOffers {
    public static List<Potion> WITCH_POTIONS = List.of(Potions.WATER_BREATHING,Potions.FIRE_RESISTANCE,Potions.HEALING,Potions.SWIFTNESS,Potions.SLOWNESS,Potions.POISON,Potions.WEAKNESS);

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
                            new TradeOffers.BuyForOneEmeraldFactory(Items.FLINT,28,12,1),
                            new TradeOffers.BuyForOneEmeraldFactory(Items.STICK,32,12,1),
                            new TradeOffers.SellPotionHoldingItemFactory(Items.ARROW,5,Items.TIPPED_ARROW,5,2,32,12),
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

    public static final Int2ObjectMap<TradeOffers.Factory[]> WITCH_TRADES = new Int2ObjectArrayMap<>(
            ImmutableMap.of(
                    1, new TradeOffers.Factory[]{
                            new TradeOffers.BuyForOneEmeraldFactory(Items.NETHER_WART,32,12,5),
                            new TradeOffers.BuyForOneEmeraldFactory(Items.GLOWSTONE_DUST,4,12,5),
                            new TradeOffers.BuyForOneEmeraldFactory(Items.RED_MUSHROOM,8,12,5),
                            new TradeOffers.BuyForOneEmeraldFactory(Items.BROWN_MUSHROOM,8,12,5),
                            new TradeOffers.SellItemFactory(Items.LAPIS_LAZULI,2,4,8,1),
                            new TradeOffers.SellItemFactory(Items.SCUTE,8,2,8,1)
                    },
                    2,new TradeOffers.Factory[]{
                            new WitchPotionFactory(10),
                            new WitchPotionFactory(10)
                    }
            )
    );

    public static final Map<VillagerProfession, Map<Integer, List<TradeOffers.Factory>>> WORKER_TRADES = Util.make(Maps.newHashMap(), villagerProfessionMapHashMap -> {
        villagerProfessionMapHashMap.put(VillagerProfession.ARMORER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.COAL,15,16,2),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_LEGGINGS),7,1,12,1,0.2F),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 1, 0.2F),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_HELMET), 5, 1, 12, 1, 0.2F),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_CHESTPLATE), 5, 1, 12, 1, 0.2F)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.IRON_INGOT, 4, 12, 10),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 5, 0.2F),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 5, 0.2F)
            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.LAVA_BUCKET, 1, 12, 20),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.DIAMOND, 1, 12, 20),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 10, 0.2F),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 10, 0.2F),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.SHIELD), 5, 1, 12, 10, 0.2F)
            ));

            integerListHashMap.put(4,List.of(
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_LEGGINGS, 14, 3, 15, 0.2F),
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_BOOTS, 8, 3, 15, 0.2F)
            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_HELMET, 8, 3, 30, 0.2F),
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_CHESTPLATE, 16, 3, 30, 0.2F)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.BUTCHER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.CHICKEN,15,16,2),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.PORKCHOP,7,16,2),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.RABBIT,4,16,2),
                    new TradeOffers.SellItemFactory(Items.RABBIT_STEW,1,1,2)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.COAL, 15, 16, 2),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.COOKED_PORKCHOP), 1, 5, 16, 5),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.COOKED_CHICKEN), 1, 5, 12, 5)
            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.MUTTON, 7, 16, 5),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.BEEF, 7, 16, 5)
            ));

            integerListHashMap.put(4,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.DRIED_KELP_BLOCK,10,12,30)
            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.SWEET_BERRIES,10,12,30)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.CARTOGRAPHER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.PAPER,24,16,2),
                    new TradeOffers.SellItemFactory(Items.MAP,7,1,2)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.GLASS_PANE, 4, 12, 10),
                    new TradeOffers.SellItemFactory(Items.LANTERN, 2, 1, 10),
                    new TradeOffers.SellMapFactory(14,StructureTags.VILLAGE,"filled_map.village", MapIcon.Type.RED_X,12,10)
            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.COMPASS, 1, 12, 20),
                    new TradeOffers.SellMapFactory(24, ModTags.ON_ANCIENT_CITY_EXPLORER_MAP,"filled_map.ancient_city", MapIcon.Type.RED_X,12,10)
            ));

            integerListHashMap.put(4,List.of(
                    new TradeOffers.SellItemFactory(Items.ITEM_FRAME, 14, 3, 15),
                    new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_BANNER, 8, 3, 15),
                    new TradeOffers.SellItemFactory(Items.GRAY_BANNER, 8, 3, 15)
            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffers.SellItemFactory(Items.GLOBE_BANNER_PATTERN, 8, 1, 30)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.LEATHERWORKER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.LEATHER,8,16,2),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.LEATHER_HELMET),19,1,16,2),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.LEATHER_CHESTPLATE),19,1,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.FLINT,28,16,5),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.LEATHER_LEGGINGS),19,1,16,5),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.LEATHER_BOOTS),19,1,16,5)

            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.RABBIT_HIDE,8,16,10),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.LEAD),8,2,16,10)
            ));

            integerListHashMap.put(4,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.SCUTE,9,16,25),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.RABBIT_FOOT,2,16,25)

            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffers.SellItemFactory(new ItemStack(Items.SADDLE),16,1,16,30),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.NAME_TAG),20,1,16,30)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.LIBRARIAN,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.PAPER, 24, 16, 2),
                    new TradeOffers.SellItemFactory(Blocks.BOOKSHELF.asItem(),9,1,12),
                    new ToolEnchantBookFactory(1,Items.IRON_AXE)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.BOOK,4,12,10),
                    new ToolEnchantBookFactory(5,Items.IRON_AXE),
                    new ToolEnchantBookFactory(5,Items.CROSSBOW),
                    new TradeOffers.SellItemFactory(Items.LANTERN, 1, 1, 5)
            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.INK_SAC, 5, 12, 20),
                    new TradeOffers.SellItemFactory(Items.GLASS, 1, 4, 10),
                    new ToolEnchantBookFactory(5,Items.CROSSBOW)
            ));

            integerListHashMap.put(4,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.WRITABLE_BOOK, 2, 12, 30),
                    new TradeOffers.EnchantBookFactory(15),
                    new TradeOffers.SellItemFactory(Items.CLOCK, 5, 1, 15),
                    new TradeOffers.SellItemFactory(Items.COMPASS, 5, 1, 15)
            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffers.SellItemFactory(Items.NAME_TAG, 20, 1, 30)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.WEAPONSMITH,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.COAL,15,16,2),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_AXE),3,1,12,1),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_SWORD), 4, 1, 12, 1)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.IRON_INGOT, 4, 12, 10),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.BELL), 36, 1, 12, 5),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.FLINT, 16, 12, 5),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.LAVA_BUCKET, 1, 12, 5),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.CROSSBOW), 4, 1, 12, 1)
            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffers.SellEnchantedToolFactory(Items.IRON_SWORD, 1, 12, 10),
                    new TradeOffers.SellEnchantedToolFactory(Items.IRON_AXE, 1, 12, 10),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.SHIELD), 5, 1, 12, 10, 0.2F)
            ));

            integerListHashMap.put(4,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.DIAMOND, 1, 12, 20),
                    new TradeOffers.SellEnchantedToolFactory(Items.CROSSBOW, 8, 3, 15, 0.2F)
            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_AXE, 16, 3, 30),
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_SWORD, 16, 3, 30)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.TOOLSMITH,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.COAL,15,16,2),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_PICKAXE),3,1,12,1),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_SHOVEL), 4, 1, 12, 1)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.IRON_INGOT, 4, 12, 10),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_HOE),3,1,12,1),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1)
            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.IRON_INGOT, 4, 12, 10),
                    new TradeOffers.SellItemFactory(new ItemStack(Items.BELL), 36, 1, 12, 5),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.FLINT, 16, 12, 5),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.LAVA_BUCKET, 1, 12, 5),
                    new TradeOffers.SellEnchantedToolFactory(Items.IRON_HOE, 14, 3, 15, 0.2F),
                    new TradeOffers.SellEnchantedToolFactory(Items.IRON_PICKAXE, 14, 3, 15, 0.2F),
                    new TradeOffers.SellEnchantedToolFactory(Items.IRON_SHOVEL, 14, 3, 15, 0.2F)
            ));

            integerListHashMap.put(4,List.of(
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_HOE, 14, 3, 15, 0.2F),
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_SHOVEL, 8, 3, 15, 0.2F)
            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_AXE, 8, 3, 30, 0.2F),
                    new TradeOffers.SellEnchantedToolFactory(Items.DIAMOND_PICKAXE, 16, 3, 30, 0.2F)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.SHEPHERD,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.WHITE_WOOL,18,16,2),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.BROWN_WOOL,18,16,2),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.GRAY_WOOL,18,16,2),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.BLACK_WOOL,18,16,2),
                    new TradeOffers.SellItemFactory(Items.SHEARS,8,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new TradeOffers.BuyForOneEmeraldFactory(Items.RED_WOOL,18,16,10),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.BLUE_WOOL,18,16,10),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.LIGHT_BLUE_WOOL,18,16,10),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.LIGHT_BLUE_DYE,12,15,10),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.BLUE_DYE,12,15,10),
                    new TradeOffers.BuyForOneEmeraldFactory(Items.BLACK_DYE,12,15,10)
            ));

            integerListHashMap.put(3,List.of(
                    new TradeOffers.SellItemFactory(Items.WHITE_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.GRAY_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.BLACK_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.BROWN_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.RED_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.ORANGE_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.YELLOW_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.LIME_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.GREEN_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.CYAN_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.BLUE_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.PURPLE_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.MAGENTA_CARPET, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.PINK_CARPET, 16, 12, 12),

                    new TradeOffers.SellItemFactory(Items.WHITE_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.GRAY_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.BLACK_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.BROWN_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.RED_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.ORANGE_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.YELLOW_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.LIME_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.GREEN_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.CYAN_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.BLUE_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.PURPLE_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.MAGENTA_WOOL, 16, 12, 12),
                    new TradeOffers.SellItemFactory(Items.PINK_WOOL, 16, 12, 12)
            ));

            integerListHashMap.put(4,List.of(
                    new TradeOffers.SellItemFactory(Items.WHITE_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.GRAY_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.BLACK_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.BROWN_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.RED_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.ORANGE_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.YELLOW_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.LIME_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.GREEN_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.CYAN_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.BLUE_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.PURPLE_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.MAGENTA_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.PINK_BANNER, 16, 12, 15),
                    new TradeOffers.SellItemFactory(Items.PAINTING, 2, 16, 15)
            ));

            integerListHashMap.put(5,List.of(
                    new TradeOffers.SellItemFactory(Raid.getOminousBanner(),12,1,16,1)
            ));
        }));
    });

    public static class WitchPotionFactory implements TradeOffers.Factory{
        private final int experience;

        public WitchPotionFactory(int experience){
            this.experience = experience;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            List<Potion> potions = new ArrayList<>(IllagerTradeOffers.WITCH_POTIONS);
            Collections.shuffle(potions);
            Potion potion = potions.get(0);
            return new TradeOffer(new ItemStack(Items.EMERALD,32 ),new ItemStack(Items.NETHER_WART,1),PotionUtil.setPotion(new ItemStack(Items.POTION),potion),12,this.experience,0.2f);
        }
    }

    public static class ToolEnchantBookFactory implements TradeOffers.Factory{
        private final int experience;
        private final ItemStack itemStack;

        public ToolEnchantBookFactory(int experience, Item item){
            this.experience =experience;
            this.itemStack = new ItemStack(item);
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            List<Enchantment> list = Registries.ENCHANTMENT.stream().filter(enchantment ->
                    enchantment.isAvailableForEnchantedBookOffer() && enchantment.isAcceptableItem(this.itemStack) && enchantment != Enchantments.MENDING).toList();
            Enchantment enchantment = list.get(random.nextInt(list.size()));
            int i = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasure()) {
                j *= 2;
            }

            j = Math.min(j,64);
            return new TradeOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F);
        }
    }
}
