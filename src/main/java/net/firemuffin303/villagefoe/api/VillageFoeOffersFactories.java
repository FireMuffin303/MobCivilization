package net.firemuffin303.villagefoe.api;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import net.firemuffin303.villagefoe.common.entity.pillager.IllagerTradeOffers;
import net.firemuffin303.villagefoe.common.integration.muffinsQuest.ModQuests;
import net.firemuffin303.muffinsquestlib.common.item.QuestPaperItem;
import net.firemuffin303.muffinsquestlib.common.quest.Quest;
import net.firemuffin303.muffinsquestlib.common.registry.QuestRegistries;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.*;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VillageFoeOffersFactories {

    //Piglin & Wither
    public static class ItemForGold implements TradeOffers.Factory {
        private final Item buy;
        private final int amount;
        private final int goldAmount;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public ItemForGold(Item buy, int amount, int maxUses, int experience){
            this(buy,amount,1,maxUses,experience,0.05f);
        }

        public ItemForGold(Item buy, int amount, int goldAmount, int maxUses, int experience){
            this(buy,amount,goldAmount,maxUses,experience,0.05f);
        }

        public ItemForGold(Item buy, int amount, int goldAmount, int maxUses, int experience, float multiplier){
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

    public static class GoldForItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final int price;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public GoldForItemFactory(ItemStack sell, int price, int maxUses, int experience) {
            this(sell, price, maxUses, experience, 0.05F);
        }
        public GoldForItemFactory(ItemStack item, int price, int maxUses, int experience, float multiplier){
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

    public static class GoldAndItemForItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final int price;
        private final Item secondBuy;
        private final int secondPrice;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public GoldAndItemForItemFactory(ItemStack sell, int price, Item secondBuy, int secondPrice , int maxUses, int experience) {
            this(sell, price,secondBuy,secondPrice, maxUses, experience, 0.05F);
        }
        public GoldAndItemForItemFactory(ItemStack item, int price, Item secondBuy, int secondPrice, int maxUses, int experience, float multiplier){
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
            if (!(entity.getWorld() instanceof ServerWorld serverWorld)) {
                return null;
            } else {
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

    public static class GoldForEnchantedTool implements TradeOffers.Factory{
        private final ItemStack tool;
        private final int basePrice;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public GoldForEnchantedTool(Item item, int basePrice, int maxUses, int experience) {
            this(item, basePrice, maxUses, experience, 0.05F);
        }

        public GoldForEnchantedTool(Item item, int basePrice, int maxUses, int experience, float multiplier) {
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
            List<RegistryEntry.Reference<Quest>> quests = entity.getWorld().getRegistryManager().get(QuestRegistries.QUEST_KEY).streamEntries().filter(questReference -> questReference.isIn(ModQuests.PIGLIN_QUEST)).toList();

            RegistryEntry.Reference<Quest> questReference = quests.get(random.nextInt(quests.size()-1));

            Quest quest = entity.getWorld().getRegistryManager().get(QuestRegistries.QUEST_KEY).get(questReference.registryKey().getValue());
            if(quest == null){
                return null;
            }

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

    public static class QuestOfferFactory implements TradeOffers.Factory{
        private final Item item;
        private final TagKey<Quest> questTagKey;

        public QuestOfferFactory(Item item,TagKey<Quest> questTagKey){
            this.item = item;
            this.questTagKey = questTagKey;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            List<RegistryEntry.Reference<Quest>> quests = entity.getWorld().getRegistryManager().get(QuestRegistries.QUEST_KEY).streamEntries().filter(questReference -> questReference.isIn(this.questTagKey)).toList();

            RegistryEntry.Reference<Quest> questReference = quests.get(random.nextInt(quests.size()-1));

            Quest quest = entity.getWorld().getRegistryManager().get(QuestRegistries.QUEST_KEY).get(questReference.registryKey().getValue());
            if(quest == null){
                return null;
            }
            int price;
            int exp;
            switch (quest.questRarity){
                case UNCOMMON -> {
                    price = 24;
                    exp = 25;
                }
                case RARE -> {
                    price = 32;
                    exp = 50;
                }
                default -> {
                    price = 16;
                    exp = 10;
                }
            }
            price = price + random.nextBetween(2,6);

            return new TradeOffer(new ItemStack(this.item,price), QuestPaperItem.getQuestPaper(questReference.registryKey().getValue(),24000),1,exp,0.05f);
        }
    }

    //Illager
    //Sell Potion Bottle.
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
            return new TradeOffer(new ItemStack(Items.EMERALD,32 ),new ItemStack(Items.NETHER_WART,1), PotionUtil.setPotion(new ItemStack(Items.POTION),potion),12,this.experience,0.2f);
        }
    }

    //Sell Enchanted Book for a specific tool
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
