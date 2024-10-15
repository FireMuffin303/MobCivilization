package net.firemuffin303.civilizedmobs.client.renderer.model;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillagerProfession;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PiglinWorkerModel extends GeoModel<WorkerPiglinEntity> {
    @Override
    public Identifier getModelResource(WorkerPiglinEntity workerPiglinEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"geo/piglin_worker_none.geo.json");
    }

    @Override
    public Identifier getTextureResource(WorkerPiglinEntity workerPiglinEntity) {
        VillagerProfession villagerProfession = workerPiglinEntity.getWorkerData().getProfession();
        String path = villagerProfession.id();
        return new Identifier(CivilizedMobs.MOD_ID,"textures/entity/piglin_worker/"+path+".png");
    }

    @Override
    public Identifier getAnimationResource(WorkerPiglinEntity workerPiglinEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"animations/civil_piglin.animation.json");
    }

    @Override
    public void setCustomAnimations(WorkerPiglinEntity animatable, long instanceId, AnimationState<WorkerPiglinEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        CoreGeoBone body = getAnimationProcessor().getBone("body");
        EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if(head != null){
            head.setRotX(entityModelData.headPitch()* MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityModelData.netHeadYaw()* MathHelper.RADIANS_PER_DEGREE);
        }

        getAnimationProcessor().getBone("left_leg").setRotX((float) (MathHelper.cos(animationState.getLimbSwing() * 0.6662f + 3.1415927F)*1.4f * animationState.getLimbSwingAmount()));
        getAnimationProcessor().getBone("right_leg").setRotX((float) (MathHelper.cos(animationState.getLimbSwing() * 0.6662f)*1.4f * (animationState.getLimbSwingAmount())));

        getAnimationProcessor().getBone("left_arm").setRotX(MathHelper.cos(animationState.getLimbSwing() * 0.6662f)*1.4f * animationState.getLimbSwingAmount());
        getAnimationProcessor().getBone("right_arm").setRotX(MathHelper.cos(animationState.getLimbSwing() * 0.6662f + 3.1415927F)*1.4f * animationState.getLimbSwingAmount());
    }
}
