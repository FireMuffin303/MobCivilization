package net.firemuffin303.villagefoe.common.entity.piglin;

import com.google.common.collect.Maps;
import net.firemuffin303.villagefoe.common.integration.muffinsQuest.ModQuests;
import net.firemuffin303.villagefoe.registry.ModTags;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.map.MapIcon;
import net.minecraft.registry.Registries;
import net.minecraft.util.Util;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.firemuffin303.villagefoe.api.VillageFoeOffersFactories.*;

import java.util.*;

public class PiglinTradeOffers {
    public static final Map<VillagerProfession,Map<Integer, List<TradeOffers.Factory>>> PIGLIN_TRADES = Util.make(Maps.newHashMap(),(villagerProfessionMapHashMap -> {
        //Status Done
        villagerProfessionMapHashMap.put(VillagerProfession.ARMORER, Util.make(Maps.newHashMap(), (integerTradeOfferHashMap) -> {
            integerTradeOfferHashMap.put(1, List.of(
                    new GoldForItemFactory(new ItemStack(Items.EMERALD) ,4,12,2),
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_HELMET),6,12,2),
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_CHESTPLATE),9,12,2)

            ));
            integerTradeOfferHashMap.put(2, List.of(
                    new GoldForItemFactory(new ItemStack(Items.BELL),30,16,5),
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_LEGGINGS),8,12,5),
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_BOOTS),5,12,5)
            ));

            integerTradeOfferHashMap.put(3, List.of(
                    new GoldForItemFactory(new ItemStack(Items.SHIELD),15,16,10),
                    new ItemForGold(Items.LAVA_BUCKET,1,16,10),
                    new GoldForItemFactory(new ItemStack(Items.IRON_INGOT),8,16,20),
                    new ItemForGold(Items.IRON_BARS,16,16,20)
            ));

            integerTradeOfferHashMap.put(4, List.of(
                    new GoldForEnchantedTool(Items.GOLDEN_HELMET,18,16,20),
                    new GoldForEnchantedTool(Items.GOLDEN_CHESTPLATE,18,16,20),
                    new GoldForEnchantedTool(Items.GOLDEN_LEGGINGS,18,16,20),
                    new GoldForEnchantedTool(Items.GOLDEN_BOOTS,18,16,20)
            ));

            integerTradeOfferHashMap.put(5, List.of(
                    new PiglinSelectedEnchantedBook(20,Registries.ENCHANTMENT.stream().filter(enchantment ->
                            (
                                    enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_HELMET)) ||
                                    enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_CHESTPLATE)) ||
                                    enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_LEGGINGS)) ||
                                    enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_BOOTS))
                            ) && !enchantment.isTreasure()).toList())
            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.BUTCHER, Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new ItemForGold(Items.PORKCHOP,14,16,2),
                    new ItemForGold(Items.LEATHER,14,16,2),
                    new PiglinSuspiciousStewFactory(StatusEffects.BLINDNESS,200,4,5),
                    new PiglinSuspiciousStewFactory(StatusEffects.NAUSEA,200,4,5),
                    new GoldForItemFactory(new ItemStack(Items.MUSHROOM_STEW),2,14,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new ItemForGold(Items.COAL,15,2,16,5),
                    new GoldForItemFactory(new ItemStack(Items.COOKED_PORKCHOP,5),3,13,5)
            ));

            integerListHashMap.put(3,List.of(
                    new ItemForGold(Items.BEEF,32,16,10),
                    new ItemForGold(Items.MUTTON,32,16,10),
                    new ItemForGold(Items.CHICKEN,32,16,10)
            ));


            integerListHashMap.put(4,List.of(
                    new ItemForGold(Items.NETHER_WART_BLOCK,48,16,15),
                    new ItemForGold(Items.WARPED_WART_BLOCK,48,16,15),
                    new ItemForGold(Items.CRIMSON_FUNGUS,32,16,15),
                    new ItemForGold(Items.WARPED_FUNGUS,32,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new GoldForItemFactory(new ItemStack(Items.SHROOMLIGHT),8,16,20),
                    new GoldForItemFactory(new ItemStack(Items.COOKED_BEEF),8,13,20),
                    new GoldForItemFactory(new ItemStack(Items.COOKED_PORKCHOP),8,13,20),
                    new GoldForItemFactory(new ItemStack(Items.COOKED_CHICKEN),8,13,20)
            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.CLERIC,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new ItemForGold(Items.ROTTEN_FLESH,32,16,2),
                    new GoldForItemFactory(new ItemStack(Items.REDSTONE,2),1,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.LAPIS_LAZULI),1,12,5),
                    new GoldForItemFactory(new ItemStack(Items.GLOWSTONE),2,12,5)
            ));

            integerListHashMap.put(3,List.of(
                    new ItemForGold(Items.RABBIT_FOOT,1,12,10),
                    new ItemForGold(Items.BLAZE_POWDER,3,12,10)
            ));

            integerListHashMap.put(4,List.of(
                    new ItemForGold(Items.GLASS_BOTTLE,9,16,25),
                    new ItemForGold(Items.SCUTE,9,3,16,25),
                    new GoldForItemFactory(new ItemStack(Items.ENDER_PEARL),12,16,25)
            ));


            integerListHashMap.put(5,List.of(
                    new ItemForGold(Items.NETHER_WART,12,16,30),
                    new GoldForItemFactory(new ItemStack(Items.EXPERIENCE_BOTTLE),12,16,30)
            ));

        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.CARTOGRAPHER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new ItemForGold(Items.PAPER,24,16,2),
                    new GoldForItemFactory(new ItemStack(Items.MAP),1,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.GLASS_PANE),1,16,2),
                    new ItemForGold(Items.INK_SAC,24,16,2),
                    new ItemForGold(Items.FEATHER,16,16,2)
            ));

            integerListHashMap.put(3,List.of(
                    new PiglinSellMapFactory(16, ModTags.ON_NETHER_FORTRESS_EXPLORER_MAP,"filled_map.fortress", MapIcon.Type.RED_X,16,15),
                    new ItemForGold(Items.COMPASS,1,16,2)
            ));

            integerListHashMap.put(4,List.of(
                    new PiglinSellMapFactory(18, ModTags.ON_BASTION_EXPLORER_MAP,"filled_map.bastion", MapIcon.Type.RED_X,16,20),
                    new GoldAndItemForItemFactory(new ItemStack(Items.GLOW_INK_SAC,8),1,Items.INK_SAC,8,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new GoldForItemFactory(new ItemStack(Items.GLOW_ITEM_FRAME),7,16,2),
                    new GoldForItemFactory(new ItemStack(Items.PIGLIN_BANNER_PATTERN),9,16,2)
            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.LEATHERWORKER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new ItemForGold(Items.LEATHER,8,16,2),
                    new GoldForItemFactory(new ItemStack(Items.LEATHER_HELMET),19,16,2),
                    new GoldForItemFactory(new ItemStack(Items.LEATHER_CHESTPLATE),19,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new ItemForGold(Items.FLINT,28,16,5),
                    new GoldForItemFactory(new ItemStack(Items.LEATHER_LEGGINGS),19,16,5),
                    new GoldForItemFactory(new ItemStack(Items.LEATHER_BOOTS),19,16,5)

            ));

            integerListHashMap.put(3,List.of(
                    new ItemForGold(Items.RABBIT_HIDE,8,16,10),
                    new GoldForItemFactory(new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK),8,16,10)
            ));

            integerListHashMap.put(4,List.of(
                    new ItemForGold(Items.SCUTE,9,3,16,25),
                    new ItemForGold(Items.RABBIT_FOOT,2,4,16,25)

            ));

            integerListHashMap.put(5,List.of(
                    new GoldForItemFactory(new ItemStack(Items.SADDLE),16,16,30),
                    new GoldForItemFactory(new ItemStack(Items.NAME_TAG),20,16,30)
            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.LIBRARIAN,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new ItemForGold(Items.PAPER,24,16,2),
                    new ItemForGold(Items.BOOK,4,16,2),
                    new GoldForItemFactory(new ItemStack(Items.BOOKSHELF) ,4,16,2),
                    new PiglinEnchantedBookFactory(15)
            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.LANTERN,2),1,16,2),
                    new ItemForGold(Items.INK_SAC,12,16,2),
                    new PiglinEnchantedBookFactory(20)
            ));

            integerListHashMap.put(3,List.of(
                    new GoldForItemFactory(new ItemStack(Items.GLASS,4),1,16,2),
                    new GoldForItemFactory(new ItemStack(Items.CLOCK,1),8,16,2)

                    ));

            integerListHashMap.put(4,List.of(
                    new GoldForItemFactory(new ItemStack(Items.COMPASS),6,16,2),
                    new GoldForItemFactory(new ItemStack(Items.WRITABLE_BOOK),6,16,2)

            ));

            integerListHashMap.put(5,List.of(
                    new PiglinSoulSpeedFactory(20),
                    new GoldForItemFactory(new ItemStack(Items.NAME_TAG),20,16,2)

            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.WEAPONSMITH,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new GoldForItemFactory(new ItemStack(Items.EMERALD) ,4,12,2),
                    new GoldForItemFactory(new ItemStack(Items.IRON_SWORD),6,12,2)
            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.IRON_AXE),9,12,2)

            ));

            integerListHashMap.put(3,List.of(
                    new GoldForItemFactory(new ItemStack(Items.IRON_INGOT),8,16,20),
                    new ItemForGold(Items.IRON_BARS,16,16,20)
            ));

            integerListHashMap.put(4,List.of(
                    new GoldForEnchantedTool(Items.IRON_SWORD,12,16,15),
                    new GoldForEnchantedTool(Items.IRON_AXE,12,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new PiglinSelectedEnchantedBook(20,Registries.ENCHANTMENT.stream().filter(enchantment ->
                            (
                                    enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_SWORD)) ||
                                    enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_AXE))) &&
                                    !enchantment.isTreasure()).toList())
            ));

        }));

        //Status DONE
        villagerProfessionMapHashMap.put(VillagerProfession.TOOLSMITH,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new GoldForItemFactory(new ItemStack(Items.EMERALD) ,4,12,2),
                    new GoldForItemFactory(new ItemStack(Items.IRON_SHOVEL),9,12,2)

            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.IRON_PICKAXE),9,12,2),
                    new GoldForItemFactory(new ItemStack(Items.IRON_HOE),9,12,2)
            ));

            integerListHashMap.put(3,List.of(
                    new GoldForItemFactory(new ItemStack(Items.IRON_INGOT),8,16,20),
                    new ItemForGold(Items.IRON_BARS,16,16,20)
            ));

            integerListHashMap.put(4,List.of(
                    new GoldForEnchantedTool(Items.IRON_SHOVEL,12,16,15),
                    new GoldForEnchantedTool(Items.IRON_PICKAXE,12,16,15),
                    new GoldForEnchantedTool(Items.IRON_HOE,12,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new PiglinSelectedEnchantedBook(20,Registries.ENCHANTMENT.stream().filter(enchantment -> (
                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_PICKAXE)) ||
                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_HOE)) ||
                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_SHOVEL))
                    ) && !enchantment.isTreasure()).toList())
            ));
        }));

        // Status Done
        villagerProfessionMapHashMap.put(VillagerProfession.FLETCHER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new ItemForGold(Items.FLINT,13,16,2),
                    new GoldForItemFactory(new ItemStack(Items.ARROW,16),1,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.BOW),3,16,2),
                    new ItemForGold(Items.STRING,12,16,2)

            ));

            integerListHashMap.put(3,List.of(
                    new ItemForGold(Items.FEATHER,16,16,2),
                    new GoldForItemFactory(new ItemStack(Items.CROSSBOW),4,16,15)
            ));

            integerListHashMap.put(4,List.of(
                    new ItemForGold(Items.TRIPWIRE_HOOK,8,2,16,2),
                    new GoldForItemFactory(new ItemStack(Items.TARGET),2,16,15)


            ));

            integerListHashMap.put(5,List.of(
                    new GoldForEnchantedTool(Items.CROSSBOW,18,16,20),
                    new GoldForItemFactory(new ItemStack(Items.SPECTRAL_ARROW,12),8,16,20)
            ));
        }));
        }
    ));

    public static final Map<Integer,List<TradeOffers.Factory>> PIGLIN_QUEST_OFFER = Util.make(Maps.newHashMap(), questMap ->{
        questMap.put(1,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.PIGLIN_QUEST)));
        questMap.put(2,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.PIGLIN_QUEST)));
        questMap.put(3,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.PIGLIN_QUEST)));
        questMap.put(4,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.PIGLIN_QUEST)));
        questMap.put(5,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.PIGLIN_QUEST)));
    });

}