package net.firemuffin303.civilizedmobs.mixin.structures;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Structure.Config.class)
public interface StructureConfigAccessor {
    @Accessor("spawnOverrides")
    @Mutable
    void setSpawnOverrides(Map<SpawnGroup, StructureSpawns> spawnOverrides);
}
