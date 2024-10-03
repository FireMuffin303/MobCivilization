package net.firemuffin303.civilizedmobs.common.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public record CivilziedProfession(Predicate<RegistryEntry<PointOfInterestType>> heldWorkstation,Predicate<RegistryEntry<PointOfInterestType>> acquirableWorkstation, SoundEvent workingSound) {
    public static final CivilziedProfession NONE= register("none",PointOfInterestType.NONE,SoundEvents.ENTITY_VILLAGER_WORK_CLERIC);
    public static final CivilziedProfession PIGLIN_ARMORER = register("piglin_armorer",PointOfInterestTypes.ARMORER,SoundEvents.ENTITY_VILLAGER_WORK_CLERIC);
    public static final CivilziedProfession PIGLIN_BUTCHER = register("piglin_butcher",PointOfInterestTypes.BUTCHER,SoundEvents.ENTITY_VILLAGER_WORK_CLERIC);

    public static CivilziedProfession register(String id,Predicate<RegistryEntry<PointOfInterestType>> heldWorkstation, SoundEvent soundEvent ){
        return register(id,heldWorkstation,heldWorkstation,soundEvent);
    }

    public static CivilziedProfession register(String id,RegistryKey<PointOfInterestType> heldWorkstation, SoundEvent soundEvent ){
        return register(id,registryEntry -> registryEntry.matchesKey(heldWorkstation),registryEntry -> registryEntry.matchesKey(heldWorkstation),soundEvent);
    }

    public static CivilziedProfession register(String id,Predicate<RegistryEntry<PointOfInterestType>> heldWorkstation,Predicate<RegistryEntry<PointOfInterestType>> acquirableWorkstation, SoundEvent soundEvent ){
        return Registry.register(CivilizedMobs.CIVILZIED_PROFESSIONS,new Identifier(CivilizedMobs.MOD_ID,id),new CivilziedProfession(heldWorkstation,acquirableWorkstation,soundEvent));
    }

   /* public static final Codec<CivilziedProfession> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(RegistryCodecs.entryList(RegistryKeys.POINT_OF_INTEREST_TYPE).fieldOf("held_workstation").forGetter(profession -> profession.heldWorkstation),
                        SoundEvent.ENTRY_CODEC.fieldOf("working_sound").forGetter(profession -> profession.workingSound))
                .apply(instance, CivilziedProfession::new);
    });
*/
    public static final RegistryKey<CivilziedProfession> PIGLIN_LIBRARIAN = of("piglin_librarian");
    public static final RegistryKey<CivilziedProfession> PIGLIN_CLERIC = of("piglin_cleric");
    public static final RegistryKey<CivilziedProfession> PIGLIN_WEAPONSMITH = of("piglin_weaponsmith");
    public static final RegistryKey<CivilziedProfession> WITHER_CLERIC = of("wither_librarian");



    public static void init(){}


    public void register(Registerable<CivilziedProfession> registerable,RegistryKey<CivilziedProfession> id, TagKey<PointOfInterestType> point,RegistryEntry<SoundEvent> soundEventRegistryEntry){
        //registerable.register(id,new CivilziedProfession(registerable.getRegistryLookup(RegistryKeys.POINT_OF_INTEREST_TYPE).getOrThrow(point), soundEventRegistryEntry));
    }

    private static RegistryKey<CivilziedProfession> of(String id){
        return RegistryKey.of(CivilizedMobs.CIVILIZED_PROFESSION,new Identifier(CivilizedMobs.MOD_ID,id));
    }

    public record CivilizedOffer(){
        
    }
}
