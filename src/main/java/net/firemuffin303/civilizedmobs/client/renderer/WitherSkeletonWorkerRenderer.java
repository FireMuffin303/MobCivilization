package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.client.renderer.model.WitherSkeletonWorkerEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.WitherSkeletonEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

public class WitherSkeletonWorkerRenderer extends SkeletonEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(CivilizedMobs.MOD_ID,"textures/entity/wither_skeleton/wither_skeleton_demo.png");

    public WitherSkeletonWorkerRenderer(EntityRendererFactory.Context context) {
        super(context, WitherSkeletonWorkerEntityModel.WITHER_SKELETON_WORKER, EntityModelLayers.WITHER_SKELETON_INNER_ARMOR,EntityModelLayers.WITHER_SKELETON_OUTER_ARMOR);
    }

    @Override
    public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }

    protected void scale(AbstractSkeletonEntity abstractSkeletonEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(1.2F, 1.2F, 1.2F);
    }
}
