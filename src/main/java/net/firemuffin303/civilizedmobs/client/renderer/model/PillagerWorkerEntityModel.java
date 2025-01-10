package net.firemuffin303.civilizedmobs.client.renderer.model;

import com.mojang.logging.LogUtils;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.pillager.worker.PillagerWorkerEntity;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.logging.Log;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PillagerWorkerEntityModel extends GeoModel<PillagerWorkerEntity> {
    @Override
    public Identifier getModelResource(PillagerWorkerEntity pillagerWorkerEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"geo/pillager/pillager_worker.geo.json");
    }

    @Override
    public Identifier getTextureResource(PillagerWorkerEntity pillagerWorkerEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"textures/entity/pillager_worker/pillager_worker.png");
    }

    @Override
    public Identifier getAnimationResource(PillagerWorkerEntity pillagerWorkerEntity) {
        return null;
    }

    @Override
    public void setCustomAnimations(PillagerWorkerEntity animatable, long instanceId, AnimationState<PillagerWorkerEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
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
        this.crossBowAnimation(animatable,entityModelData,leftArm,rightArm);
    }


    private void crossBowAnimation(PillagerWorkerEntity animatable,EntityModelData entityModelData , CoreGeoBone leftArm,CoreGeoBone rightArm){
        //Y = yaw | X = pitch
        if(animatable.isCharging()){
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
        } else if (animatable.isHolding(Items.CROSSBOW)) {
            CoreGeoBone holdingArm = animatable.isLeftHanded() ? leftArm : rightArm;
            CoreGeoBone pullingArm = animatable.isLeftHanded() ? rightArm : leftArm;

            holdingArm.setRotY(animatable.isLeftHanded() ? -0.3f: 0.3f + (entityModelData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
            pullingArm.setRotY(animatable.isLeftHanded() ? 0.6F : -0.6F + ((entityModelData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE)));
            holdingArm.setRotX(1.5707964F + (entityModelData.headPitch() * MathHelper.RADIANS_PER_DEGREE)-0.1f);
            pullingArm.setRotX(1.5f + (entityModelData.headPitch() * MathHelper.RADIANS_PER_DEGREE));
        }else{
            rightArm.setRotY(0);
            leftArm.setRotY(0);
        }
    }
}
