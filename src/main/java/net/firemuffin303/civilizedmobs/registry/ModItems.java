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
    public static Item PIGLIN_WORKER_SPAWN_EGG = register("piglin_worker_spawn_egg",true,new SpawnEggItem(ModEntityType.PIGLIN_WORKER,10051392,0xddd39f,new Item.Settings()));
    public static Item PIGLIN_LEADER_SPAWN_EGG = register("piglin_leader_spawn_egg",true,new SpawnEggItem(ModEntityType.PIGLIN_LEADER_ENTITY,10051392,0xfdf79b,new Item.Settings()));
    public static Item PILLAGER_WORKER_LEADER_SPAWN_EGG = register("pillager_worker_spawn_egg",true,new SpawnEggItem(ModEntityType.PILLAGER_WORKER,10051392,0xfdf79b,new Item.Settings()));


    public static Item register(String id,boolean addToGroup,Item item){
        if(addToGroup){
            ITEMS.add(item);
        }
        return Registry.register(Registries.ITEM, Identifier.of(CivilizedMobs.MOD_ID,id),item);
    }

    public static void init() {

    }
}
