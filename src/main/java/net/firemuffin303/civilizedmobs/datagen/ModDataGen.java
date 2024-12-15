package net.firemuffin303.civilizedmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.integration.muffinsQuest.ModQuests;
import net.firemuffin303.civilizedmobs.datagen.integration.QuestDataGen;
import net.firemuffin303.civilizedmobs.datagen.structure.FortressStructureData;
import net.firemuffin303.civilizedmobs.datagen.structure.PillagerStructureData;
import net.firemuffin303.civilizedmobs.datagen.structure.StructureData;
import net.firemuffin303.muffinsquestlib.common.registry.ModRegistries;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModDataGen implements DataGeneratorEntrypoint {
    public static final TagKey<Biome> PIGLIN_VILLAGE_HAS_STRUCTURE = TagKey.of(RegistryKeys.BIOME,new Identifier(CivilizedMobs.MOD_ID,"piglin_village_has_village"));
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelDataGen::new);
        pack.addProvider(DynamicDataGen::new);
        pack.addProvider(LangDataGen::new);
        pack.addProvider(LangDataGen.ThaiLangDataGen::new);

        //Tag
        pack.addProvider(ModTagDataGen.ProfessionTagDataGen::new);
        pack.addProvider(ModTagDataGen.AcquirablePOIDataGen::new);
        pack.addProvider(ModTagDataGen.BiomeTagDataProvider::new);
        pack.addProvider(ModTagDataGen.EntityTypeTagDataProvider::new);
        pack.addProvider(ModTagDataGen.StructureTagDataProvider::new);
        pack.addProvider(ModTagDataGen.ModItemTagDataProvider::new);

        pack.addProvider(LoottableDataProvider.ChestDataProvider::new);
        pack.addProvider(LoottableDataProvider.EntityLootDataProvider::new);
        //Quest Lib
        pack.addProvider(QuestDataGen.QuestDynamicProvider::new);
        pack.addProvider(QuestDataGen.QuestTagProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.TEMPLATE_POOL,registerable -> {
            StructureData.bootstrap(registerable);
            PillagerStructureData.templatePoolBootstrap(registerable);
            FortressStructureData.templatePoolBootstrap(registerable);
        } );
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE,registerable -> {
            StructureData.structureBootstrap(registerable);
            PillagerStructureData.structureBootstrap(registerable);
        } );
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE_SET,StructureData::structureSetBootstrap);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE,StructureData::configuredFeatureBootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE,StructureData::placedFeatureBootstrap);

        //Quest Lib
        registryBuilder.addRegistry(ModRegistries.QUEST_KEY, ModQuests::bootstrap);


    }
}
