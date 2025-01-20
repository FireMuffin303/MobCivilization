package net.firemuffin303.villagefoe.mixin.structures;

import net.minecraft.structure.StructureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureSet.class)
public interface StructureSetAccessor {
    @Accessor("structures")
    @Mutable
    List<StructureSet.WeightedEntry> getStructures();

    @Accessor("structures")
    @Mutable
    void setStructures(List<StructureSet.WeightedEntry> list);
}
