package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.PiglinQuestEntityModel;
import net.firemuffin303.civilizedmobs.common.entity.piglin.PiglinQuestEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PiglinQuestEntityRenderer extends GeoEntityRenderer<PiglinQuestEntity> {
    public PiglinQuestEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PiglinQuestEntityModel());
    }


}
