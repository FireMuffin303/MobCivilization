package net.firemuffin303.civilizedmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelDataGen::new);
        pack.addProvider(DynamicDataGen::new);
        pack.addProvider(ModTagDataGen.ProfessionTagDataGen::new);
        pack.addProvider(ModTagDataGen.AcquirablePOIDataGen::new);
    }
}