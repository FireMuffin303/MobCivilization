package net.firemuffin303.civilizedmobs.client.renderer.model;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.pillager.quest.PillagerQuestEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PillagerLeaderEntityModel extends GeoModel<PillagerQuestEntity> {
    @Override
    public Identifier getModelResource(PillagerQuestEntity pillagerQuestEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"geo/pillager_leader_prototype.geo.json");
    }

    @Override
    public Identifier getTextureResource(PillagerQuestEntity pillagerQuestEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"textures/entity/pillager_worker/pillager_worker.png");
    }

    @Override
    public Identifier getAnimationResource(PillagerQuestEntity pillagerQuestEntity) {
        return null;
    }

    @Override
    public void setCustomAnimations(PillagerQuestEntity animatable, long instanceId, AnimationState<PillagerQuestEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
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
