package net.firemuffin303.civilizedmobs.client.renderer;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.client.renderer.model.CivilPiglinModel;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CivilPiglinRenderer extends GeoEntityRenderer<WorkerPiglinEntity> {
    public CivilPiglinRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CivilPiglinModel());
    }

    @Override
    public Identifier getTextureLocation(WorkerPiglinEntity animatable) {
        return new Identifier(CivilizedMobs.MOD_ID,"textures/entity/civil_piglin.png");
    }
}
