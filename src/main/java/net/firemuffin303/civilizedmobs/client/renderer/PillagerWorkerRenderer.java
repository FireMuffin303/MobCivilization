package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.PillagerWorkerEntityModel;
import net.firemuffin303.civilizedmobs.common.entity.pillager.PillagerWorkerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PillagerWorkerRenderer extends GeoEntityRenderer<PillagerWorkerEntity> {
    public PillagerWorkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PillagerWorkerEntityModel());
    }
}
