package net.firemuffin303.civilizedmobs.datagen;

import com.eliotlash.mclib.math.functions.classic.Mod;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.integration.muffinsQuest.ModQuests;
import net.firemuffin303.civilizedmobs.datagen.integration.QuestDataGen;
import net.firemuffin303.muffinsquestlib.common.registry.ModRegistries;
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
        pack.addProvider(ModTagDataGen.EntityTypeTagDataProvider::new);
        pack.addProvider(ModTagDataGen.StructureTagDataProvider::new);

        pack.addProvider(LoottableDataProvider.ChestDataProvider::new);
        //Quest Lib
        pack.addProvider(QuestDataGen.QuestDynamicProvider::new);
        pack.addProvider(QuestDataGen.QuestTagProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.TEMPLATE_POOL,StructureData::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE,StructureData::structureBootstrap);
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE_SET,StructureData::structureSetBootstrap);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE,StructureData::configuredFeatureBootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE,StructureData::placedFeatureBootstrap);

        //Quest Lib
        registryBuilder.addRegistry(ModRegistries.QUEST_KEY, ModQuests::bootstrap);


    }
}
