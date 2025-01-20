package net.firemuffin303.villagefoe.datagen;

import dev.emi.trinkets.TrinketsMain;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.firemuffin303.villagefoe.registry.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.concurrent.CompletableFuture;

public class ModTagDataGen {
    public static class ModItemTagDataProvider extends FabricTagProvider.ItemTagProvider {
        public static final TagKey<Item> TRINKET_HAT = TagKey.of(RegistryKeys.ITEM,new Identifier(TrinketsMain.MOD_ID,"head/hat"));

        public ModItemTagDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture, null);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            this.getOrCreateTagBuilder(TRINKET_HAT).forceAddTag(ItemTags.BANNERS);
        }
    }


    public static class StructureTagDataProvider extends FabricTagProvider<Structure>{

        public StructureTagDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.STRUCTURE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            this.getOrCreateTagBuilder(ModTags.ON_NETHER_FORTRESS_EXPLORER_MAP).add(StructureKeys.FORTRESS);
            this.getOrCreateTagBuilder(ModTags.ON_BASTION_EXPLORER_MAP).add(StructureKeys.BASTION_REMNANT);
            this.getOrCreateTagBuilder(ModTags.ON_ANCIENT_CITY_EXPLORER_MAP).add(StructureKeys.ANCIENT_CITY);
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
                    .add(EntityType.WITHER_SKELETON)
                    .add(ModEntityType.WITHER_SKELETON_WORKER)
                    .add(ModEntityType.WITHER_SKELETON_LEADER);

            this.getOrCreateTagBuilder(ModTags.PIGLIN_SCARED_ZOMBIFIED)
                    .add(EntityType.ZOMBIFIED_PIGLIN)
                    .add(EntityType.ZOGLIN);

            this.getOrCreateTagBuilder(ModTags.ILLAGER_NEMESIS)
                    .add(EntityType.VILLAGER)
                    .add(EntityType.IRON_GOLEM);

            this.getOrCreateTagBuilder(ModTags.WITHER_SKELTON_NEMESIS)
                    .add(EntityType.PIGLIN)
                    .add(EntityType.PIGLIN_BRUTE)
                    .add(ModEntityType.PIGLIN_WORKER)
                    .add(ModEntityType.PIGLIN_LEADER_ENTITY)
                    .add(EntityType.IRON_GOLEM)
                    .add(EntityType.TURTLE);

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

            this.getOrCreateTagBuilder(ModTags.ILLAGER_ACQUIRABLE_JOB_SITE).add(
                    PointOfInterestTypes.ARMORER,
                    PointOfInterestTypes.BUTCHER,
                    PointOfInterestTypes.CARTOGRAPHER,
                    PointOfInterestTypes.LEATHERWORKER,
                    PointOfInterestTypes.LIBRARIAN,
                    PointOfInterestTypes.WEAPONSMITH,
                    PointOfInterestTypes.TOOLSMITH,
                    PointOfInterestTypes.SHEPHERD
            );

            this.getOrCreateTagBuilder(ModTags.WITHER_SKELETON_ACQUIRABLE_JOB_SITE).add(
                    PointOfInterestTypes.ARMORER,
                    PointOfInterestTypes.BUTCHER,
                    PointOfInterestTypes.WEAPONSMITH,
                    PointOfInterestTypes.TOOLSMITH,
                    PointOfInterestTypes.CLERIC,
                    PointOfInterestTypes.MASON
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

            getOrCreateTagBuilder(ModTags.ILLAGER_PROFESSION).add(
                    VillagerProfession.ARMORER,
                    VillagerProfession.BUTCHER,
                    VillagerProfession.CARTOGRAPHER,
                    VillagerProfession.LEATHERWORKER,
                    VillagerProfession.LIBRARIAN,
                    VillagerProfession.WEAPONSMITH,
                    VillagerProfession.TOOLSMITH,
                    VillagerProfession.SHEPHERD
            );

            getOrCreateTagBuilder(ModTags.WITHER_PROFESSION).add(
                    VillagerProfession.ARMORER,
                    VillagerProfession.BUTCHER,
                    VillagerProfession.WEAPONSMITH,
                    VillagerProfession.TOOLSMITH,
                    VillagerProfession.CLERIC,
                    VillagerProfession.MASON
            );


        }
    }
}
