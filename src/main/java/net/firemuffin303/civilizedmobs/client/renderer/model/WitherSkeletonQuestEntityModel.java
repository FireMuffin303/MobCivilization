package net.firemuffin303.civilizedmobs.client.renderer.model;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.witherSkelton.quest.WitherSkeletonQuestEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WitherSkeletonQuestEntityModel extends GeoModel<WitherSkeletonQuestEntity> {
    @Override
    public Identifier getModelResource(WitherSkeletonQuestEntity witherSkeletonQuestEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"geo/wither_skeleton/wither_skeleton_leader.geo.json");
    }

    @Override
    public Identifier getTextureResource(WitherSkeletonQuestEntity witherSkeletonQuestEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"textures/entity/wither_skeleton/wither_skeleton_leader.png");
    }

    @Override
    public Identifier getAnimationResource(WitherSkeletonQuestEntity witherSkeletonQuestEntity) {
        return null;
    }

    @Override
    public void setCustomAnimations(WitherSkeletonQuestEntity animatable, long instanceId, AnimationState<WitherSkeletonQuestEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        CoreGeoBone body = getAnimationProcessor().getBone("body");
        CoreGeoBone leftArm = getAnimationProcessor().getBone("left_arm");
        CoreGeoBone rightArm = getAnimationProcessor().getBone("right_arm");
        EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if(head != null){
            head.setRotX(entityModelData.headPitch()* MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityModelData.netHeadYaw()* MathHelper.RADIANS_PER_DEGREE);
        }

        getAnimationProcessor().getBone("left_leg").setRotX((float) (MathHelper.cos(animationState.getLimbSwing() * 0.6662f + 3.1415927F)*1.4f * animationState.getLimbSwingAmount()));
        getAnimationProcessor().getBone("right_leg").setRotX((float) (MathHelper.cos(animationState.getLimbSwing() * 0.6662f)*1.4f * (animationState.getLimbSwingAmount())));

        leftArm.setRotX(MathHelper.cos(animationState.getLimbSwing() * 0.6662f)*1.4f * animationState.getLimbSwingAmount());
        rightArm.setRotX(MathHelper.cos(animationState.getLimbSwing() * 0.6662f + 3.1415927F)*1.4f * animationState.getLimbSwingAmount());

        if(animatable.isAttacking()){
            if(animatable.isLeftHanded()){
                leftArm.setRotX(1.8f);
            }else{
                rightArm.setRotX(1.8f);
            }
        }
    }
}
