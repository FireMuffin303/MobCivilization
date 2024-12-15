package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.WitherSkeletonQuestEntityModel;
import net.firemuffin303.civilizedmobs.common.entity.piglin.quest.PiglinQuestEntity;
import net.firemuffin303.civilizedmobs.common.entity.witherSkelton.quest.WitherSkeletonQuestEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class WitherSkeletonQuestRenderer extends GeoEntityRenderer<WitherSkeletonQuestEntity> {
    public WitherSkeletonQuestRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new WitherSkeletonQuestEntityModel());
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this){
            @Override
            protected ItemStack getStackForBone(GeoBone bone, WitherSkeletonQuestEntity animatable) {
                switch (bone.getName()){
                    case "left_arm_item" -> {
                        return animatable.isLeftHanded() ? animatable.getMainHandStack() : animatable.getOffHandStack();
                    }
                    case "right_arm_item" -> {
                        return animatable.isLeftHanded() ? animatable.getOffHandStack() : animatable.getMainHandStack();
                    }
                }
                return super.getStackForBone(bone, animatable);
            }

            @Override
            protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, WitherSkeletonQuestEntity animatable) {
                return switch (bone.getName()) {
                    case "left_arm_item", "right_arm_item" -> ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
                    default -> super.getTransformTypeForStack(bone, stack, animatable);
                };
            }

            @Override
            protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, WitherSkeletonQuestEntity animatable, VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.scale(2,2,2);

                if(stack == animatable.getMainHandStack()){
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, -0.25);
                    }
                } else if (stack == animatable.getOffHandStack()) {
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, 0.25);
                        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                    }
                }
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }
}
