package net.firemuffin303.civilizedmobs.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.firemuffin303.civilizedmobs.client.renderer.PiglinWorkerRenderer;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;

public class CivilizedMobsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntityType.CIVIL_PIGLIN, PiglinWorkerRenderer::new);

    }
}
