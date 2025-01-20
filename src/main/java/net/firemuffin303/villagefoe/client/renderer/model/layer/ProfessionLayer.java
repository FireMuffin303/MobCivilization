package net.firemuffin303.villagefoe.client.renderer.model.layer;

import net.firemuffin303.villagefoe.common.entity.WorkerContainer;
import net.firemuffin303.villagefoe.common.entity.WorkerData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ProfessionLayer<T extends MobEntity & WorkerContainer & GeoAnimatable> extends GeoRenderLayer<T> {
    private final Identifier identifier;
    public ProfessionLayer(GeoRenderer<T> entityRendererIn,EntityType<T> entityType) {
        super(entityRendererIn);
        this.identifier = EntityType.getId(entityType);
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(!animatable.isInvisible()){
            WorkerData workerData = animatable.getWorkerData();
            if(workerData.getProfession() != VillagerProfession.NONE){
                poseStack.push();

                String path = String.format("textures/entity/%s/profession/%s.png",this.identifier.getPath(),workerData.getProfession().id());
                Identifier identifier = new Identifier(this.identifier.getNamespace(),path);
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderLayer.getEntityCutoutNoCull(identifier));
                poseStack.pop();
                this.getRenderer().reRender(bakedModel,poseStack,bufferSource,animatable,renderType,vertexConsumer,partialTick,packedLight,packedOverlay,1f,1f,1f,1f);
                super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            }
        }
    }
}
