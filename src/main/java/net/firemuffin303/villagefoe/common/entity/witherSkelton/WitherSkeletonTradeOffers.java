package net.firemuffin303.villagefoe.common.entity.witherSkelton;

import com.google.common.collect.Maps;
import net.firemuffin303.villagefoe.common.integration.muffinsQuest.ModQuests;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Util;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.firemuffin303.villagefoe.api.VillageFoeOffersFactories.*;

import java.util.List;
import java.util.Map;

public class WitherSkeletonTradeOffers {

    public static final Map<VillagerProfession, Map<Integer, List<TradeOffers.Factory>>> WITHER_TRADES = Util.make(Maps.newHashMap(), villagerProfessionMapHashMap -> {
        villagerProfessionMapHashMap.put(VillagerProfession.ARMORER,Util.make(Maps.newHashMap(),integerTradeOfferHashMap -> {
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
                    new PiglinSelectedEnchantedBook(20, Registries.ENCHANTMENT.stream().filter(enchantment ->
                            (
                                    enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_HELMET)) ||
                                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_CHESTPLATE)) ||
                                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_LEGGINGS)) ||
                                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_BOOTS))
                            ) && !enchantment.isTreasure()).toList())
            ));
        }));

        //Bu
        villagerProfessionMapHashMap.put(VillagerProfession.BUTCHER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new ItemForGold(Items.PORKCHOP,14,16,2),
                    new ItemForGold(Items.LEATHER,14,16,2),
                    new PiglinSuspiciousStewFactory(StatusEffects.BLINDNESS,200,4,5),
                    new PiglinSuspiciousStewFactory(StatusEffects.NAUSEA,200,4,5),
                    new GoldForItemFactory(new ItemStack(Items.MUSHROOM_STEW),2,14,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new ItemForGold(Items.COAL,15,2,16,5),
                    new GoldForItemFactory(new ItemStack(Items.COOKED_PORKCHOP,24),3,13,5)
            ));

            integerListHashMap.put(3,List.of(
                    new ItemForGold(Items.BEEF,24,16,10),
                    new ItemForGold(Items.MUTTON,24,16,10),
                    new ItemForGold(Items.CHICKEN,24,16,10)
            ));


            integerListHashMap.put(4,List.of(
                    new ItemForGold(Items.NETHER_WART_BLOCK,48,16,15),
                    new ItemForGold(Items.WARPED_WART_BLOCK,48,16,15),
                    new ItemForGold(Items.CRIMSON_FUNGUS,32,16,15),
                    new ItemForGold(Items.WARPED_FUNGUS,32,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new GoldForItemFactory(new ItemStack(Items.SHROOMLIGHT,4),8,16,20),
                    new GoldForItemFactory(new ItemStack(Items.COOKED_BEEF,16),8,13,20),
                    new GoldForItemFactory(new ItemStack(Items.COOKED_PORKCHOP,16),8,13,20),
                    new GoldForItemFactory(new ItemStack(Items.COOKED_CHICKEN,16),8,13,20)
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.WEAPONSMITH,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new GoldForItemFactory(new ItemStack(Items.EMERALD) ,4,12,2),
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_SWORD),6,12,2)
            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_AXE),9,12,2)

            ));

            integerListHashMap.put(3,List.of(
                    new GoldForItemFactory(new ItemStack(Items.IRON_INGOT),8,16,20),
                    new ItemForGold(Items.IRON_BARS,16,16,20)
            ));

            integerListHashMap.put(4,List.of(
                    new GoldForEnchantedTool(Items.GOLDEN_SWORD,12,16,15),
                    new GoldForEnchantedTool(Items.GOLDEN_AXE,12,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new PiglinSelectedEnchantedBook(20,Registries.ENCHANTMENT.stream().filter(enchantment ->
                            (
                                    enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_SWORD)) ||
                                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_AXE))) &&
                                    !enchantment.isTreasure()).toList())
            ));
        }));

        villagerProfessionMapHashMap.put(VillagerProfession.TOOLSMITH,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new GoldForItemFactory(new ItemStack(Items.EMERALD) ,4,12,2),
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_SHOVEL),9,12,2)

            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_PICKAXE),9,12,2),
                    new GoldForItemFactory(new ItemStack(Items.GOLDEN_HOE),9,12,2)
            ));

            integerListHashMap.put(3,List.of(
                    new GoldForItemFactory(new ItemStack(Items.IRON_INGOT),8,16,20),
                    new ItemForGold(Items.IRON_BARS,16,16,20)
            ));

            integerListHashMap.put(4,List.of(
                    new GoldForEnchantedTool(Items.GOLDEN_SHOVEL,12,16,15),
                    new GoldForEnchantedTool(Items.GOLDEN_PICKAXE,12,16,15),
                    new GoldForEnchantedTool(Items.GOLDEN_HOE,12,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new PiglinSelectedEnchantedBook(20,Registries.ENCHANTMENT.stream().filter(enchantment -> (
                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_PICKAXE)) ||
                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_HOE)) ||
                            enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_SHOVEL))
                    ) && !enchantment.isTreasure()).toList())
            ));
        }));

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

        villagerProfessionMapHashMap.put(VillagerProfession.MASON,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new ItemForGold(Items.NETHERRACK,32,16,2),
                    new GoldForItemFactory(new ItemStack(Items.NETHER_BRICK,10),1,12,1,0.2F),
                    new ItemForGold(Items.GRAVEL,32,16,2)

            ));

            integerListHashMap.put(2,List.of(
                    new GoldForItemFactory(new ItemStack(Items.BLACKSTONE,16), 1, 12, 5, 0.2F),
                    new GoldForItemFactory(new ItemStack(Items.CHISELED_POLISHED_BLACKSTONE,4), 1, 12, 5, 0.2F)
            ));

            integerListHashMap.put(3,List.of(
                    new ItemForGold(Items.SOUL_SAND,32,16,5),
                    new ItemForGold(Items.SOUL_SOIL,32,16,5),
                    new GoldForItemFactory(new ItemStack(Items.RED_NETHER_BRICKS,16), 8, 12, 10, 0.2F),
                    new GoldForItemFactory(new ItemStack(Items.CHISELED_NETHER_BRICKS,16), 8, 12, 10, 0.2F)
            ));

            integerListHashMap.put(4,List.of(
                    new ItemForGold(Items.QUARTZ,12,16,30),
                    new GoldForItemFactory(new ItemStack(Items.QUARTZ_BLOCK,12),1,12,20,0.2F),
                    new GoldForItemFactory(new ItemStack(Items.QUARTZ_PILLAR,12),1,12,20,0.2F)
            ));

            integerListHashMap.put(5,List.of(
                    new GoldForItemFactory(new ItemStack(Items.QUARTZ_BRICKS,12),1,12,30,0.2F),
                    new GoldForItemFactory(new ItemStack(Items.GILDED_BLACKSTONE,12),16,12,30,0.2F)
            ));
        }));
    });

    public static final Map<Integer,List<TradeOffers.Factory>> WITHER_QUSET_OFFER = Util.make(Maps.newHashMap(), questMap ->{
        questMap.put(1,List.of(new QuestOfferFactory(Items.GOLD_INGOT, ModQuests.WITHER_QUEST)));
        questMap.put(2,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.WITHER_QUEST)));
        questMap.put(3,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.WITHER_QUEST)));
        questMap.put(4,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.WITHER_QUEST)));
        questMap.put(5,List.of(new QuestOfferFactory(Items.GOLD_INGOT,ModQuests.WITHER_QUEST)));
    });
}
