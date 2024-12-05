package net.firemuffin303.civilizedmobs.common.entity;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import net.firemuffin303.civilizedmobs.common.integration.muffinsQuest.ModQuests;
import net.firemuffin303.civilizedmobs.registry.ModTags;
import net.firemuffin303.muffinsquestlib.MuffinsQuestLib;
import net.firemuffin303.muffinsquestlib.common.item.QuestPaperItem;
import net.firemuffin303.muffinsquestlib.common.quest.Quest;
import net.firemuffin303.muffinsquestlib.common.registry.ModItems;
import net.firemuffin303.muffinsquestlib.common.registry.ModRegistries;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

public class ModWorkerOffers {
    public static float tradeMultiplier = 0.05f;
    public static final Map<VillagerProfession,Map<Integer, List<TradeOffers.Factory>>> PIGLIN_TRADES = Util.make(Maps.newHashMap(),(villagerProfessionMapHashMap -> {
        //Status Done
        villagerProfessionMapHashMap.put(VillagerProfession.ARMORER, Util.make(Maps.newHashMap(), (integerTradeOfferHashMap) -> {
            integerTradeOfferHashMap.put(1, List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.EMERALD) ,4,12,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_HELMET),6,12,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_CHESTPLATE),9,12,2)

            ));
            integerTradeOfferHashMap.put(2, List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.BELL),30,16,5),
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_LEGGINGS),8,12,5),
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_BOOTS),5,12,5)
            ));

            integerTradeOfferHashMap.put(3, List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.SHIELD),15,16,10),
                    new PiglinItemForGold(Items.LAVA_BUCKET,1,16,10),
                    new PiglinGoldForItemFactory(new ItemStack(Items.IRON_INGOT),8,16,20),
                    new PiglinItemForGold(Items.IRON_BARS,16,16,20)
            ));

            integerTradeOfferHashMap.put(4, List.of(
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_HELMET,18,16,20),
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_CHESTPLATE,18,16,20),
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_LEGGINGS,18,16,20),
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_BOOTS,18,16,20)
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
                    new PiglinItemForGold(Items.PORKCHOP,14,16,2),
                    new PiglinItemForGold(Items.LEATHER,14,16,2),
                    new PiglinSuspiciousStewFactory(StatusEffects.BLINDNESS,200,4,5),
                    new PiglinSuspiciousStewFactory(StatusEffects.NAUSEA,200,4,5),
                    new PiglinGoldForItemFactory(new ItemStack(Items.MUSHROOM_STEW),2,14,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new PiglinItemForGold(Items.COAL,15,2,16,5),
                    new PiglinGoldForItemFactory(new ItemStack(Items.COOKED_PORKCHOP,5),3,13,5)
            ));

            integerListHashMap.put(3,List.of(
                    new PiglinItemForGold(Items.BEEF,32,16,10),
                    new PiglinItemForGold(Items.MUTTON,32,16,10),
                    new PiglinItemForGold(Items.CHICKEN,32,16,10)
            ));


            integerListHashMap.put(4,List.of(
                    new PiglinItemForGold(Items.NETHER_WART_BLOCK,48,16,15),
                    new PiglinItemForGold(Items.WARPED_WART_BLOCK,48,16,15),
                    new PiglinItemForGold(Items.CRIMSON_FUNGUS,32,16,15),
                    new PiglinItemForGold(Items.WARPED_FUNGUS,32,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.SHROOMLIGHT),8,16,20),
                    new PiglinGoldForItemFactory(new ItemStack(Items.COOKED_BEEF),8,13,20),
                    new PiglinGoldForItemFactory(new ItemStack(Items.COOKED_PORKCHOP),8,13,20),
                    new PiglinGoldForItemFactory(new ItemStack(Items.COOKED_CHICKEN),8,13,20)
            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.CLERIC,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new PiglinItemForGold(Items.ROTTEN_FLESH,32,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.REDSTONE,2),1,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.LAPIS_LAZULI),1,12,5),
                    new PiglinGoldForItemFactory(new ItemStack(Items.GLOWSTONE),2,12,5)
            ));

            integerListHashMap.put(3,List.of(
                    new PiglinItemForGold(Items.RABBIT_FOOT,1,12,10),
                    new PiglinItemForGold(Items.BLAZE_POWDER,3,12,10)
            ));

            integerListHashMap.put(4,List.of(
                    new PiglinItemForGold(Items.GLASS_BOTTLE,9,16,25),
                    new PiglinItemForGold(Items.SCUTE,9,3,16,25),
                    new PiglinGoldForItemFactory(new ItemStack(Items.ENDER_PEARL),12,16,25)
            ));


            integerListHashMap.put(5,List.of(
                    new PiglinItemForGold(Items.NETHER_WART,12,16,30),
                    new PiglinGoldForItemFactory(new ItemStack(Items.EXPERIENCE_BOTTLE),12,16,30)
            ));

        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.CARTOGRAPHER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new PiglinItemForGold(Items.PAPER,24,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.MAP),1,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.GLASS_PANE),1,16,2),
                    new PiglinItemForGold(Items.INK_SAC,24,16,2),
                    new PiglinItemForGold(Items.FEATHER,16,16,2)
            ));

            integerListHashMap.put(3,List.of(
                    new PiglinSellMapFactory(16, ModTags.ON_NETHER_FORTRESS_EXPLORER_MAP,"filled_map.fortress", MapIcon.Type.RED_X,16,15),
                    new PiglinItemForGold(Items.COMPASS,1,16,2)
            ));

            integerListHashMap.put(4,List.of(
                    new PiglinSellMapFactory(18, ModTags.ON_BASTION_EXPLORER_MAP,"filled_map.bastion", MapIcon.Type.RED_X,16,20),
                    new PiglinGoldAndItemForItemFactory(new ItemStack(Items.GLOW_INK_SAC,8),1,Items.INK_SAC,8,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.GLOW_ITEM_FRAME),7,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.PIGLIN_BANNER_PATTERN),9,16,2)
            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.LEATHERWORKER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new PiglinItemForGold(Items.LEATHER,8,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.LEATHER_HELMET),19,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.LEATHER_CHESTPLATE),19,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new PiglinItemForGold(Items.FLINT,28,16,5),
                    new PiglinGoldForItemFactory(new ItemStack(Items.LEATHER_LEGGINGS),19,16,5),
                    new PiglinGoldForItemFactory(new ItemStack(Items.LEATHER_BOOTS),19,16,5)

            ));

            integerListHashMap.put(3,List.of(
                    new PiglinItemForGold(Items.RABBIT_HIDE,8,16,10),
                    new PiglinGoldForItemFactory(new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK),8,16,10)
            ));

            integerListHashMap.put(4,List.of(
                    new PiglinItemForGold(Items.SCUTE,9,3,16,25),
                    new PiglinItemForGold(Items.RABBIT_FOOT,2,4,16,25)

            ));

            integerListHashMap.put(5,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.SADDLE),16,16,30),
                    new PiglinGoldForItemFactory(new ItemStack(Items.NAME_TAG),20,16,30)
            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.LIBRARIAN,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new PiglinItemForGold(Items.PAPER,24,16,2),
                    new PiglinItemForGold(Items.BOOK,4,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.BOOKSHELF) ,4,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.LANTERN,2),1,16,2),
                    new PiglinItemForGold(Items.INK_SAC,12,16,2)
            ));

            integerListHashMap.put(3,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.GLASS,4),1,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.CLOCK,1),8,16,2)

                    ));

            integerListHashMap.put(4,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.COMPASS),6,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.WRITABLE_BOOK),6,16,2)

            ));

            integerListHashMap.put(5,List.of(
                    new PiglinSoulSpeedFactory(20),
                    new PiglinGoldForItemFactory(new ItemStack(Items.NAME_TAG),20,16,2)

            ));
        }));

        //DONE
        villagerProfessionMapHashMap.put(VillagerProfession.WEAPONSMITH,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.EMERALD) ,4,12,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_SWORD),6,12,2)
            ));

            integerListHashMap.put(2,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_AXE),9,12,2)

            ));

            integerListHashMap.put(3,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.IRON_INGOT),8,16,20),
                    new PiglinItemForGold(Items.IRON_BARS,16,16,20)
            ));

            integerListHashMap.put(4,List.of(
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_SWORD,12,16,15),
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_AXE,12,16,15)
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
                    new PiglinGoldForItemFactory(new ItemStack(Items.EMERALD) ,4,12,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_SHOVEL),9,12,2)

            ));

            integerListHashMap.put(2,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_PICKAXE),9,12,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.GOLDEN_HOE),9,12,2)
            ));

            integerListHashMap.put(3,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.IRON_INGOT),8,16,20),
                    new PiglinItemForGold(Items.IRON_BARS,16,16,20)
            ));

            integerListHashMap.put(4,List.of(
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_SHOVEL,12,16,15),
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_PICKAXE,12,16,15),
                    new PiglinGoldForEnchantedTool(Items.GOLDEN_HOE,12,16,15)
            ));

            integerListHashMap.put(5,List.of(
                    new PiglinSelectedEnchantedBook(20,Registries.ENCHANTMENT.stream().filter(enchantment -> {
                        return (enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_PICKAXE)) ||
                                enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_HOE)) ||
                                enchantment.isAcceptableItem(new ItemStack(Items.GOLDEN_SHOVEL))) && !enchantment.isTreasure();
                    }).toList())
            ));
        }));

        // Status Done
        villagerProfessionMapHashMap.put(VillagerProfession.FLETCHER,Util.make(Maps.newHashMap(),integerListHashMap -> {
            integerListHashMap.put(1,List.of(
                    new PiglinItemForGold(Items.FLINT,13,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.ARROW,16),1,16,2)
            ));

            integerListHashMap.put(2,List.of(
                    new PiglinGoldForItemFactory(new ItemStack(Items.BOW),3,16,2),
                    new PiglinItemForGold(Items.STRING,12,16,2)

            ));

            integerListHashMap.put(3,List.of(
                    new PiglinItemForGold(Items.FEATHER,16,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.CROSSBOW),4,16,15)
            ));

            integerListHashMap.put(4,List.of(
                    new PiglinItemForGold(Items.TRIPWIRE_HOOK,8,2,16,2),
                    new PiglinGoldForItemFactory(new ItemStack(Items.TARGET),2,16,15)


            ));

            integerListHashMap.put(5,List.of(
                    new PiglinGoldForEnchantedTool(Items.CROSSBOW,18,16,20),
                    new PiglinGoldForItemFactory(new ItemStack(Items.SPECTRAL_ARROW,12),8,16,20)
            ));
        }));
        }
    ));

    public static final Map<Integer,List<TradeOffers.Factory>> PIGLIN_QUEST_OFFER = Util.make(Maps.newHashMap(), questMap ->{
        questMap.put(1,List.of(new PiglinQuestOfferFactory(5)));
        questMap.put(2,List.of(new PiglinQuestOfferFactory(10)));
        questMap.put(3,List.of(new PiglinQuestOfferFactory(20)));
        questMap.put(4,List.of(new PiglinQuestOfferFactory(30)));
        questMap.put(5,List.of(new PiglinQuestOfferFactory(40)));
    });

    public static final Map<VillagerProfession,Map<Integer,List<TradeOffers.Factory>>> PILLAGER_TRADES = Util.make(Maps.newHashMap(),villagerProfessionMapHashMap -> {
       villagerProfessionMapHashMap.put(VillagerProfession.ARMORER,Util.make(Maps.newHashMap(),integerListHashMap -> {
           integerListHashMap.put(1,List.of());
       })) ;
    });

    public static class PiglinItemForGold implements TradeOffers.Factory {
        private final Item buy;
        private final int amount;
        private final int goldAmount;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public PiglinItemForGold(Item buy, int amount, int maxUses, int experience){
            this(buy,amount,1,maxUses,experience,0.05f);
        }

        public PiglinItemForGold(Item buy, int amount,int goldAmount, int maxUses, int experience){
            this(buy,amount,goldAmount,maxUses,experience,0.05f);
        }

        public PiglinItemForGold(Item buy, int amount,int goldAmount, int maxUses, int experience, float multiplier){
            this.buy = buy;
            this.amount = amount;
            this.goldAmount = goldAmount;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(this.buy,this.amount),new ItemStack(Items.GOLD_INGOT,this.goldAmount),this.maxUses,this.experience,this.multiplier);
        }
    }

    public static class PiglinGoldForItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final int price;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public PiglinGoldForItemFactory(ItemStack sell, int price, int maxUses, int experience) {
            this(sell, price, maxUses, experience, 0.05F);
        }
        public PiglinGoldForItemFactory(ItemStack item, int price, int maxUses, int experience, float multiplier){
            this.sell = item;
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(Items.GOLD_INGOT, this.price), this.sell, this.maxUses, this.experience, this.multiplier);
        }
    }

    public static class PiglinGoldAndItemForItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final int price;
        private final Item secondBuy;
        private final int secondPrice;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public PiglinGoldAndItemForItemFactory(ItemStack sell, int price,Item secondBuy,int secondPrice , int maxUses, int experience) {
            this(sell, price,secondBuy,secondPrice, maxUses, experience, 0.05F);
        }
        public PiglinGoldAndItemForItemFactory(ItemStack item, int price,Item secondBuy,int secondPrice, int maxUses, int experience, float multiplier){
            this.sell = item;
            this.price = price;
            this.secondBuy = secondBuy;
            this.secondPrice = secondPrice;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(Items.GOLD_INGOT, this.price),new ItemStack(this.secondBuy,this.secondPrice), this.sell, this.maxUses, this.experience, this.multiplier);
        }
    }

    public static class PiglinEnchantedBookFactory implements TradeOffers.Factory{
        private final int experience;

        public PiglinEnchantedBookFactory(int experience){
            this.experience = experience;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            List<Enchantment> list = Registries.ENCHANTMENT.stream().filter(enchantment -> enchantment.isAvailableForEnchantedBookOffer() || enchantment == Enchantments.SOUL_SPEED).toList();
            Enchantment enchantment = list.get(random.nextInt(list.size()));
            int i = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasure()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }

            return new TradeOffer(new ItemStack(Items.GOLD_INGOT, j), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F);
        }
    }

    public static class PiglinSoulSpeedFactory implements TradeOffers.Factory{
        private final int experience;

        public PiglinSoulSpeedFactory(int experience){
            this.experience = experience;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            Enchantment enchantment = Enchantments.SOUL_SPEED;
            int i = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasure()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }

            return new TradeOffer(new ItemStack(Items.GOLD_INGOT, j), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F);
        }
    }

    public static class PiglinSellMapFactory implements TradeOffers.Factory{
        private final int price;
        private final TagKey<Structure> structure;
        private final String nameKey;
        private final MapIcon.Type iconType;
        private final int maxUses;
        private final int experience;

        public PiglinSellMapFactory(int price,TagKey<Structure> structure,String nameKey,MapIcon.Type iconType,int maxUses,int experience){
            this.price = price;
            this.structure  = structure;
            this.nameKey = nameKey;
            this.iconType = iconType;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            if (!(entity.getWorld() instanceof ServerWorld)) {
                return null;
            } else {
                ServerWorld serverWorld = (ServerWorld)entity.getWorld();
                BlockPos blockPos = serverWorld.locateStructure(this.structure, entity.getBlockPos(), 100, true);
                if (blockPos != null) {
                    ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte)2, true, true);
                    FilledMapItem.fillExplorationMap(serverWorld, itemStack);
                    MapState.addDecorationsNbt(itemStack, blockPos, "+", this.iconType);
                    itemStack.setCustomName(Text.translatable(this.nameKey));
                    return new TradeOffer(new ItemStack(Items.GOLD_INGOT, this.price), new ItemStack(Items.COMPASS), itemStack, this.maxUses, this.experience, 0.2F);
                } else {
                    return null;
                }
            }
        }
    }

    public static class PiglinSuspiciousStewFactory implements TradeOffers.Factory{
        final StatusEffect effect;
        final int duration;
        final int price;
        final int experience;
        final float multiplier;

        public PiglinSuspiciousStewFactory(StatusEffect statusEffect,int duration,int price,int experience){
            this(statusEffect,duration,price,experience,0.05f);
        }

        public PiglinSuspiciousStewFactory(StatusEffect statusEffect,int duration,int price,int experience,float multiplier){
            this.effect = statusEffect;
            this.duration = duration;
            this.price = price;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW,1);
            SuspiciousStewItem.addEffectToStew(itemStack,this.effect,this.duration);
            return new TradeOffer(new ItemStack(Items.GOLD_INGOT,this.price),itemStack,12,this.experience,this.multiplier);
        }
    }

    public static class PiglinStructureCompassFactory implements TradeOffers.Factory{

        final TagKey<Structure> structureTagKey;
        final int price;
        final int experience;

        public PiglinStructureCompassFactory(TagKey<Structure> structureTagKey,int price,int experience){
            this.structureTagKey = structureTagKey;
            this.price = price;
            this.experience = experience;
        }

        private NbtCompound writeNbt(RegistryKey<World> worldKey, BlockPos pos, NbtCompound nbt) {
            nbt.put("LodestonePos", NbtHelper.fromBlockPos(pos));
            DataResult<NbtElement> dataResult = World.CODEC.encodeStart(NbtOps.INSTANCE, worldKey);
            Logger loggger = LogUtils.getLogger();
            Objects.requireNonNull(loggger);
            dataResult.resultOrPartial(loggger::error).ifPresent((nbtElement) -> nbt.put("LodestoneDimension", nbtElement));
            nbt.putBoolean("LodestoneTracked", true);
            return nbt;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(Items.COMPASS);
            ServerWorld serverWorld = (ServerWorld) entity.getWorld();
            BlockPos blockPos = entity.getBlockPos();
            BlockPos structurePos = serverWorld.locateStructure(this.structureTagKey,blockPos,100,true);
            itemStack.setNbt(this.writeNbt(entity.getWorld().getRegistryKey(),structurePos,new NbtCompound()));

            return new TradeOffer(new ItemStack(Items.GOLD_INGOT,this.price),new ItemStack(Items.COMPASS,1),itemStack,1,this.experience,0.05f);
        }
    }

    public static class PiglinGoldForEnchantedTool implements TradeOffers.Factory{
        private final ItemStack tool;
        private final int basePrice;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public PiglinGoldForEnchantedTool(Item item, int basePrice, int maxUses, int experience) {
            this(item, basePrice, maxUses, experience, 0.05F);
        }

        public PiglinGoldForEnchantedTool(Item item, int basePrice, int maxUses, int experience, float multiplier) {
            this.tool = new ItemStack(item);
            this.basePrice = basePrice;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            int i = 5 + random.nextInt(15);
            ItemStack itemStack = EnchantmentHelper.enchant(random, new ItemStack(this.tool.getItem()), i, false);
            int j = Math.min(this.basePrice + i, 64);
            ItemStack itemStack2 = new ItemStack(Items.GOLD_INGOT, j);
            return new TradeOffer(itemStack2, itemStack, this.maxUses, this.experience, this.multiplier);
        }
    }

    public static class PiglinSelectedEnchantedBook implements TradeOffers.Factory{

        final int experience;
        final List<Enchantment> enchantments;

        public PiglinSelectedEnchantedBook(int experience, List<Enchantment> enchantments){
            this.experience = experience;
            this.enchantments = enchantments;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            Enchantment enchantment = this.enchantments.get(random.nextInt(this.enchantments.size()));
            int i = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasure()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }
            return new TradeOffer(new ItemStack(Items.GOLD_INGOT, j), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F);
        }
    }

    public static class PiglinQuestOfferFactory implements TradeOffers.Factory{
        final int experience;

        public PiglinQuestOfferFactory(int experience){
            this.experience = experience;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            List<RegistryEntry.Reference<Quest>> quests = entity.getWorld().getRegistryManager().get(ModRegistries.QUEST_KEY).streamEntries().filter(questReference -> questReference.isIn(ModQuests.PIGLIN_QUEST)).toList();

            RegistryEntry.Reference<Quest> questReference = quests.get(random.nextInt(quests.size()-1));

            Quest quest = entity.getWorld().getRegistryManager().get(ModRegistries.QUEST_KEY).get(questReference.registryKey().getValue());
            int price;
            switch (quest.questRarity){
                case UNCOMMON -> price = 24;
                case RARE -> price = 32;
                default -> price = 16;
            }
            price = price + random.nextBetween(2,6);

            return new TradeOffer(new ItemStack(Items.GOLD_INGOT,price), QuestPaperItem.getQuestPaper(questReference.registryKey().getValue(),24000),1,this.experience,0.05f);
        }
    }
}