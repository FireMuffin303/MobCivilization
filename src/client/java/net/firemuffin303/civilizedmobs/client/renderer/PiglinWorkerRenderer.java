package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.client.renderer.model.PiglinWorkerModel;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PiglinWorkerRenderer extends GeoEntityRenderer<WorkerPiglinEntity> {
    public PiglinWorkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PiglinWorkerModel());
    }
}
