package net.firemuffin303.civilizedmobs.datagen.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BiomeTags;
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
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VillagePlacedFeatures;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;

public class PillagerStructureData {
    public static final RegistryKey<Structure> PILLAGE_VILLAGE = RegistryKey.of(RegistryKeys.STRUCTURE,new Identifier(CivilizedMobs.MOD_ID,"pillage_village"));

    public static final RegistryKey<StructurePool> PILLAGE_VILLAGE_BASE_PLATE = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/pillager_outpost/base_plate"));
    public static final RegistryKey<StructurePool> PILLAGE_VILLAGE_TOWER = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/pillager_outpost/bell_towers"));
    public static final RegistryKey<StructurePool> PILLAGE_VILLAGE_HOUSES = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/pillager_outpost/houses"));
    public static final RegistryKey<StructurePool> PILLAGE_VILLAGE_INDOOR_DECOR = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/pillager_outpost/indoor_decor"));
    public static final RegistryKey<StructurePool> PILLAGE_VILLAGE_DECOR = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/pillager_outpost/decor"));
    public static final RegistryKey<StructurePool> PILLAGE_VILLAGE_STREETS = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"village/pillager_outpost/streets"));

    public static void structureBootstrap(Registerable<Structure> registerable){
        registerable.register(PILLAGE_VILLAGE,new JigsawStructure(
                new Structure.Config(
                        registerable.getRegistryLookup(RegistryKeys.BIOME).getOrThrow(BiomeTags.PILLAGER_OUTPOST_HAS_STRUCTURE),
                        Map.of(),
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        StructureTerrainAdaptation.BEARD_THIN
                ),
                registerable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL).getOrThrow(PILLAGE_VILLAGE_BASE_PLATE),
                6,
                ConstantHeightProvider.create(YOffset.fixed(0)),
                true,
                Heightmap.Type.WORLD_SURFACE_WG
                )
        );
    }

    public static void templatePoolBootstrap(Registerable<StructurePool> registerable){
        RegistryEntryLookup<StructureProcessorList> processorLookup = registerable.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
        RegistryEntryLookup<StructurePool> structurePoolLookup = registerable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntryLookup<PlacedFeature> placedFeatureLookup = registerable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);

        registerable.register(PILLAGE_VILLAGE_BASE_PLATE,new StructurePool(
                structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/base_plate",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),
                StructurePool.Projection.RIGID
        ));

        registerable.register(PILLAGE_VILLAGE_HOUSES,new StructurePool(
                structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/houses/flecther",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/houses/armorer",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/houses/general_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),StructurePool.Projection.RIGID));

        registerable.register(PILLAGE_VILLAGE_INDOOR_DECOR,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/indoor_decor/decorated_pot_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/indoor_decor/decorated_pot_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/indoor_decor/flower_pot_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/indoor_decor/flower_pot_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),StructurePool.Projection.RIGID));

        registerable.register(PILLAGE_VILLAGE_DECOR,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/decor/lamp_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),3),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/decor/wall_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),3),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/decor/tall_wall_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),3),
                        Pair.of(StructurePoolElement.ofFeature(placedFeatureLookup.getOrThrow(VillagePlacedFeatures.PILE_HAY)),2),
                        Pair.of(StructurePoolElement.ofFeature(placedFeatureLookup.getOrThrow(VillagePlacedFeatures.PILE_PUMPKIN)),2)
                ), StructurePool.Projection.RIGID));

        registerable.register(PILLAGE_VILLAGE_STREETS,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/streets/corner_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/streets/corner_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/streets/crossroad_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/streets/straight_03",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),StructurePool.Projection.TERRAIN_MATCHING));

        registerable.register(PILLAGE_VILLAGE_TOWER,new StructurePool(structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/town_centers/central_tower_01",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("village/pillager_outpost/town_centers/central_tower_02",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),StructurePool.Projection.TERRAIN_MATCHING));
    }

    public static void dataGen(RegistryWrapper.WrapperLookup wrapperLookup, FabricDynamicRegistryProvider.Entries entries){
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.STRUCTURE),PILLAGE_VILLAGE);

        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PILLAGE_VILLAGE_BASE_PLATE);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PILLAGE_VILLAGE_TOWER);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PILLAGE_VILLAGE_STREETS);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PILLAGE_VILLAGE_DECOR);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PILLAGE_VILLAGE_HOUSES);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),PILLAGE_VILLAGE_INDOOR_DECOR);
    }

}