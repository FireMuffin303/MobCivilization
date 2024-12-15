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
    public static Item PIGLIN_WORKER_SPAWN_EGG = register("piglin_worker_spawn_egg",true,new SpawnEggItem(ModEntityType.PIGLIN_WORKER,10051392,0xec9035,new Item.Settings()));
    public static Item PIGLIN_LEADER_SPAWN_EGG = register("piglin_leader_spawn_egg",true,new SpawnEggItem(ModEntityType.PIGLIN_LEADER_ENTITY,10051392,0xfff793,new Item.Settings()));

    public static Item PILLAGER_WORKER_SPAWN_EGG = register("pillager_worker_spawn_egg",true,new SpawnEggItem(ModEntityType.PILLAGER_WORKER,0x532F36,0xec9035,new Item.Settings()));
    public static Item PILLAGER_LEADER_SPAWN_EGG = register("pillager_leader_spawn_egg",true,new SpawnEggItem(ModEntityType.PILLAGER_LEADER,0x532F36,0xfff793,new Item.Settings()));

    public static Item WITHTER_SKELETON_WORKER_SPAWN_EGG = register("wither_skeleton_worker_spawn_egg",true,new SpawnEggItem(ModEntityType.WITHER_SKELETON_WORKER,0x141414,0xec9035,new Item.Settings()));
    public static Item WITHTER_SKELETON_LEADER_SPAWN_EGG = register("wither_skeleton_leader_spawn_egg",true,new SpawnEggItem(ModEntityType.WITHER_SKELETON_QUEST,0x141414,0xfff793,new Item.Settings()));


    public static Item register(String id,boolean addToGroup,Item item){
        if(addToGroup){
            ITEMS.add(item);
        }
        return Registry.register(Registries.ITEM, Identifier.of(CivilizedMobs.MOD_ID,id),item);
    }

    public static void init() {

    }
}
