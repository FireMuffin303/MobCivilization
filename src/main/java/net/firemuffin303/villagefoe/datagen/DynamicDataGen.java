package net.firemuffin303.villagefoe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.datagen.structure.FortressStructureData;
import net.firemuffin303.villagefoe.datagen.structure.PillagerStructureData;
import net.firemuffin303.villagefoe.datagen.structure.StructureData;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class DynamicDataGen extends FabricDynamicRegistryProvider {

    public DynamicDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        StructureData.dataGen(wrapperLookup, entries);
        PillagerStructureData.dataGen(wrapperLookup,entries);
        FortressStructureData.dataGen(wrapperLookup,entries);
    }

    @Override
    public String getName() {
        return VillageFoe.MOD_ID;
    }
}
