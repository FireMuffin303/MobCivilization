package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.QuestEntityModel;
import net.firemuffin303.civilizedmobs.common.entity.piglin.PiglinQuestEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QuestEntityRenderer extends GeoEntityRenderer<PiglinQuestEntity> {
    public QuestEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new QuestEntityModel());
    }


}
