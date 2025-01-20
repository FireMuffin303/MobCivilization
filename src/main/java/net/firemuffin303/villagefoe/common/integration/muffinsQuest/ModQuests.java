package net.firemuffin303.villagefoe.common.integration.muffinsQuest;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.muffinsquestlib.common.quest.Quest;
import net.firemuffin303.muffinsquestlib.common.quest.data.BlockRequirementEntry;
import net.firemuffin303.muffinsquestlib.common.quest.data.BreakBlockQuestData;
import net.firemuffin303.muffinsquestlib.common.quest.data.KillEntityQuestData;
import net.firemuffin303.muffinsquestlib.common.registry.QuestRegistries;
import net.firemuffin303.muffinsquestlib.common.registry.QuestTypes;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.raid.Raid;

import java.util.List;

public class ModQuests {
    public static final TagKey<Quest> PIGLIN_QUEST = TagKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"piglin_quest"));
    public static final TagKey<Quest> PILLAGER_QUEST = TagKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"pillager_quest"));
    public static final TagKey<Quest> WITHER_QUEST = TagKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"wither_quest"));

    //--- Piglin Quest ---
    public static final RegistryKey<Quest> KILL_10_HOGLINS = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_10_hoglins"));
    public static final RegistryKey<Quest> KILL_20_HOGLINS = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_20_hoglins"));
    public static final RegistryKey<Quest> KILL_30_HOGLINS = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_30_hoglins"));

    public static final RegistryKey<Quest> KILL_10_WITHER_SKELETON = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_10_wither_skeleton"));
    public static final RegistryKey<Quest> KILL_20_WITHER_SKELETON = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_20_wither_skeleton"));
    public static final RegistryKey<Quest> KILL_30_WITHER_SKELETON = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_30_wither_skeleton"));

    public static final RegistryKey<Quest> KILL_10_MAGMA_CREAM = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_10_magma_cream"));
    public static final RegistryKey<Quest> KILL_20_MAGMA_CREAM = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_20_magma_cream"));
    public static final RegistryKey<Quest> KILL_30_MAGMA_CREAM = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_30_magma_cream"));

    public static final RegistryKey<Quest> KILL_10_GHAST = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_10_ghast"));
    public static final RegistryKey<Quest> KILL_20_GHAST = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_20_ghast"));
    public static final RegistryKey<Quest> KILL_30_GHAST = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_30_ghast"));

    public static final RegistryKey<Quest> KILL_10_BLAZE = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_10_blaze"));
    public static final RegistryKey<Quest> KILL_20_BLAZE = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_20_blaze"));
    public static final RegistryKey<Quest> KILL_30_BLAZE = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_30_blaze"));

    //---------------------
    public static final RegistryKey<Quest> KILL_10_VILLAGER = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_10_villager"));
    public static final RegistryKey<Quest> KILL_20_VILLAGER = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_20_villager"));
    public static final RegistryKey<Quest> KILL_30_VILLAGER = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_30_villager"));

    public static final RegistryKey<Quest> KILL_10_IRON_GOLEM = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_10_iron_golem"));
    public static final RegistryKey<Quest> KILL_20_IRON_GOLEM = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_20_iron_golem"));
    public static final RegistryKey<Quest> KILL_30_IRON_GOLEM = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_30_iron_golem"));


    //---- Wither ----
    public static final RegistryKey<Quest> KILL_10_PIGLIN = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_10_piglins"));
    public static final RegistryKey<Quest> KILL_20_PIGLIN = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_20_piglins"));
    public static final RegistryKey<Quest> KILL_30_PIGLIN = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"kill_30_piglins"));

    // Mine Blocks
    public static final RegistryKey<Quest> MINE_32_NETHER_GOLD_ORE = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"mine_32_nether_gold_ore"));
    public static final RegistryKey<Quest> MINE_8_SCULK_SENSOR = RegistryKey.of(QuestRegistries.QUEST_KEY,new Identifier(VillageFoe.MOD_ID,"mine_8_sculk_sensor"));


    public static void bootstrap(Registerable<Quest> questRegisterable){
        //----- Kill Hoglins -----
        questRegisterable.register(KILL_10_HOGLINS,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.LEATHER,24),new ItemStack(Items.COOKED_PORKCHOP,12)),20),"Kill 10 Hoglins").setRarity(Quest.QuestRarity.COMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.HOGLIN,10))));

        questRegisterable.register(KILL_20_HOGLINS,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.LEATHER,32),new ItemStack(Items.COOKED_PORKCHOP,24),new ItemStack(Items.GOLD_INGOT,18)),50),"Kill 20 Hoglins").setRarity(Quest.QuestRarity.COMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.HOGLIN,20))));

        questRegisterable.register(KILL_30_HOGLINS,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.LEATHER,64),new ItemStack(Items.COOKED_PORKCHOP,48),new ItemStack(Items.PIGLIN_BANNER_PATTERN),new ItemStack(Items.GOLD_BLOCK,8)),80),"Kill 30 Hoglins").setRarity(Quest.QuestRarity.UNCOMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.HOGLIN,30))));

        //----- Kill Wither Skeleton -----
        questRegisterable.register(KILL_10_WITHER_SKELETON,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.BONE,32)),20),"Kill 10 Wither Skeletons").setRarity(Quest.QuestRarity.UNCOMMON)
                        .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.WITHER_SKELETON,30))));

        questRegisterable.register(KILL_20_WITHER_SKELETON,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.BONE,48),new ItemStack(Items.COAL,32),new ItemStack(Items.WITHER_SKELETON_SKULL)),50),"Kill 20 Wither Skeletons").setRarity(Quest.QuestRarity.UNCOMMON)
                        .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.WITHER_SKELETON,30))));

        questRegisterable.register(KILL_30_WITHER_SKELETON,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.BONE,48),new ItemStack(Items.COAL,32),new ItemStack(Items.WITHER_SKELETON_SKULL),new ItemStack(Items.SKULL_BANNER_PATTERN)),80),"Kill 30 Wither Skeletons").setRarity(Quest.QuestRarity.RARE)
                        .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.WITHER_SKELETON,30))));

        //----- Kill Magma Cream -----
        questRegisterable.register(KILL_10_MAGMA_CREAM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.MAGMA_CREAM,16)),20),"Kill 10 Magma Cream").setRarity(Quest.QuestRarity.COMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.MAGMA_CUBE,10))));
        questRegisterable.register(KILL_20_MAGMA_CREAM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.MAGMA_CREAM,24)),50),"Kill 20 Magma Cream").setRarity(Quest.QuestRarity.COMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.MAGMA_CUBE,20))));
        questRegisterable.register(KILL_30_MAGMA_CREAM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.MAGMA_CREAM,32), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE)),80),"Kill 30 Magma Cream").setRarity(Quest.QuestRarity.UNCOMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.MAGMA_CUBE,30))));

        //----- Kill Ghast -----
        questRegisterable.register(KILL_10_GHAST,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.GUNPOWDER,5),new ItemStack(Items.GHAST_TEAR,5)),20),"Kill 10 Ghasts").setRarity(Quest.QuestRarity.COMMON)
                        .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.GHAST,10))));
        questRegisterable.register(KILL_20_GHAST,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.GUNPOWDER,10),new ItemStack(Items.GHAST_TEAR,16)),50),"Kill 20 Ghasts").setRarity(Quest.QuestRarity.UNCOMMON)
                        .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.GHAST,20))));
        questRegisterable.register(KILL_30_GHAST,
                new Quest(new Quest.Definition(List.of(new ItemStack(Items.GUNPOWDER,16),new ItemStack(Items.GHAST_TEAR,32),PotionUtil.setPotion(new ItemStack(Items.POTION),Potions.REGENERATION)),80),"Kill 30 Ghaasts").setRarity(Quest.QuestRarity.UNCOMMON)
                        .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.GHAST,30))));

        //----- Kill Blaze -----
        questRegisterable.register(KILL_10_BLAZE,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BLAZE_POWDER,8)),20),"Kill 10 Blazes").setRarity(Quest.QuestRarity.UNCOMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.BLAZE,10))));
        questRegisterable.register(KILL_20_BLAZE,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BLAZE_POWDER,16),new ItemStack(Items.NETHER_WART)),50),"Kill 20 Blazes").setRarity(Quest.QuestRarity.UNCOMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.BLAZE,20))));
        questRegisterable.register(KILL_30_BLAZE,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BLAZE_POWDER,24),new ItemStack(Items.NETHER_WART,15),new ItemStack(Items.MUSIC_DISC_PIGSTEP)),80),"Kill 30 Blazes").setRarity(Quest.QuestRarity.UNCOMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.BLAZE,30))));

        //---------------------

        questRegisterable.register(KILL_10_VILLAGER,new Quest(new Quest.Definition(List.of(new ItemStack(Items.EMERALD,32)),20),"Kill 10 Villagers").setRarity(Quest.QuestRarity.COMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.VILLAGER,10))));

        questRegisterable.register(KILL_20_VILLAGER,new Quest(new Quest.Definition(List.of(new ItemStack(Items.EMERALD,64),new ItemStack(Items.EMERALD,32)),20),"Kill 20 Villagers").setRarity(Quest.QuestRarity.COMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.VILLAGER,20))));

        questRegisterable.register(KILL_30_VILLAGER,new Quest(new Quest.Definition(List.of(new ItemStack(Items.EMERALD,64),new ItemStack(Items.EMERALD,32), Raid.getOminousBanner(), EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(Enchantments.MENDING,1))),20),"Kill 30 Villagers").setRarity(Quest.QuestRarity.RARE)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.VILLAGER,30))));

        questRegisterable.register(KILL_10_IRON_GOLEM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.EMERALD,32)),50),"Kill 10 Iron Golems").setRarity(Quest.QuestRarity.COMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.IRON_GOLEM,10))));

        questRegisterable.register(KILL_20_IRON_GOLEM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.EMERALD,64),new ItemStack(Items.EMERALD,32),Raid.getOminousBanner(),new ItemStack(Items.DIAMOND,12)),80),"Kill 20 Iron Golems").setRarity(Quest.QuestRarity.UNCOMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.IRON_GOLEM,20))));

        questRegisterable.register(KILL_30_IRON_GOLEM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.EMERALD,64),new ItemStack(Items.EMERALD,32), Raid.getOminousBanner(), new ItemStack(Items.TOTEM_OF_UNDYING)),120),"Kill 30 Iron Golems").setRarity(Quest.QuestRarity.RARE)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.IRON_GOLEM,30))));

        //-----------------------

        //----- Kill Piglin -----
        questRegisterable.register(KILL_10_PIGLIN,new Quest(new Quest.Definition(List.of(new ItemStack(Items.GOLD_BLOCK,16)),50),"Kill 10 Piglins").setRarity(Quest.QuestRarity.COMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.PIGLIN,10))));

        questRegisterable.register(KILL_20_PIGLIN,new Quest(new Quest.Definition(List.of(new ItemStack(Items.GOLD_BLOCK,20),new ItemStack(Items.WITHER_ROSE,8)),60),"Kill 20 Piglins").setRarity(Quest.QuestRarity.UNCOMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.PIGLIN,20))));

        questRegisterable.register(KILL_30_PIGLIN,new Quest(new Quest.Definition(List.of(new ItemStack(Items.GOLD_BLOCK,24),new ItemStack(Items.PIGLIN_HEAD,1), new ItemStack(Items.WITHER_ROSE,16)),80),"Kill 30 Piglins").setRarity(Quest.QuestRarity.UNCOMMON)
                .addQuest(QuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.PIGLIN,30))));


        // ------ Mine Gold ------
        questRegisterable.register(MINE_32_NETHER_GOLD_ORE,new Quest(
                new Quest.Definition(
                        List.of(
                                EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(Enchantments.FORTUNE,2)))
                        ,40),"Mine 32 Nether Gold Ore")
                .addQuest(QuestTypes.BREAK_BLOCK_DATA,new BreakBlockQuestData(new BlockRequirementEntry(Blocks.NETHER_GOLD_ORE,32))).setRarity(Quest.QuestRarity.UNCOMMON)
        );

        questRegisterable.register(MINE_8_SCULK_SENSOR,new Quest(
                new Quest.Definition(
                        List.of(
                                new ItemStack(Items.SCULK_CATALYST,1),
                                new ItemStack(Items.SNOWBALL,16),
                                new ItemStack(Items.BLUE_WOOL,32),
                                PotionUtil.setPotion(new ItemStack(Items.POTION),Potions.NIGHT_VISION),
                                new ItemStack(Items.DIAMOND)
                        )
                        ,40),"Mine 8 Sculk Sensor")
                .addQuest(QuestTypes.BREAK_BLOCK_DATA,new BreakBlockQuestData(new BlockRequirementEntry(Blocks.SCULK_SENSOR,8))).setRarity(Quest.QuestRarity.UNCOMMON)
        );
    }

    public static void register(RegistryWrapper.WrapperLookup wrapperLookup, FabricDynamicRegistryProvider.Entries entries) {
        var registry = wrapperLookup.getWrapperOrThrow(QuestRegistries.QUEST_KEY);
        entries.add(registry,ModQuests.KILL_10_HOGLINS);
        entries.add(registry,ModQuests.KILL_20_HOGLINS);
        entries.add(registry,ModQuests.KILL_30_HOGLINS);

        entries.add(registry,ModQuests.KILL_10_WITHER_SKELETON);
        entries.add(registry,ModQuests.KILL_20_WITHER_SKELETON);
        entries.add(registry,ModQuests.KILL_30_WITHER_SKELETON);

        entries.add(registry,ModQuests.KILL_10_MAGMA_CREAM);
        entries.add(registry,ModQuests.KILL_20_MAGMA_CREAM);
        entries.add(registry,ModQuests.KILL_30_MAGMA_CREAM);

        entries.add(registry,ModQuests.KILL_10_GHAST);
        entries.add(registry,ModQuests.KILL_20_GHAST);
        entries.add(registry,ModQuests.KILL_30_GHAST);

        entries.add(registry,ModQuests.KILL_10_BLAZE);
        entries.add(registry,ModQuests.KILL_20_BLAZE);
        entries.add(registry,ModQuests.KILL_30_BLAZE);

        entries.add(registry,ModQuests.KILL_10_VILLAGER);
        entries.add(registry,ModQuests.KILL_20_VILLAGER);
        entries.add(registry,ModQuests.KILL_30_VILLAGER);

        entries.add(registry,ModQuests.KILL_10_IRON_GOLEM);
        entries.add(registry,ModQuests.KILL_20_IRON_GOLEM);
        entries.add(registry,ModQuests.KILL_30_IRON_GOLEM);

        entries.add(registry,ModQuests.KILL_10_PIGLIN);
        entries.add(registry,ModQuests.KILL_20_PIGLIN);
        entries.add(registry,ModQuests.KILL_30_PIGLIN);

        entries.add(registry,MINE_32_NETHER_GOLD_ORE);
        entries.add(registry,MINE_8_SCULK_SENSOR);
    }
}
