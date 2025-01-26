package net.firemuffin303.villagefoe.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.firemuffin303.villagefoe.client.renderer.trinkets.TrinketItemHeadRenderer;
import net.minecraft.item.Items;

public class ClientIntegration {
    public static void trinketIntegration(){
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
