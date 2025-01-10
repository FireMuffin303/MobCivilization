package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.layer.CivilizedBlockAndItemLayer;
import net.firemuffin303.civilizedmobs.client.renderer.model.layer.PiglinWorkerLayer;
import net.firemuffin303.civilizedmobs.client.renderer.model.PiglinWorkerModel;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PiglinWorkerRenderer extends GeoEntityRenderer<WorkerPiglinEntity> {


    public PiglinWorkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PiglinWorkerModel());
        this.addRenderLayer(new PiglinWorkerLayer(this));
        this.addRenderLayer(new CivilizedBlockAndItemLayer<>(this));

    }

    @Override
    public boolean isShaking(WorkerPiglinEntity animatable) {
        return super.isShaking(animatable) || animatable.shouldZombify();
    }

}
