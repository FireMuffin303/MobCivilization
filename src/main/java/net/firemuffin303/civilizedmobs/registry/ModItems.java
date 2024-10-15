package net.firemuffin303.civilizedmobs.registry;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static List<Item> ITEMS = new ArrayList<>();
    public static Item CIVIL_PIGLIN_SPAWN_EGG = register("civil_piglin_spawn_egg",new SpawnEggItem(ModEntityType.CIVIL_PIGLIN,6156165,65156,new Item.Settings()));
    public static Item QUEST_SPAWN_EGG = register("quest_spawn_egg",new SpawnEggItem(ModEntityType.QUEST_ENTITY,68458648,65156,new Item.Settings()));

    public static Item register(String id,Item item){
        ITEMS.add(item);
        return Registry.register(Registries.ITEM, Identifier.of(CivilizedMobs.MOD_ID,id),item);
    }

    public static void init() {

    }
}
