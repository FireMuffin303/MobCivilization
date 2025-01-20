package net.firemuffin303.villagefoe.mixin.structures;

import net.minecraft.util.collection.Pool;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureSpawns.class)
public interface StructureSpawnsAccessor {
    @Accessor("spawns")
    Pool<SpawnSettings.SpawnEntry> getSpawns();

    @Accessor("spawns")
    @Mutable
    void setSpawns(Pool<SpawnSettings.SpawnEntry> pool);
}
