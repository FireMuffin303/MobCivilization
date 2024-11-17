package net.firemuffin303.civilizedmobs.common.integration.muffinsQuest;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.muffinsquestlib.common.quest.Quest;
import net.firemuffin303.muffinsquestlib.common.quest.data.CollectItemQuestData;
import net.firemuffin303.muffinsquestlib.common.quest.data.KillEntityQuestData;
import net.firemuffin303.muffinsquestlib.common.registry.ModQuestTypes;
import net.firemuffin303.muffinsquestlib.common.registry.ModRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModQuests {
    public static final TagKey<Quest> PIGLIN_QUEST = TagKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"piglin_quest"));

    public static final RegistryKey<Quest> KILL_10_HOGLINS = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_10_hoglins"));
    public static final RegistryKey<Quest> KILL_20_HOGLINS = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_20_hoglins"));
    public static final RegistryKey<Quest> KILL_30_HOGLINS = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_30_hoglins"));

    public static final RegistryKey<Quest> KILL_10_WITHER_SKELETON = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_10_wither_skeleton"));
    public static final RegistryKey<Quest> KILL_20_WITHER_SKELETON = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_20_wither_skeleton"));
    public static final RegistryKey<Quest> KILL_30_WITHER_SKELETON = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_30_wither_skeleton"));

    public static final RegistryKey<Quest> KILL_10_MAGMA_CREAM = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_10_magma_cream"));
    public static final RegistryKey<Quest> KILL_20_MAGMA_CREAM = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_20_magma_cream"));
    public static final RegistryKey<Quest> KILL_30_MAGMA_CREAM = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_30_magma_cream"));

    public static final RegistryKey<Quest> KILL_10_GHAST = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_10_ghast"));
    public static final RegistryKey<Quest> KILL_20_GHAST = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_20_ghast"));
    public static final RegistryKey<Quest> KILL_30_GHAST = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_30_ghast"));

    public static final RegistryKey<Quest> KILL_10_BLAZE = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_10_blaze"));
    public static final RegistryKey<Quest> KILL_20_BLAZE = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_20_blaze"));
    public static final RegistryKey<Quest> KILL_30_BLAZE = RegistryKey.of(ModRegistries.QUEST_KEY,new Identifier(CivilizedMobs.MOD_ID,"kill_30_blaze"));

    public static void bootstrap(Registerable<Quest> questRegisterable){
        questRegisterable.register(KILL_10_HOGLINS,new Quest(new Quest.Definition(List.of(new ItemStack(Items.LEATHER,5)),20),"Kill 10 Hoglins")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.HOGLIN,10))));
        questRegisterable.register(KILL_20_HOGLINS,new Quest(new Quest.Definition(List.of(new ItemStack(Items.LEATHER,15),new ItemStack(Items.PORKCHOP,5)),50),"Kill 20 Hoglins")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.HOGLIN,20))));
        questRegisterable.register(KILL_30_HOGLINS,new Quest(new Quest.Definition(List.of(new ItemStack(Items.LEATHER,32),new ItemStack(Items.COOKED_PORKCHOP,15)),80),"Kill 30 Hoglins")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.HOGLIN,30))));

        questRegisterable.register(KILL_10_WITHER_SKELETON,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BONE,5)),20),"Kill 10 Wither Skeletons")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.WITHER_SKELETON,30))));
        questRegisterable.register(KILL_20_WITHER_SKELETON,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BONE,15),new ItemStack(Items.COAL,5)),50),"Kill 20 Wither Skeletons")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.WITHER_SKELETON,30))));
        questRegisterable.register(KILL_30_WITHER_SKELETON,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BONE,15),new ItemStack(Items.COAL,5),new ItemStack(Items.WITHER_SKELETON_SKULL)),80),"Kill 30 Wither Skeletons")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.WITHER_SKELETON,30))));

        questRegisterable.register(KILL_10_MAGMA_CREAM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.MAGMA_CREAM,5)),20),"Kill 10 Magma Cream")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.MAGMA_CUBE,10))));
        questRegisterable.register(KILL_20_MAGMA_CREAM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.MAGMA_CREAM,15)),50),"Kill 20 Magma Cream")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.MAGMA_CUBE,20))));
        questRegisterable.register(KILL_30_MAGMA_CREAM,new Quest(new Quest.Definition(List.of(new ItemStack(Items.MAGMA_CREAM,24), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE)),80),"Kill 30 Magma Cream")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.MAGMA_CUBE,30))));

        questRegisterable.register(KILL_10_GHAST,new Quest(new Quest.Definition(List.of(new ItemStack(Items.GUNPOWDER,5),new ItemStack(Items.GHAST_TEAR)),20),"Kill 10 Ghasts")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.GHAST,10))));
        questRegisterable.register(KILL_20_GHAST,new Quest(new Quest.Definition(List.of(new ItemStack(Items.GUNPOWDER,10),new ItemStack(Items.GHAST_TEAR,2)),50),"Kill 20 Ghasts")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.GHAST,20))));
        questRegisterable.register(KILL_30_GHAST,new Quest(new Quest.Definition(List.of(new ItemStack(Items.GUNPOWDER,16),new ItemStack(Items.GHAST_TEAR,5)),80),"Kill 30 Ghaasts")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.GHAST,30))));

        questRegisterable.register(KILL_10_BLAZE,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BLAZE_POWDER,2)),20),"Kill 10 Blazes")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.BLAZE,10))));
        questRegisterable.register(KILL_20_BLAZE,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BLAZE_POWDER,5),new ItemStack(Items.NETHER_WART)),50),"Kill 20 Blazes")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.BLAZE,20))));
        questRegisterable.register(KILL_30_BLAZE,new Quest(new Quest.Definition(List.of(new ItemStack(Items.BLAZE_POWDER,10),new ItemStack(Items.NETHER_WART,15)),80),"Kill 30 Blazes")
                .addQuest(ModQuestTypes.KILL_ENTITY_DATA,new KillEntityQuestData(new KillEntityQuestData.EntityRequirementEntry(EntityType.BLAZE,30))));
    }

    public static void register(RegistryWrapper.WrapperLookup wrapperLookup, FabricDynamicRegistryProvider.Entries entries) {
        var registry = wrapperLookup.getWrapperOrThrow(ModRegistries.QUEST_KEY);
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
    }
}
