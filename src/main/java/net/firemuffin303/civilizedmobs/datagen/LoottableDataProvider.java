package net.firemuffin303.civilizedmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class LoottableDataProvider {
    public static final Identifier VILLAGE_PIGLIN_BUTCHER_CHEST = new Identifier(CivilizedMobs.MOD_ID,"chests/village/piglin/village_butcher");
    public static final Identifier VILLAGE_PIGLIN_ARMORER_CHEST = new Identifier(CivilizedMobs.MOD_ID,"chests/village/piglin/village_armorer");
    public static final Identifier VILLAGE_PIGLIN_TANNERY_CHEST = new Identifier(CivilizedMobs.MOD_ID,"chests/village/piglin/village_tannery");
    public static final Identifier VILLAGE_PIGLIN_FLETCHER_CHEST = new Identifier(CivilizedMobs.MOD_ID,"chests/village/piglin/village_fletcher");

    public static class ChestDataProvider extends SimpleFabricLootTableProvider {
        public ChestDataProvider(FabricDataOutput output) {
            super(output, LootContextTypes.CHEST);
        }

        @Override
        public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_ARMORER_CHEST,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.IRON_INGOT).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,3.0f))))
                            .with(ItemEntry.builder(Items.GOLDEN_BOOTS).weight(3))
                            .with(ItemEntry.builder(Items.GOLDEN_LEGGINGS).weight(3))
                            .with(ItemEntry.builder(Items.GOLDEN_CHESTPLATE).weight(3))
                            .with(ItemEntry.builder(Items.GOLDEN_HELMET).weight(3))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(3))
                            .with(ItemEntry.builder(Items.PORKCHOP).weight(4))
                            .build()
            ));

            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_TANNERY_CHEST,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.STRING).weight(3))
                            .with(ItemEntry.builder(Items.LEATHER).weight(3))
                            .with(ItemEntry.builder(Items.FLINT).weight(3))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(3))
                            .with(ItemEntry.builder(Items.PORKCHOP).weight(4))
                            .build()
            ));


            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_BUTCHER_CHEST,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.STRING).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,3.0f))))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .with(ItemEntry.builder(Items.COAL).weight(3))
                            .with(ItemEntry.builder(Items.PORKCHOP).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .build()
            ));

            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_FLETCHER_CHEST,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.CROSSBOW).apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.PIERCING, ConstantLootNumberProvider.create(1))).weight(1))
                            .with(ItemEntry.builder(Items.CROSSBOW).weight(1))
                            .with(ItemEntry.builder(Items.FEATHER).weight(3))
                            .with(ItemEntry.builder(Items.STRING).weight(6))
                            .with(ItemEntry.builder(Items.FLINT).weight(6))
                            .build()
            ));
        }
    }
}
