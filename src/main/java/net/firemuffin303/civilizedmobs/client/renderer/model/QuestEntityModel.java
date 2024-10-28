package net.firemuffin303.civilizedmobs.client.renderer.model;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.piglin.PiglinQuestEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class QuestEntityModel extends GeoModel<PiglinQuestEntity> {
    @Override
    public Identifier getModelResource(PiglinQuestEntity questEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"geo/piglin_worker_none.geo.json");
    }

    @Override
    public Identifier getTextureResource(PiglinQuestEntity questEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"textures/entity/piglin_worker/none.png");
    }

    @Override
    public Identifier getAnimationResource(PiglinQuestEntity questEntity) {
        return new Identifier(CivilizedMobs.MOD_ID,"animations/civil_piglin.animation.json");
    }
}
