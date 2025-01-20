package net.firemuffin303.villagefoe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;

import java.util.function.Consumer;

public class AdvancementDataProvider extends FabricAdvancementProvider {
    protected AdvancementDataProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {

    }
}
