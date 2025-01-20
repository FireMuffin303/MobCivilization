package net.firemuffin303.villagefoe.datagen.integration;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.common.integration.muffinsQuest.ModQuests;
import net.firemuffin303.muffinsquestlib.common.quest.Quest;
import net.firemuffin303.muffinsquestlib.common.registry.QuestRegistries;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class QuestDataGen {
    public static class QuestTagProvider extends FabricTagProvider<Quest> {
        public QuestTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, QuestRegistries.QUEST_KEY, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            this.getOrCreateTagBuilder(ModQuests.PIGLIN_QUEST)
                    .add(ModQuests.KILL_10_HOGLINS)
                    .add(ModQuests.KILL_20_HOGLINS)
                    .add(ModQuests.KILL_30_HOGLINS)
                    .add(ModQuests.KILL_10_WITHER_SKELETON)
                    .add(ModQuests.KILL_20_WITHER_SKELETON)
                    .add(ModQuests.KILL_30_WITHER_SKELETON)
                    .add(ModQuests.KILL_10_MAGMA_CREAM)
                    .add(ModQuests.KILL_20_MAGMA_CREAM)
                    .add(ModQuests.KILL_30_MAGMA_CREAM)
                    .add(ModQuests.KILL_10_GHAST)
                    .add(ModQuests.KILL_20_GHAST)
                    .add(ModQuests.KILL_30_GHAST)
                    .add(ModQuests.KILL_10_BLAZE)
                    .add(ModQuests.KILL_20_BLAZE)
                    .add(ModQuests.KILL_30_BLAZE)
                    .add(ModQuests.MINE_32_NETHER_GOLD_ORE);

            this.getOrCreateTagBuilder(ModQuests.PILLAGER_QUEST)
                    .add(ModQuests.KILL_10_VILLAGER)
                    .add(ModQuests.KILL_20_VILLAGER)
                    .add(ModQuests.KILL_30_VILLAGER)
                    .add(ModQuests.KILL_10_IRON_GOLEM)
                    .add(ModQuests.KILL_20_IRON_GOLEM)
                    .add(ModQuests.KILL_30_IRON_GOLEM)
                    .add(ModQuests.MINE_8_SCULK_SENSOR);

            this.getOrCreateTagBuilder(ModQuests.WITHER_QUEST)
                    .add(ModQuests.KILL_10_HOGLINS)
                    .add(ModQuests.KILL_20_HOGLINS)
                    .add(ModQuests.KILL_30_HOGLINS)
                    .add(ModQuests.KILL_10_PIGLIN)
                    .add(ModQuests.KILL_20_PIGLIN)
                    .add(ModQuests.KILL_30_PIGLIN)
                    .add(ModQuests.KILL_10_MAGMA_CREAM)
                    .add(ModQuests.KILL_20_MAGMA_CREAM)
                    .add(ModQuests.KILL_30_MAGMA_CREAM)
                    .add(ModQuests.KILL_10_GHAST)
                    .add(ModQuests.KILL_20_GHAST)
                    .add(ModQuests.KILL_30_GHAST)
                    .add(ModQuests.KILL_10_BLAZE)
                    .add(ModQuests.KILL_20_BLAZE)
                    .add(ModQuests.KILL_30_BLAZE);
        }
    }

    public static class QuestDynamicProvider extends FabricDynamicRegistryProvider {
        public QuestDynamicProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
            ModQuests.register(wrapperLookup,entries);
        }

        @Override
        public String getName() {
            return VillageFoe.MOD_ID + " Quest";
        }
    }
}
