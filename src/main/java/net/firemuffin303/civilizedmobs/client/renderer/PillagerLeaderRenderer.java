package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.layer.CivilizedBlockAndItemLayer;
import net.firemuffin303.civilizedmobs.client.renderer.model.PillagerLeaderEntityModel;
import net.firemuffin303.civilizedmobs.common.entity.pillager.quest.PillagerQuestEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PillagerLeaderRenderer extends GeoEntityRenderer<PillagerQuestEntity> {
    public PillagerLeaderRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PillagerLeaderEntityModel());
        this.addRenderLayer(new CivilizedBlockAndItemLayer<>(this));
    }
}
