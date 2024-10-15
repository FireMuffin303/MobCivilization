package net.firemuffin303.civilizedmobs.common.quest;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class QuestPoolTypes {

    public static final RegistryKey<QuestPool> PIGLIN_QUEST_KEY = RegistryKey.of(CivilizedMobs.QUEST_POOL,new Identifier(CivilizedMobs.MOD_ID,"piglin_quest_pool"));
    public static final RegistryKey<QuestPool> WITHER_QUEST_KEY = RegistryKey.of(CivilizedMobs.QUEST_POOL,new Identifier(CivilizedMobs.MOD_ID,"wither_quest_pool"));
    public static final RegistryKey<QuestPool> ILLAGER_QUEST_KEY = RegistryKey.of(CivilizedMobs.QUEST_POOL,new Identifier(CivilizedMobs.MOD_ID,"illager_quest_pool"));
    public static void init(){}

    public static QuestPool piglinQuest(){

        QuestList level1 = new QuestList();
        level1.add(new Quest(new ItemStack(Items.GOLD_INGOT,2),ItemStack.EMPTY,60,2));
        level1.add(new Quest(new ItemStack(Items.DIAMOND,32),ItemStack.EMPTY,60,2));

        QuestList level2 = new QuestList();
        level2.add(new Quest(new ItemStack(Items.ENDER_PEARL,2),ItemStack.EMPTY,60,5));
        level2.add(new Quest(new ItemStack(Items.BLAZE_POWDER,32),ItemStack.EMPTY,60,5));

        QuestList level3 = new QuestList();
        level3.add(new Quest(new ItemStack(Items.BEEF,2),ItemStack.EMPTY,60,5));
        level3.add(new Quest(new ItemStack(Items.FARMLAND,32),ItemStack.EMPTY,60,5));

        QuestList level4 = new QuestList();
        level4.add(new Quest(new ItemStack(Items.EGG,2),ItemStack.EMPTY,60,5));
        level4.add(new Quest(new ItemStack(Items.ECHO_SHARD,32),ItemStack.EMPTY,60,5));

        QuestList level5 = new QuestList();
        level5.add(new Quest(new ItemStack(Items.QUARTZ,2),ItemStack.EMPTY,60,5));
        level5.add(new Quest(new ItemStack(Items.FIREWORK_STAR,32),ItemStack.EMPTY,60,5));

        return new QuestPool(level1,level2,level3,level4,level5);
    }
}
