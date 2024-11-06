package net.firemuffin303.civilizedmobs.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
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
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VillagePlacedFeatures;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;
import java.util.function.Function;

public class StructureData {
    //-- Piglin  Village Structure--
    public static final RegistryKey<Structure> PIGLIN_VILLAGE = RegistryKey.of(RegistryKeys.STRUCTURE,new Identifier(CivilizedMobs.MOD_ID,"piglin_village"));
    public static final RegistryKey<StructureSet> NETHER_VILLAGE = RegistryKey.of(RegistryKeys.STRUCTURE_SET,new Identifier(CivilizedMobs.MOD_ID,"nether_village"));

        //--Template--
    public static final RegistryKey<StructurePool> PIGLIN_TOWN_CENTER = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/town_center"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_HOUSES = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/houses"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_DECOR = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/decor"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_STREETS = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/streets"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_TERMINATOR = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/terminators"));
    public static final RegistryKey<StructurePool> PIGLIN_VILLAGE_UNEMPLOYED = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/piglin/entity/unemployed"));
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
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/houses/tannery_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2)
                ), StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_DECOR,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructurePoolElement.ofFeature(placedFeatureLookup.getOrThrow(VillagePlacedFeatures.PILE_PUMPKIN)),5),
                        Pair.of(StructurePoolElement.ofFeature(placedFeatureLookup.getOrThrow(VillagePlacedFeatures.PILE_HAY)),1)
                ), StructurePool.Projection.RIGID));

        registerable.register(PIGLIN_VILLAGE_STREETS,new StructurePool(structurePoolLookup.getOrThrow(PIGLIN_VILLAGE_TERMINATOR),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/corner_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/corner_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/piglin/streets/corner_03",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),2)
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
