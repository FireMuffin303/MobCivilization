package net.firemuffin303.civilizedmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.firemuffin303.civilizedmobs.registry.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.concurrent.CompletableFuture;

public class ModTagDataGen {
    public static class StructureTagDataProvider extends FabricTagProvider<Structure>{

        public StructureTagDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.STRUCTURE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            this.getOrCreateTagBuilder(ModTags.ON_NETHER_FORTRESS_EXPLORER_MAP).add(StructureKeys.FORTRESS);
            this.getOrCreateTagBuilder(ModTags.ON_BASTION_EXPLORER_MAP).add(StructureKeys.BASTION_REMNANT);
        }
    }

    public static class EntityTypeTagDataProvider extends FabricTagProvider.EntityTypeTagProvider{

        public EntityTypeTagDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            this.getOrCreateTagBuilder(ModTags.PIGLIN_NEMESIS)
                    .add(EntityType.WITHER)
                    .add(EntityType.WITHER_SKELETON);

            this.getOrCreateTagBuilder(ModTags.PIGLIN_SCARED_ZOMBIFIED)
                    .add(EntityType.ZOMBIFIED_PIGLIN)
                    .add(EntityType.ZOGLIN);
        }
    }

    public static class BiomeTagDataProvider extends FabricTagProvider<Biome> {
        public BiomeTagDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.BIOME, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            this.getOrCreateTagBuilder(ModDataGen.PIGLIN_VILLAGE_HAS_STRUCTURE).add(BiomeKeys.CRIMSON_FOREST);
        }
    }

    public static class AcquirablePOIDataGen extends FabricTagProvider<PointOfInterestType> {
        public AcquirablePOIDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.POINT_OF_INTEREST_TYPE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            this.getOrCreateTagBuilder(ModTags.PIGLIN_ACQUIRABLE_JOB_SITE).add(
                    PointOfInterestTypes.ARMORER,
                    PointOfInterestTypes.BUTCHER,
                    PointOfInterestTypes.CLERIC,
                    PointOfInterestTypes.CARTOGRAPHER,
                    PointOfInterestTypes.LEATHERWORKER,
                    PointOfInterestTypes.LIBRARIAN,
                    PointOfInterestTypes.WEAPONSMITH,
                    PointOfInterestTypes.TOOLSMITH,
                    PointOfInterestTypes.FLETCHER
            );
        }
    }

    public static class ProfessionTagDataGen extends FabricTagProvider<VillagerProfession> {
        public ProfessionTagDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.VILLAGER_PROFESSION, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(ModTags.PIGLIN_PROFESSION).add(
                    VillagerProfession.ARMORER,
                    VillagerProfession.BUTCHER,
                    VillagerProfession.CLERIC,
                    VillagerProfession.CARTOGRAPHER,
                    VillagerProfession.LEATHERWORKER,
                    VillagerProfession.LIBRARIAN,
                    VillagerProfession.WEAPONSMITH,
                    VillagerProfession.TOOLSMITH,
                    VillagerProfession.FLETCHER
            );

        }
    }
}
