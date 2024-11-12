package net.firemuffin303.civilizedmobs.common.entity.brain;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;
import java.util.Set;

public class PiglinDetectGoldArmorPlayer extends Sensor<LivingEntity> {
    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        LivingTargetCache livingTargetCache = brain.getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_MOBS).orElse(LivingTargetCache.empty());
        Optional<PlayerEntity> optionalPlayerEntity = Optional.empty();

        for(LivingEntity livingEntity:livingTargetCache.iterate(livingEntity -> livingEntity instanceof PlayerEntity)){
            if(!PiglinBrain.wearsGoldArmor(livingEntity) && entity.canTarget(livingEntity) && livingEntity instanceof PlayerEntity player){
                optionalPlayerEntity = Optional.of(player);
            }
        }

        brain.remember(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, optionalPlayerEntity);

    }

    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,MemoryModuleType.VISIBLE_MOBS);
    }
}
