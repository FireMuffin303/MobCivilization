package net.firemuffin303.civilizedmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.firemuffin303.civilizedmobs.registry.ModItems;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModelDataGen extends FabricModelProvider {
    private static final Model SPAWN_EGG = createMincraftItem("template_spawn_egg");
    public ModelDataGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.PIGLIN_WORKER_SPAWN_EGG,SPAWN_EGG);
        itemModelGenerator.register(ModItems.PIGLIN_LEADER_SPAWN_EGG,SPAWN_EGG);
    }

    private static Model createMincraftItem(String string, TextureKey... textureSlots) {
        return new Model(Optional.of(new Identifier("item/" + string)),Optional.empty(), textureSlots);
    }
}
