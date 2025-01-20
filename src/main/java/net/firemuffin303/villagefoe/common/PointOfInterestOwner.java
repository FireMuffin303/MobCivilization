package net.firemuffin303.villagefoe.common;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.poi.PointOfInterest;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PointOfInterestOwner {
    private @Nullable UUID uuid;

    public void setUuid(UUID uuid){
        this.uuid = uuid;
    }

    public @Nullable UUID getUuid() {
        return uuid;
    }

    public @Nullable Entity getEntity(ServerWorld serverWorld){
        if(this.uuid == null){
            return null;
        }
        return serverWorld.getEntity(this.uuid);
    }

    public static PointOfInterestOwner getPointOfInterestOwner(PointOfInterest pointOfInterest){
        return ((PointOfInterestOwnerAccessor)pointOfInterest).mobCivilization$getPointOfInterestOwner();
    }

    public interface PointOfInterestOwnerAccessor{
        PointOfInterestOwner mobCivilization$getPointOfInterestOwner();
    }
}
