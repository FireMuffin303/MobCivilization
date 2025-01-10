package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.layer.CivilizedBlockAndItemLayer;
import net.firemuffin303.civilizedmobs.client.renderer.model.WitherSkeletonQuestEntityModel;
import net.firemuffin303.civilizedmobs.common.entity.witherSkelton.quest.WitherSkeletonQuestEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WitherSkeletonQuestRenderer extends GeoEntityRenderer<WitherSkeletonQuestEntity> {
    public WitherSkeletonQuestRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new WitherSkeletonQuestEntityModel());
        this.addRenderLayer(new CivilizedBlockAndItemLayer<>(this));
    }
}
