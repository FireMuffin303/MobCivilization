package net.firemuffin303.civilizedmobs.client.renderer.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class TrinketItemHeadRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, LivingEntity livingEntity, float v, float v1, float v2, float v3, float v4, float v5) {
        if(entityModel instanceof ModelWithHead model){
            model.getHead().rotate(matrixStack);
            float f = 0.625F;
            matrixStack.translate(0.0F, -0.25F, 0.0F);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrixStack.scale(f, -f, -f);
            MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer().renderItem(livingEntity,itemStack, ModelTransformationMode.HEAD,false,matrixStack,vertexConsumerProvider,light);
        }
    }
}
