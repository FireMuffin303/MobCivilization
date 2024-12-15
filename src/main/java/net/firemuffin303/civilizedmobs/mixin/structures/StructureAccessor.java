package net.firemuffin303.civilizedmobs.mixin.structures;

import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Structure.class)
public interface StructureAccessor {

    @Accessor("config")
    Structure.Config getConfig();

    @Accessor("config")
    @Mutable
    void setConfig(Structure.Config config);
}
