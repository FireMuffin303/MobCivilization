package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.QuestEntityModel;
import net.firemuffin303.civilizedmobs.common.entity.quest.QuestEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QuestEntityRenderer extends GeoEntityRenderer<QuestEntity> {
    public QuestEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new QuestEntityModel());
    }


}
