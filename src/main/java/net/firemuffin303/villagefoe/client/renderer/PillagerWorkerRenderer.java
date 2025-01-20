package net.firemuffin303.villagefoe.client.renderer;

import net.firemuffin303.villagefoe.client.renderer.model.layer.CivilizedBlockAndItemLayer;
import net.firemuffin303.villagefoe.client.renderer.model.PillagerWorkerEntityModel;
import net.firemuffin303.villagefoe.client.renderer.model.layer.ProfessionLayer;
import net.firemuffin303.villagefoe.client.renderer.model.layer.ProfessionLevelLayer;
import net.firemuffin303.villagefoe.common.entity.pillager.worker.PillagerWorkerEntity;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PillagerWorkerRenderer extends GeoEntityRenderer<PillagerWorkerEntity> {
    public PillagerWorkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PillagerWorkerEntityModel());
        this.addRenderLayer(new ProfessionLayer<>(this, ModEntityType.PILLAGER_WORKER));
        this.addRenderLayer(new ProfessionLevelLayer<>(this,ModEntityType.PILLAGER_WORKER));
        this.addRenderLayer(new CivilizedBlockAndItemLayer<>(this));

    }

    @Override
    public void render(PillagerWorkerEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
