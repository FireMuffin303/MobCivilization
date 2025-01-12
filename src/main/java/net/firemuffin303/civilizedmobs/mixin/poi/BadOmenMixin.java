package net.firemuffin303.civilizedmobs.mixin.poi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.firemuffin303.civilizedmobs.common.PointOfInterestOwner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;

@Debug(export = true)
@Mixin(targets = "net/minecraft/entity/effect/StatusEffects$1")
public abstract class BadOmenMixin {
    @WrapOperation(method = "applyUpdateEffect",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isNearOccupiedPointOfInterest(Lnet/minecraft/util/math/BlockPos;)Z"))
    public boolean civilMob$checkOwner(ServerWorld serverWorld, BlockPos pos, Operation<Boolean> original){
        Iterator<PointOfInterest> list = serverWorld.getPointOfInterestStorage().getInSquare(type -> type.isIn(PointOfInterestTypeTags.VILLAGE),pos,32, PointOfInterestStorage.OccupationStatus.IS_OCCUPIED).iterator();
        boolean isVillager = false;
        while(list.hasNext()){
            PointOfInterest pointOfInterest = list.next();
            Entity entity = PointOfInterestOwner.getPointOfInterestOwner(pointOfInterest).getEntity(serverWorld);
            if(entity instanceof VillagerEntity){
                isVillager = true;
            }
        }
        return isVillager && original.call(serverWorld,pos);
    }
}
