package net.firemuffin303.civilizedmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class DynamicDataGen extends FabricDynamicRegistryProvider {
    public DynamicDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);


    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {

        //RegistryEntry<PointOfInterestType> cleric = wrapperLookup.createRegistryLookup().getOrThrow(RegistryKeys.POINT_OF_INTEREST_TYPE).getOrThrow(PointOfInterestTypes.CLERIC);

        //entries.add(CivilziedProfession.PIGLIN_CLERIC,new CivilziedProfession(RegistryEntryList.of(cleric), SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS));
    }

    @Override
    public String getName() {
        return CivilizedMobs.MOD_ID;
    }
}
