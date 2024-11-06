package net.firemuffin303.civilizedmobs.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structures;

import java.util.Map;

import static net.firemuffin303.civilizedmobs.datagen.StructureData.PIGLIN_TOWN_CENTER;

public class ModDataGen implements DataGeneratorEntrypoint {
    public static final TagKey<Biome> PIGLIN_VILLAGE_HAS_STRUCTURE = TagKey.of(RegistryKeys.BIOME,new Identifier(CivilizedMobs.MOD_ID,"piglin_village_has_village"));
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelDataGen::new);
        pack.addProvider(DynamicDataGen::new);
        pack.addProvider(LangDataGen::new);
        pack.addProvider(ModTagDataGen.ProfessionTagDataGen::new);
        pack.addProvider(ModTagDataGen.AcquirablePOIDataGen::new);
        pack.addProvider(ModTagDataGen.BiomeTagDataProvider::new);
        pack.addProvider(LoottableDataProvider.ChestDataProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.TEMPLATE_POOL,StructureData::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE,StructureData::structureBootstrap);
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE_SET,StructureData::structureSetBootstrap);

    }
}
