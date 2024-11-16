package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.client.renderer.model.PiglinWorkerLayer;
import net.firemuffin303.civilizedmobs.client.renderer.model.PiglinWorkerModel;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class PiglinWorkerRenderer extends GeoEntityRenderer<WorkerPiglinEntity> {


    public PiglinWorkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PiglinWorkerModel());
        this.addRenderLayer(new PiglinWorkerLayer(this));
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this){
            @Override
            protected ItemStack getStackForBone(GeoBone bone, WorkerPiglinEntity animatable) {
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
            protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, WorkerPiglinEntity animatable) {
                return switch (bone.getName()) {
                    case "left_arm_item", "right_arm_item" -> ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
                    default -> super.getTransformTypeForStack(bone, stack, animatable);
                };
            }

            @Override
            protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, WorkerPiglinEntity animatable, VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
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

    @Override
    public boolean isShaking(WorkerPiglinEntity animatable) {
        return super.isShaking(animatable) || animatable.shouldZombify();
    }

}
