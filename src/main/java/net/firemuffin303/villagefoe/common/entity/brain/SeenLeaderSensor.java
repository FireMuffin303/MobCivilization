package net.firemuffin303.villagefoe.common.entity.brain;

import net.firemuffin303.villagefoe.registry.ModBrains;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SeenLeaderSensor extends Sensor<LivingEntity> {
    private EntityType<?> entityType;

    public SeenLeaderSensor(EntityType<?> entityType){
        super(200);
        this.entityType = entityType;
    }

    @Override
    protected void sense(ServerWorld world, LivingEntity entity) {
        Optional<List<LivingEntity>> optional = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.MOBS);
        if(optional.isPresent()){
            boolean bl = optional.get().stream().anyMatch(livingEntity -> {
                return livingEntity.getType().equals(this.entityType);
            });

            if(bl){
                entity.getBrain().remember(ModBrains.LEADER_DETECTED_RECENTLY, true, 12000L);
            }
        }
    }

    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return Set.of(MemoryModuleType.MOBS);
    }
}
