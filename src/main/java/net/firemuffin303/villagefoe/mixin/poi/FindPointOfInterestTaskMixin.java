package net.firemuffin303.villagefoe.mixin.poi;

import com.mojang.datafixers.kinds.Const;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.firemuffin303.villagefoe.common.PointOfInterestOwner;
import net.minecraft.entity.ai.brain.MemoryQueryResult;
import net.minecraft.entity.ai.brain.task.FindPointOfInterestTask;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Predicate;

@Debug(export = true)
@Mixin(FindPointOfInterestTask.class)
public abstract class FindPointOfInterestTaskMixin {

    @Inject(method = "method_46880",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/poi/PointOfInterestStorage;getPosition(Ljava/util/function/Predicate;Ljava/util/function/BiPredicate;Lnet/minecraft/util/math/BlockPos;I)Ljava/util/Optional;"))
    private static void mobCivil$setPOIOwner(PointOfInterestStorage pointOfInterestStorage, Predicate<RegistryEntry<PointOfInterestType>> predicate, BlockPos blockPos, MemoryQueryResult<Const.Mu<Unit>, GlobalPos> memoryQueryResult, ServerWorld serverWorld, Optional optional, PathAwareEntity pathAwareEntity, Long2ObjectMap long2ObjectMap, RegistryEntry<PointOfInterestType> poiType, CallbackInfo ci){
        Optional<PointOfInterest> pointOfInterest = pointOfInterestStorage.getInCircle(predicate,blockPos,1, PointOfInterestStorage.OccupationStatus.HAS_SPACE).findFirst();
        pointOfInterest.ifPresent(pointOfInterest1 -> PointOfInterestOwner.getPointOfInterestOwner(pointOfInterest1).setUuid(pathAwareEntity.getUuid()));
    }
}
