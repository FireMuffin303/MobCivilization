package net.firemuffin303.civilizedmobs.mixin.poi;

import com.mojang.logging.LogUtils;
import net.firemuffin303.civilizedmobs.common.PointOfInterestOwner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.poi.PointOfInterest;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Debug(export = true)
@Mixin(RaidManager.class)
public abstract class RaidManagerMixin {
    @Shadow @Final private ServerWorld world;

    @ModifyVariable(method = "startRaid",at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    public List<PointOfInterest> mobCivilized$checkPointOfInterestOwner(List<PointOfInterest> list){
        List<PointOfInterest> pointOfInterests;
        pointOfInterests = list.stream().filter(pointOfInterest -> {
            Entity entity = PointOfInterestOwner.getPointOfInterestOwner(pointOfInterest).getEntity(this.world);
            return entity instanceof VillagerEntity;
        }).toList();
        list = pointOfInterests;
        return list;
    }
}
