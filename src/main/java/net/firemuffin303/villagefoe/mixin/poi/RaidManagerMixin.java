package net.firemuffin303.villagefoe.mixin.poi;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.firemuffin303.villagefoe.common.PointOfInterestOwner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.Raid;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.poi.PointOfInterest;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Debug(export = true)
@Mixin(RaidManager.class)
public abstract class RaidManagerMixin {
    @Shadow @Final private ServerWorld world;

    @Inject(method = "startRaid",at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;",shift = At.Shift.AFTER))
    public void mobCivilized$checkPointOfInterestOwner(ServerPlayerEntity player, CallbackInfoReturnable<Raid> cir, @Local LocalRef<List<PointOfInterest>> list){
        List<PointOfInterest> pointOfInterests;
        pointOfInterests = list.get().stream().filter(pointOfInterest -> {
            Entity entity = PointOfInterestOwner.getPointOfInterestOwner(pointOfInterest).getEntity(this.world);
            return entity instanceof VillagerEntity;
        }).toList();
        list.set(pointOfInterests);
    }

}
