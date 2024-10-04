package net.firemuffin303.civilizedmobs.registry;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModTags {

    public static TagKey<VillagerProfession> PIGLIN_PROFESSION = register(RegistryKeys.VILLAGER_PROFESSION,"piglin_profession");
    public static TagKey<VillagerProfession> WITHER_PROFESSION = register(RegistryKeys.VILLAGER_PROFESSION,"wither_profession");
    public static TagKey<VillagerProfession> ILLAGER_PROFESSION = register(RegistryKeys.VILLAGER_PROFESSION,"illager_profession");

    public static TagKey<PointOfInterestType> PIGLIN_ACQUIRABLE_JOB_SITE = register(RegistryKeys.POINT_OF_INTEREST_TYPE,"piglin_acquirable_job_site");

    public static <T> TagKey<T> register(RegistryKey<Registry<T>> registryKey,String id){
        return TagKey.of(registryKey,new Identifier(CivilizedMobs.MOD_ID,id));
    }
}
