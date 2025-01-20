package net.firemuffin303.villagefoe.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.client.renderer.*;
import net.firemuffin303.villagefoe.client.renderer.model.witherSkeleton.WitherSkeletonWorkerEntityModel;
import net.firemuffin303.villagefoe.client.renderer.trinkets.TrinketItemHeadRenderer;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.minecraft.item.Items;

public class VillageFoeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(WitherSkeletonWorkerEntityModel.WITHER_SKELETON_WORKER,WitherSkeletonWorkerEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntityType.PIGLIN_WORKER, PiglinWorkerRenderer::new);
        EntityRendererRegistry.register(ModEntityType.PIGLIN_LEADER_ENTITY, PiglinQuestEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityType.PILLAGER_WORKER, PillagerWorkerRenderer::new);
        EntityRendererRegistry.register(ModEntityType.PILLAGER_LEADER, PillagerLeaderRenderer::new);
        EntityRendererRegistry.register(ModEntityType.WITHER_SKELETON_WORKER, WitherSkeletonWorkerRenderer::new);
        EntityRendererRegistry.register(ModEntityType.WITHER_SKELETON_LEADER, WitherSkeletonQuestRenderer::new);


        if(VillageFoe.isTrinketsInstall){
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
