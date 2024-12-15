package net.firemuffin303.civilizedmobs.datagen.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.registry.*;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;

public class FortressStructureData {
    public static final RegistryKey<StructurePool> FORTRESS_WORKER = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"fortress/worker"));
    public static final RegistryKey<StructurePool> FORTRESS_LEADER_WORKER = RegistryKey.of(RegistryKeys.TEMPLATE_POOL,new Identifier(CivilizedMobs.MOD_ID,"fortress/leader_worker"));

    public static void templatePoolBootstrap(Registerable<StructurePool> registerable){
        RegistryEntryLookup<StructureProcessorList> processorLookup = registerable.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
        RegistryEntryLookup<StructurePool> structurePoolLookup = registerable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        registerable.register(FORTRESS_WORKER,new StructurePool(
                structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("fortress/worker",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),
                StructurePool.Projection.RIGID
        ));

        registerable.register(FORTRESS_LEADER_WORKER,new StructurePool(
                structurePoolLookup.getOrThrow(StructurePools.EMPTY),
                ImmutableList.of(
                        Pair.of(StructureData.ofProcessedLegacySingle("fortress/leader",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1),
                        Pair.of(StructureData.ofProcessedLegacySingle("fortress/worker",processorLookup.getOrThrow(StructureProcessorLists.EMPTY)),1)
                ),
                StructurePool.Projection.RIGID
        ));
    }

    public static void dataGen(RegistryWrapper.WrapperLookup wrapperLookup, FabricDynamicRegistryProvider.Entries entries){
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),FORTRESS_WORKER);
        entries.add(wrapperLookup.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL),FORTRESS_LEADER_WORKER);
    }

}
