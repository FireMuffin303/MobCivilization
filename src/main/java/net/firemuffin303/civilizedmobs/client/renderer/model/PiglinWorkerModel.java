package net.firemuffin303.civilizedmobs.client.renderer.model;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
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

        //Y = yaw | X = pitch

        if(animatable.getActivity() == PiglinActivity.CROSSBOW_CHARGE){
            CoreGeoBone holdingArm = animatable.isLeftHanded() ? leftArm : rightArm;
            CoreGeoBone pullingArm = animatable.isLeftHanded() ? rightArm : leftArm;
            holdingArm.setRotY(animatable.isLeftHanded() ? -0.8F : 0.8F);
            pullingArm.setRotX(0.97079635F);
            rightArm.setRotX(0.97079635F);
            float f = (float) CrossbowItem.getPullTime(animatable.getActiveItem());
            float g = MathHelper.clamp((float)animatable.getItemUseTime(), 0.0F, f);
            float h = g / f;
            pullingArm.setRotY(MathHelper.lerp(h, 0.4F, 0.85F) * -1);
            pullingArm.setRotX(MathHelper.lerp(h, leftArm.getRotX(), 1.5707964F));
        } else if (animatable.getActivity() == PiglinActivity.CROSSBOW_HOLD) {
            CoreGeoBone holdingArm = animatable.isLeftHanded() ? leftArm : rightArm;
            CoreGeoBone pullingArm = animatable.isLeftHanded() ? rightArm : leftArm;

            holdingArm.setRotY(animatable.isLeftHanded() ? -0.3f: 0.3f + (entityModelData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
            pullingArm.setRotY(animatable.isLeftHanded() ? 0.6F : -0.6F + ((entityModelData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE)));
            holdingArm.setRotX(1.5707964F + (entityModelData.headPitch() * MathHelper.RADIANS_PER_DEGREE)-0.1f);
            pullingArm.setRotX(1.5f + (entityModelData.headPitch() * MathHelper.RADIANS_PER_DEGREE));
        } else if (animatable.getActivity() == PiglinActivity.ATTACKING_WITH_MELEE_WEAPON) {
            if(animatable.isLeftHanded()){
                leftArm.setRotX(1.8f);
            }else{
                rightArm.setRotX(1.8f);
            }
        }else{
            rightArm.setRotY(0);
            leftArm.setRotY(0);
        }
    }


}
