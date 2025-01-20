package net.firemuffin303.villagefoe.client.renderer;

import net.firemuffin303.villagefoe.client.renderer.model.layer.CivilizedBlockAndItemLayer;
import net.firemuffin303.villagefoe.client.renderer.model.layer.ProfessionLayer;
import net.firemuffin303.villagefoe.client.renderer.model.layer.ProfessionLevelLayer;
import net.firemuffin303.villagefoe.client.renderer.model.witherSkeleton.WitherSkeletonWorkerEntityModelGecko;
import net.firemuffin303.villagefoe.common.entity.witherSkelton.worker.WitherSkeletonWorkerEntity;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WitherSkeletonWorkerRenderer extends GeoEntityRenderer<WitherSkeletonWorkerEntity> {

    public WitherSkeletonWorkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new WitherSkeletonWorkerEntityModelGecko());
        this.addRenderLayer(new ProfessionLayer<>(this, ModEntityType.WITHER_SKELETON_WORKER));
        this.addRenderLayer(new ProfessionLevelLayer<>(this,ModEntityType.WITHER_SKELETON_WORKER));
        this.addRenderLayer(new CivilizedBlockAndItemLayer<>(this));
    }

    @Override
    public void render(WitherSkeletonWorkerEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();
        poseStack.scale(1.2F, 1.2F, 1.2F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
