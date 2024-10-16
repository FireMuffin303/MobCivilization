package net.firemuffin303.civilizedmobs.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.client.renderer.PiglinWorkerRenderer;
import net.firemuffin303.civilizedmobs.client.renderer.QuestEntityRenderer;
import net.firemuffin303.civilizedmobs.client.screen.QuestScreen;
import net.firemuffin303.civilizedmobs.common.quest.QuestList;
import net.firemuffin303.civilizedmobs.common.screen.QuestScreenHandler;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.firemuffin303.civilizedmobs.registry.ModScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandler;

public class CivilizedMobsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntityType.CIVIL_PIGLIN, PiglinWorkerRenderer::new);
        EntityRendererRegistry.register(ModEntityType.QUEST_ENTITY, QuestEntityRenderer::new);

        HandledScreens.register(ModScreenHandlerType.QUEST_SCREEN, QuestScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(CivilizedMobs.QUEST_SCREEN_PAYLOAD_ID,(minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int syncID = packetByteBuf.readInt();
            QuestList quests = QuestList.fromPacket(packetByteBuf);

            int xp = packetByteBuf.readInt();
            int level = packetByteBuf.readInt();

            minecraftClient.execute(() -> {
                if(minecraftClient.player != null){
                    ScreenHandler screenHandler = minecraftClient.player.currentScreenHandler;
                    if(screenHandler.syncId == syncID && screenHandler instanceof QuestScreenHandler questScreenHandler){
                        questScreenHandler.setQuestList(quests);
                        questScreenHandler.setLevel(level);
                        questScreenHandler.setXpProgress(xp);
                    }
                }

            });
        });
    }
}
