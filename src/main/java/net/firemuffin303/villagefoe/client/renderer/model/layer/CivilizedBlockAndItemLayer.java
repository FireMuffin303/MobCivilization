package net.firemuffin303.villagefoe.client.renderer.model.layer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class CivilizedBlockAndItemLayer<T extends MobEntity & GeoAnimatable> extends BlockAndItemGeoLayer<T> {
    public CivilizedBlockAndItemLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    protected ItemStack getStackForBone(GeoBone bone, T animatable) {
        switch (bone.getName()){
            case "left_arm_item" -> {
                return animatable.isLeftHanded() ? animatable.getMainHandStack() : animatable.getOffHandStack();
            }
            case "right_arm_item" -> {
                return animatable.isLeftHanded() ? animatable.getOffHandStack() : animatable.getMainHandStack();
            }
            case "head" -> {
                return animatable.getEquippedStack(EquipmentSlot.HEAD);
            }
        }
        return super.getStackForBone(bone, animatable);
    }

    @Override
    protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
        return switch (bone.getName()) {
            case "right_arm_item" -> ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
            case "left_arm_item" -> ModelTransformationMode.THIRD_PERSON_LEFT_HAND;
            case "head" -> ModelTransformationMode.HEAD;
            default -> super.getTransformTypeForStack(bone, stack, animatable);
        };
    }

    @Override
    protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, T animatable, VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
        if(stack == animatable.getMainHandStack()){
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            poseStack.translate((float)(animatable.isLeftHanded() ? -1 : 1) / 16.0F, 0.125F, -0F);
            if (stack.getItem() instanceof ShieldItem) {
                poseStack.translate(0.0, 0.125, -0.25);
            }
        } else if (stack == animatable.getOffHandStack()) {
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            if (stack.getItem() instanceof ShieldItem) {
                poseStack.translate(0.0, 0.125, 0.25);
                poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            }
        } else if (stack == animatable.getEquippedStack(EquipmentSlot.HEAD)) {
            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            poseStack.scale(0.625F, -0.625F, -0.625F);
        }

        super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
    }
}
