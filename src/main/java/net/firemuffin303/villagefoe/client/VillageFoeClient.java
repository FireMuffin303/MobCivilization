package net.firemuffin303.villagefoe.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.firemuffin303.villagefoe.VillageFoe;
import net.firemuffin303.villagefoe.client.renderer.*;
import net.firemuffin303.villagefoe.client.renderer.model.witherSkeleton.WitherSkeletonWorkerEntityModel;
import net.firemuffin303.villagefoe.registry.ModEntityType;

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
            ClientIntegration.trinketIntegration();
        }
    }
}
