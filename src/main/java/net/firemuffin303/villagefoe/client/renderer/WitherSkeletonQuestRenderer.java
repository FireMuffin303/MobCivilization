package net.firemuffin303.villagefoe.client.renderer;

import net.firemuffin303.villagefoe.client.renderer.model.layer.CivilizedBlockAndItemLayer;
import net.firemuffin303.villagefoe.client.renderer.model.witherSkeleton.WitherSkeletonQuestEntityModel;
import net.firemuffin303.villagefoe.common.entity.witherSkelton.quest.WitherSkeletonQuestEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class WitherSkeletonQuestRenderer extends GeoEntityRenderer<WitherSkeletonQuestEntity> {
    public WitherSkeletonQuestRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new WitherSkeletonQuestEntityModel());
        this.addRenderLayer(new CivilizedBlockAndItemLayer<>(this));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public void render(WitherSkeletonQuestEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();
        poseStack.scale(1.2F, 1.2F, 1.2F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
