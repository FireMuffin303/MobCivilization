package net.firemuffin303.civilizedmobs.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.firemuffin303.civilizedmobs.client.renderer.PiglinQuestEntityRenderer;
import net.firemuffin303.civilizedmobs.client.renderer.PiglinWorkerRenderer;
import net.firemuffin303.civilizedmobs.client.renderer.PillagerWorkerRenderer;
import net.firemuffin303.civilizedmobs.common.entity.pillager.PillagerWorkerEntity;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;

public class CivilizedMobsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntityType.PIGLIN_WORKER, PiglinWorkerRenderer::new);
        EntityRendererRegistry.register(ModEntityType.PIGLIN_LEADER_ENTITY, PiglinQuestEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityType.PILLAGER_WORKER, PillagerWorkerRenderer::new);


        ModS2CHandler.init();


    }
}
