package net.firemuffin303.villagefoe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.firemuffin303.villagefoe.VillageFoe;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class LoottableDataProvider {
    public static final Identifier VILLAGE_PIGLIN_ARMORER_CHEST = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/village_armorer");
    public static final Identifier VILLAGE_PIGLIN_BUTCHER_CHEST = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/village_butcher");
    public static final Identifier VILLAGE_PIGLIN_CARTOGRAPHER_CHEST = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/village_cartographer");
    public static final Identifier VILLAGE_PIGLIN_CLERIC_CHEST = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/village_cleric");
    public static final Identifier VILLAGE_PIGLIN_FLETCHER_CHEST = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/village_fletcher");
    public static final Identifier VILLAGE_PIGLIN_LIBRARY_CHEST = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/village_library");
    public static final Identifier VILLAGE_PIGLIN_TANNERY_CHEST = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/village_tannery");
    public static final Identifier VILLAGE_PIGLIN_WEAPONSMITH_CHEST = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/village_weaponsmith");
    public static final Identifier VILLAGE_PIGLIN_TENT = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/tent_01");
    public static final Identifier VILLAGE_PIGLIN_GENERIC_HOUSE = new Identifier(VillageFoe.MOD_ID,"chests/village/piglin/generic_house");

    public static final Identifier PIGLIN_WORKER = new Identifier(VillageFoe.MOD_ID,"entities/piglin_worker");
    public static final Identifier PIGLIN_LEADER = new Identifier(VillageFoe.MOD_ID,"entities/piglin_leader");
    public static final Identifier PILLAGER_WORKER = new Identifier(VillageFoe.MOD_ID,"entities/pillager_worker");
    public static final Identifier PILLAGER_LEADER = new Identifier(VillageFoe.MOD_ID,"entities/pillager_leader");
    public static final Identifier WITHER_SKELETON_WORKER = new Identifier(VillageFoe.MOD_ID,"entities/wither_skeleton_worker");
    public static final Identifier WITHER_SKELETON_LEADER = new Identifier(VillageFoe.MOD_ID,"entities/wither_skeleton_leader");

    public static class EntityLootDataProvider extends SimpleFabricLootTableProvider{

        public EntityLootDataProvider(FabricDataOutput output) {
            super(output, LootContextTypes.ENTITY);
        }

        @Override
        public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
            LootTable.Builder witherDrops = LootTable.builder().pool(
                            LootPool.builder()
                                    .rolls(ConstantLootNumberProvider.create(1.0F))
                                    .with(ItemEntry.builder(Items.COAL)
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-1.0F, 1.0F)))
                                            .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))
                                    )
                    )
                    .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                            .with(ItemEntry.builder(Items.BONE)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F)))
                                    .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))))
                    .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                            .with(ItemEntry.builder(Blocks.WITHER_SKELETON_SKULL))
                            .conditionally(KilledByPlayerLootCondition.builder())
                            .conditionally(RandomChanceWithLootingLootCondition.builder(0.025F, 0.01F)));

            exporter.accept(LoottableDataProvider.PIGLIN_WORKER,LootTable.builder());
            exporter.accept(LoottableDataProvider.PIGLIN_LEADER,LootTable.builder());

            exporter.accept(LoottableDataProvider.PILLAGER_WORKER,LootTable.builder());
            exporter.accept(LoottableDataProvider.PILLAGER_LEADER,LootTable.builder());

            exporter.accept(LoottableDataProvider.WITHER_SKELETON_WORKER,witherDrops);
            exporter.accept(LoottableDataProvider.WITHER_SKELETON_LEADER,witherDrops);
        }
    }

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

            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_CARTOGRAPHER_CHEST,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.COMPASS).weight(2))
                            .with(ItemEntry.builder(Items.REDSTONE).weight(2))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .with(ItemEntry.builder(Items.PAPER).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,3.0f))))
                            .with(ItemEntry.builder(Items.PORKCHOP).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .build()
            ));

            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_CLERIC_CHEST,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.GLOWSTONE_DUST).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,3.0f))))
                            .with(ItemEntry.builder(Items.REDSTONE).weight(2))
                            .with(ItemEntry.builder(Items.SOUL_SAND).weight(2))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
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

            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_LIBRARY_CHEST,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.BOOK).weight(2))
                            .with(ItemEntry.builder(Items.PAPER).weight(3))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .with(ItemEntry.builder(Items.PORKCHOP).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
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

            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_WEAPONSMITH_CHEST,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.GOLDEN_SWORD).weight(1).apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.SMITE,ConstantLootNumberProvider.create(1))))
                            .with(ItemEntry.builder(Items.IRON_INGOT).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,3.0f))))
                            .with(ItemEntry.builder(Items.GOLDEN_SWORD).weight(3))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .with(ItemEntry.builder(Items.PORKCHOP).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .build()
            ));

            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_TENT,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(1))
                            .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1))
                            .with(ItemEntry.builder(Items.NETHER_WART_BLOCK).weight(3))
                            .with(ItemEntry.builder(Items.BLACKSTONE).weight(3))
                            .with(ItemEntry.builder(Items.NETHERRACK).weight(3))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .with(ItemEntry.builder(Items.PORKCHOP).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .build()
            ));

            exporter.accept(LoottableDataProvider.VILLAGE_PIGLIN_GENERIC_HOUSE,LootTable.builder().pool(
                    LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f,5.0f))
                            .with(ItemEntry.builder(Items.NETHER_WART_BLOCK).weight(3))
                            .with(ItemEntry.builder(Items.BLACKSTONE).weight(3))
                            .with(ItemEntry.builder(Items.STRING).weight(3))
                            .with(ItemEntry.builder(Items.GRAVEL).weight(3))
                            .with(ItemEntry.builder(Items.NETHERRACK).weight(3))
                            .with(ItemEntry.builder(Items.CRIMSON_FUNGUS).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .with(ItemEntry.builder(Items.PORKCHOP).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f,2.0f))))
                            .build()
            ));

        }
    }
}
