package net.firemuffin303.villagefoe;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.firemuffin303.villagefoe.common.entity.piglin.quest.PiglinQuestEntity;
import net.firemuffin303.villagefoe.common.entity.piglin.worker.WorkerPiglinEntity;
import net.firemuffin303.villagefoe.common.entity.pillager.worker.PillagerWorkerEntity;
import net.firemuffin303.villagefoe.common.entity.pillager.quest.PillagerQuestEntity;
import net.firemuffin303.villagefoe.common.entity.witherSkelton.quest.WitherSkeletonQuestEntity;
import net.firemuffin303.villagefoe.common.event.ModServerEntityEvents;
import net.firemuffin303.villagefoe.datagen.structure.PillagerStructureData;
import net.firemuffin303.villagefoe.mixin.structures.*;
import net.firemuffin303.villagefoe.registry.ModBrains;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.firemuffin303.villagefoe.registry.ModItems;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.GameRules;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VillageFoe implements ModInitializer {

    public static String MOD_ID = "village_foe";
    public static Logger LOGGER = LogUtils.getLogger();

    public static boolean isTrinketsInstall = false;

    public static final GameRules.Key<GameRules.IntRule> QUEST_RESTOCK_TIME = GameRuleRegistry.register(MOD_ID+"_quest_restock_time", GameRules.Category.MOBS, GameRuleFactory.createIntRule(1800));

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemgroup."+MOD_ID))
            .entries((displayContext, entries) -> {
                ModItems.ITEMS.forEach(entries::add);
            })
            .icon(() -> new ItemStack(Items.LECTERN)).build();

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
        FabricDefaultAttributeRegistry.register(ModEntityType.PILLAGER_LEADER, PillagerQuestEntity.createAttribute());
        FabricDefaultAttributeRegistry.register(ModEntityType.WITHER_SKELETON_WORKER, WitherSkeletonEntity.createAbstractSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityType.WITHER_SKELETON_LEADER, WitherSkeletonQuestEntity.createAttribute());

        ServerEntityEvents.ENTITY_LOAD.register(ModServerEntityEvents::onEntityLoaded);
        ServerLifecycleEvents.SERVER_STARTING.register(VillageFoe::onServerStarting);

        if(FabricLoader.getInstance().isModLoaded("trinkets")){
            isTrinketsInstall = true;
        }
    }

    private static void onServerStarting(MinecraftServer minecraftServer) {
        RegistryWrapper<StructureSet> structureSetWrapper = minecraftServer.getRegistryManager().getWrapperOrThrow(RegistryKeys.STRUCTURE_SET);
        RegistryWrapper<Structure> structureWrapper = minecraftServer.getRegistryManager().getWrapperOrThrow(RegistryKeys.STRUCTURE);
        RegistryEntry<StructureSet> pillagerOutpost = structureSetWrapper.getOrThrow(StructureSetKeys.PILLAGER_OUTPOSTS);
        RegistryEntry<Structure> fortressStructure = structureWrapper.getOrThrow(StructureKeys.FORTRESS);

        if(FabricLoader.getInstance().isModLoaded("betterfortresses")){
            fortressStructure = structureWrapper.getOrThrow(RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier("betterfortresses","fortress")));
            VillageFoe.onYungBetterFortressLoaded(minecraftServer);
        }

        if (pillagerOutpost != null) {
            RegistryEntry<Structure> pillageVillage = structureWrapper.getOrThrow(PillagerStructureData.PILLAGE_VILLAGE);
            StructureSet structureSet = pillagerOutpost.value();
            List<StructureSet.WeightedEntry> weightedEntries = new ArrayList<>(structureSet.structures());
            weightedEntries.add(StructureSet.createEntry(pillageVillage));
            ((StructureSetAccessor)(Object) structureSet).setStructures(weightedEntries);
        }

        if(fortressStructure != null){
            Structure fortress = fortressStructure.value();
            Structure.Config fortressConfig = ((StructureAccessor)fortress).getConfig();

            Map<SpawnGroup, StructureSpawns> spawnMap = new HashMap<>(fortressConfig.spawnOverrides());

            Pool<SpawnSettings.SpawnEntry> pool = spawnMap.get(SpawnGroup.MONSTER).spawns();

            List<SpawnSettings.SpawnEntry> entries = new ArrayList<>(pool.getEntries());
            entries.add(new SpawnSettings.SpawnEntry(ModEntityType.WITHER_SKELETON_WORKER,1,1,3));

            ((StructureSpawnsAccessor)(Object)spawnMap.get(SpawnGroup.MONSTER)).setSpawns(Pool.of(entries));
            ((StructureConfigAccessor)(Object)fortressConfig).setSpawnOverrides(spawnMap);
            ((StructureAccessor) fortress).setConfig(fortressConfig);

        }
    }

    private static void onYungBetterFortressLoaded(MinecraftServer minecraftServer){
        RegistryWrapper<StructurePool> structurePoolRegistryWrapper = minecraftServer.getRegistryManager().getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL);
        //RegistryWrapper<StructureProcessorList> processorListRegistryWrapper = minecraftServer.getRegistryManager().getWrapperOrThrow(RegistryKeys.PROCESSOR_LIST);

        RegistryEntry<StructurePool> hallPool = structurePoolRegistryWrapper.getOrThrow(RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier("betterfortresses","halls")));
        RegistryEntry<StructurePool> injectedHallPool = structurePoolRegistryWrapper.getOrThrow(RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(VillageFoe.MOD_ID,"inject/betterfortresses/halls")));

        if(injectedHallPool == null || hallPool == null){
            return;
        }

        StructurePool injectHP =  injectedHallPool.value();
        List<Pair<StructurePoolElement,Integer>> a = new ArrayList<>(((StructurePoolAccessor)hallPool.value()).getRawTemplates());
        List<Pair<StructurePoolElement,Integer>> b = new ArrayList<>(((StructurePoolAccessor)injectHP).getRawTemplates());

        a.addAll(b);

        ((StructurePoolAccessor)hallPool.value()).setRawTemplates(a);

        for(Pair<StructurePoolElement,Integer> pair: b){
            for(int i = 0; i < pair.getSecond();i++){
                ((StructurePoolAccessor)hallPool.value()).getTemplates().add(pair.getFirst());
            }
        }
    }

}
