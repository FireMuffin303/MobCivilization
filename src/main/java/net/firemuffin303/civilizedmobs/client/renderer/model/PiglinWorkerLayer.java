package net.firemuffin303.civilizedmobs.client.renderer.model;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.WorkerData;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class PiglinWorkerLayer extends GeoRenderLayer<WorkerPiglinEntity> {
    public PiglinWorkerLayer(GeoRenderer<WorkerPiglinEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, WorkerPiglinEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(!animatable.isInvisible()){
            WorkerData workerData = animatable.getWorkerData();
            if(workerData.getProfession() != VillagerProfession.NONE && workerData.getLevel() > 0){
                poseStack.push();

                int level = workerData.getLevel();
                Identifier identifier = new Identifier(CivilizedMobs.MOD_ID,"textures/entity/piglin_worker/level/level"+level+".png");
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderLayer.getEntityCutoutNoCull(identifier));
                poseStack.pop();
                this.getRenderer().reRender(bakedModel,poseStack,bufferSource,animatable,renderType,vertexConsumer,partialTick,packedLight,packedOverlay,1.0f,1.0f,1.0f,1.0f);
                super.render(poseStack, animatable, bakedModel, renderType, bufferSource, vertexConsumer, partialTick, packedLight, packedOverlay);
            }

        }
    }
}
