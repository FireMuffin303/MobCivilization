package net.firemuffin303.civilizedmobs.client.renderer.model;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CivilPiglinModel extends GeoModel<WorkerPiglinEntity> {
    @Override
    public Identifier getModelResource(WorkerPiglinEntity workerPiglinEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"geo/civil_piglin.geo.json");
    }

    @Override
    public Identifier getTextureResource(WorkerPiglinEntity workerPiglinEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"textures/entity/civil_piglin.png");
    }

    @Override
    public Identifier getAnimationResource(WorkerPiglinEntity workerPiglinEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"animations/civil_piglin.animation.json");
    }

    @Override
    public void setCustomAnimations(WorkerPiglinEntity animatable, long instanceId, AnimationState<WorkerPiglinEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if(head != null){
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityModelData.headPitch()* MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityModelData.netHeadYaw()* MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
