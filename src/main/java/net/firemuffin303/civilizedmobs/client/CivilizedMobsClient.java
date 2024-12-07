package net.firemuffin303.civilizedmobs.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.client.renderer.PiglinQuestEntityRenderer;
import net.firemuffin303.civilizedmobs.client.renderer.PiglinWorkerRenderer;
import net.firemuffin303.civilizedmobs.client.renderer.PillagerWorkerRenderer;
import net.firemuffin303.civilizedmobs.client.renderer.trinkets.TrinketItemHeadRenderer;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.minecraft.item.Items;

public class CivilizedMobsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntityType.PIGLIN_WORKER, PiglinWorkerRenderer::new);
        EntityRendererRegistry.register(ModEntityType.PIGLIN_LEADER_ENTITY, PiglinQuestEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityType.PILLAGER_WORKER, PillagerWorkerRenderer::new);
        ModS2CHandler.init();

        if(CivilizedMobs.isTrinketsInstall){
            TrinketRendererRegistry.registerRenderer(Items.WHITE_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.LIGHT_GRAY_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.GRAY_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.BLACK_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.BROWN_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.RED_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.ORANGE_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.YELLOW_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.LIME_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.GREEN_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.CYAN_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.LIGHT_BLUE_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.BLUE_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.MAGENTA_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.PURPLE_BANNER,new TrinketItemHeadRenderer());
            TrinketRendererRegistry.registerRenderer(Items.PINK_BANNER,new TrinketItemHeadRenderer());
        }
    }
}
