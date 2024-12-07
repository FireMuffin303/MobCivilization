package net.firemuffin303.civilizedmobs;

import com.mojang.logging.LogUtils;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.firemuffin303.civilizedmobs.common.entity.piglin.quest.PiglinQuestEntity;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.common.entity.pillager.PillagerWorkerEntity;
import net.firemuffin303.civilizedmobs.common.event.ModServerEntityEvents;
import net.firemuffin303.civilizedmobs.datagen.structure.PillagerStructureData;
import net.firemuffin303.civilizedmobs.mixin.structures.StructureSetAccessor;
import net.firemuffin303.civilizedmobs.registry.ModBrains;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.firemuffin303.civilizedmobs.registry.ModItems;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.structure.StructureSets;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.Structure;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CivilizedMobs implements ModInitializer {

    public static String MOD_ID = "civil_mobs";
    public static Logger LOGGER = LogUtils.getLogger();

    public static boolean isTrinketsInstall = false;

    public static final GameRules.Key<GameRules.IntRule> QUEST_RESTOCK_TIME = GameRuleRegistry.register("civil_mobs-quest_restock_time", GameRules.Category.MOBS, GameRuleFactory.createIntRule(1800));

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.of(MOD_ID+".item_group"))
            .entries((displayContext, entries) -> {
                ModItems.ITEMS.forEach(entries::add);
            })
            .icon(() -> new ItemStack(Items.GOLD_INGOT)).build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID,"main"),ITEM_GROUP);

        ModBrains.init();
        ModEntityType.init();
        ModItems.init();

        TrackedDataHandlerRegistry.register(ModEntityType.WORKER_DATA);
        FabricDefaultAttributeRegistry.register(ModEntityType.PIGLIN_WORKER, WorkerPiglinEntity.createAttribute());
        FabricDefaultAttributeRegistry.register(ModEntityType.PIGLIN_LEADER_ENTITY, PiglinQuestEntity.createAttribute());
        FabricDefaultAttributeRegistry.register(ModEntityType.PILLAGER_WORKER, PillagerWorkerEntity.createPillagerAttributes());

        ServerEntityEvents.ENTITY_LOAD.register(ModServerEntityEvents::IllagerLoaded);
        ServerLifecycleEvents.SERVER_STARTING.register(CivilizedMobs::onServerStarting);

        if(FabricLoader.getInstance().isModLoaded("trinkets")){
            isTrinketsInstall = true;

        }
    }

    private static void onServerStarting(MinecraftServer minecraftServer) {
        RegistryWrapper<StructureSet> structureSetWrapper = minecraftServer.getRegistryManager().getWrapperOrThrow(RegistryKeys.STRUCTURE_SET);
        RegistryWrapper<Structure> structureWrapper = minecraftServer.getRegistryManager().getWrapperOrThrow(RegistryKeys.STRUCTURE);

        RegistryEntry<StructureSet> pillagerOutpost = structureSetWrapper.getOrThrow(StructureSetKeys.PILLAGER_OUTPOSTS);

        if (pillagerOutpost != null) {
            RegistryEntry<Structure> pillageVillage = structureWrapper.getOrThrow(PillagerStructureData.PILLAGE_VILLAGE);
            StructureSet structureSet = pillagerOutpost.value();
            List<StructureSet.WeightedEntry> weightedEntries = new ArrayList<>(structureSet.structures());
            weightedEntries.add(StructureSet.createEntry(pillageVillage));
            ((StructureSetAccessor)(Object) structureSet).setStructures(weightedEntries);
        }
    }
}
