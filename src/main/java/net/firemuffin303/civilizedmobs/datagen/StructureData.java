package net.firemuffin303.civilizedmobs.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.block.Blocks;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.pool.LegacySinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StructureData {
    //-- Piglin  Village Structure--
    public static final RegistryKey<Structure> PIGLIN_VILLAGE = RegistryKey.of(RegistryKeys.STRUCTURE,new Identifier(CivilizedMobs.MOD_ID,"piglin_village"));
    public static final RegistryKey<StructureSet> NETHER_VILLAGE = RegistryKey.of(RegistryKeys.STRUCTURE_SET,new Identifier(CivilizedMobs.MOD_ID,"nether_village"));

    //--- Configured Feature
    public static final RegistryKey<ConfiguredFeature<?,?>> PILE_NETHER_WART_BLOCK_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE,new Identifier(CivilizedMobs.MOD_ID,"pile_nether_wart_block"));
    public static final RegistryKey<ConfiguredFeature<?,?>> PILE_WARPED_WART_BLOCK_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE,new Identifier(CivilizedMobs.MOD_ID,"pile_warped_wart_block"));
    public static final RegistryKey<ConfiguredFeature<?,?>> PILE_SOUL_SAND_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE,new Identifier(CivilizedMobs.MOD_ID,"pile_soul_sand_block"));
    public static final RegistryKey<ConfiguredFeature<?,?>> PILE_SOUL_SOIL_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE,new Identifier(CivilizedMobs.MOD_ID,"pile_soul_soil_block"));

    //--- Placed Feature ---
    public static final RegistryKey<PlacedFeature> PILE_NETHER_WART_BLOCK_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE,new Identifier(CivilizedMobs.MOD_ID,"pile_nether_wart_block"));
    public static final RegistryKey<PlacedFeature> PILE_WARPED_WART_BLOCK_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE,new Identifier(CivilizedMobs.MOD_ID,"pile_warped_wart_block"));
    public static final RegistryKey<PlacedFeature> PILE_SOUL_SAND_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE,new Identifier(CivilizedMobs.MOD_ID,"pile_soul_sand_block"));
    public static final RegistryKey<PlacedFeature> PILE_SOUL_SOIL_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE,new Identifier(CivilizedMobs.MOD_ID,"pile_soul_soil_block"));

    //--Template--
    public static final RegistryKey<StructurePool> PIGLIN_TOWN_CENTER = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/town_center"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_HOUSES = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/houses"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_DECOR = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/decor"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_STREETS = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/streets"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_TERMINATOR = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/terminators"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_UNEMPLOYED = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/entity/unemployed"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_LEADER = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/entity/leader"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_ANIMAL = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/entity/animals"));

    public static void structureBootstrap(Registerable<Structure> registerable){
        registerable.register(PIGLIN_VILLAGE,new JigsawStructure(
                new Structure.Config(
                        registerable.getRegistryLookup(RegistryKeys.BIOME).getOrThrow(ModDataGen.PIGLIN_VILLAGE_HAS_STRUCTURE),
                        Map.of(),
                        GenerationStep.Feature.UNDERGROUND_STRUCTURES,
                        StructureTerrainAdaptation.BEARD_THIN
                ),
                registerable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL).getOrThrow(PIGLIN_TOWN_CENTER),
                6,
                ConstantHeightProvider.create(YOffset.fixed(50)),
                true
        ));
    }

    public static void configuredFeatureBootstrap(Registerable<ConfiguredFeature<?,?>> registerable){
        registerable.register(PILE_NETHER_WART_BLOCK_CONFIGURED,new ConfiguredFeature<>(Feature.BLOCK_PILE,new BlockPileFeatureConfig(BlockStateProvider.of(Blocks.NETHER_WART_BLOCK))));
        registerable.register(PILE_WARPED_WART_BLOCK_CONFIGURED,new ConfiguredFeature<>(Feature.BLOCK_PILE,new BlockPileFeatureConfig(BlockStateProvider.of(Blocks.WARPED_WART_BLOCK))));
        registerable.register(PILE_SOUL_SAND_CONFIGURED,new ConfiguredFeature<>(Feature.BLOCK_PILE,new BlockPileFeatureConfig(BlockStateProvider.of(Blocks.SOUL_SAND))));
        registerable.register(PILE_SOUL_SOIL_CONFIGURED,new ConfiguredFeature<>(Feature.BLOCK_PILE,new BlockPileFeatureConfig(BlockStateProvider.of(Blocks.SOUL_SOIL))));
    }

    public static void placedFeatureBootstrap(Registerable<PlacedFeature> registerable){
        RegistryEntryLookup<ConfiguredFeature<?,?>> registryEntryLookup = registerable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        registerable.register(PILE_NETHER_WART_BLOCK_PLACED,new PlacedFeature(registryEntryLookup.getOrThrow(PILE_NETHER_WART_BLOCK_CONFIGURED), List.of()));
        registerable.register(PILE_WARPED_WART_BLOCK_PLACED,new PlacedFeature(registryEntryLookup.getOrThrow(PILE_WARPED_WART_BLOCK_CONFIGURED), List.of()));
        registerable.register(PILE_SOUL_SAND_PLACED,new PlacedFeature(registryEntryLookup.getOrThrow(PILE_SOUL_SAND_CONFIGURED), List.of()));
        registerable.register(PILE_SOUL_SOIL_PLACED,new PlacedFeature(registryEntryLookup.getOrThrow(PILE_SOUL_SOIL_CONFIGURED), List.of()));
    }

    public static void structureSetBootstrap(Registerable<StructureSet> registerable){
        RegistryEntryLookup<Structure> structureLookup = registerable.getRegistryLookup(RegistryKeys.STRUCTURE);
        registerable.register(NETHER_VILLAGE,new StructureSet(structureLookup.getOrThrow(StructureData.PIGLIN_VILLAGE),new RandomSpreadStructurePlacement(34,8, SpreadType.LINEAR,10387312)));
    }

    public static void bootstrap(Registerable<StructurePool> registerable){
        RegistryEntryLookup<StructureProcessorList> processorLookup = registerable.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
        RegistryEntryLookup<StructurePool> structurePoolLookup = registerable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntryLookup<PlacedFeature> placedFeatureLookup = registerable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);


        registerable.register(PIGLIN_TOWN_CENTER,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/town_centers/camp",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),50),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/town_centers/fountain_1",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),50),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/town_centers/gold_pile",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),5),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/town_centers/meeting_point",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),50),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/town_centers/tree",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),50)
                ), StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_HOUSES,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/armorer_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/butcher_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/cartographer_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/cleric_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/cleric_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/fletcher_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/library_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/library_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/tannery_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/weaponsmith_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/tent_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2)
                        ), StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_DECOR,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/decor/lamp_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),5),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/decor/lamp_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),5),
                        Pair.of(StructurePoolElement.ofFeature(placedFeatureLookup.getOrThrow(StructureData.PILE_NETHER_WART_BLOCK_PLACED)),3),
                        Pair.of(StructurePoolElement.ofFeature(placedFeatureLookup.getOrThrow(StructureData.PILE_SOUL_SOIL_PLACED)),3),
                        Pair.of(StructurePoolElement.ofFeature(placedFeatureLookup.getOrThrow(StructureData.PILE_SOUL_SAND_PLACED)),2),
                        Pair.of(StructurePoolElement.ofFeature(placedFeatureLookup.getOrThrow(StructureData.PILE_WARPED_WART_BLOCK_PLACED)),1)
                ), StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_STREETS,new StructurePool(structurePoolLookup.getOrThrow(PIGLIN_VILLAGE_TERMINATOR),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/corner_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/corner_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/corner_03",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/crossroad_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/crossroad_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/crossroad_03",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/crossroad_04",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/crossroad_05",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/straight_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/straight_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/straight_03",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/straight_04",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/straight_05",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/straight_06",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2)
                ), StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_TERMINATOR,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/terminators/terminator_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/terminators/terminator_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/terminators/terminator_03",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ), StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_UNEMPLOYED,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/entity/unemployed",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_LEADER,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/entity/leader",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ), StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_ANIMAL,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/entity/hoglin",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/entity/strider",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/entity/hoglin_baby",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),StructurePool.Projection.RIGID));
    }

    public static void dataGen(RegistryWrapper.WrapperLookup wrapperLookup, FabricDynamicRegistryProvider.Entries entries){
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.STRUCTURE),PIGLIN_VILLAGE);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.STRUCTURE_SET),NETHER_VILLAGE);

        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE),PILE_NETHER_WART_BLOCK_CONFIGURED);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE),PILE_WARPED_WART_BLOCK_CONFIGURED);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE),PILE_SOUL_SAND_CONFIGURED);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE),PILE_SOUL_SOIL_CONFIGURED);

        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE),PILE_NETHER_WART_BLOCK_PLACED);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE),PILE_WARPED_WART_BLOCK_PLACED);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE),PILE_SOUL_SAND_PLACED);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE),PILE_SOUL_SOIL_PLACED);

        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PIGLIN_TOWN_CENTER);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PIGLIN_VILLAGE_HOUSES);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PIGLIN_VILLAGE_STREETS);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PIGLIN_VILLAGE_TERMINATOR);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PIGLIN_VILLAGE_DECOR);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PIGLIN_VILLAGE_UNEMPLOYED);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PIGLIN_VILLAGE_ANIMAL);
    }

    public static Function<StructurePool.Projection, LegacySinglePoolElement> ofProcessedLegacySingle(String id, RegistryEntry<StructureProcessorList> processorListEntry) {
        return (projection) -> new ModLegacySinglePoolElement(Either.left(new Identifier(CivilizedMobs.MOD_ID,id)), processorListEntry, projection);
    }


    private static class ModLegacySinglePoolElement extends LegacySinglePoolElement {
        public ModLegacySinglePoolElement(Either<Identifier, StructureTemplate> either, RegistryEntry<StructureProcessorList> registryEntry, StructurePool.Projection projection) {
            super(either, registryEntry, projection);
        }
    }
}
