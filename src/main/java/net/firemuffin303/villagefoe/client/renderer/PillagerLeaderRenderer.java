package net.firemuffin303.villagefoe.client.renderer;

import com.mojang.logging.LogUtils;
import net.firemuffin303.villagefoe.client.renderer.model.layer.CivilizedBlockAndItemLayer;
import net.firemuffin303.villagefoe.client.renderer.model.PillagerLeaderEntityModel;
import net.firemuffin303.villagefoe.common.entity.pillager.quest.PillagerQuestEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

public class PillagerLeaderRenderer extends DynamicGeoEntityRenderer<PillagerQuestEntity> {
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
