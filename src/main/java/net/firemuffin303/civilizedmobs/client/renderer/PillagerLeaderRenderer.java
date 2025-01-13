package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.layer.CivilizedBlockAndItemLayer;
import net.firemuffin303.civilizedmobs.client.renderer.model.PillagerLeaderEntityModel;
import net.firemuffin303.civilizedmobs.common.entity.pillager.quest.PillagerQuestEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PillagerLeaderRenderer extends GeoEntityRenderer<PillagerQuestEntity> {
    public PillagerLeaderRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PillagerLeaderEntityModel());
        this.addRenderLayer(new CivilizedBlockAndItemLayer<>(this));
    }

    @Override
    public void render(PillagerQuestEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
