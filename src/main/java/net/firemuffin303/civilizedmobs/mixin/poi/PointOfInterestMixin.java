package net.firemuffin303.civilizedmobs.mixin.poi;

import net.firemuffin303.civilizedmobs.common.PointOfInterestOwner;
import net.minecraft.world.poi.PointOfInterest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PointOfInterest.class)
public abstract class PointOfInterestMixin implements PointOfInterestOwner.PointOfInterestOwnerAccessor {
    @Unique
    private final PointOfInterestOwner owner = new PointOfInterestOwner();

    @Override
    public PointOfInterestOwner mobCivilization$getPointOfInterestOwner() {
        return this.owner;
    }
}
