package net.firemuffin303.civilizedmobs.registry;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinData;
import net.firemuffin303.civilizedmobs.common.entity.CivilizedPiglinEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntityType {
    public static final TrackedDataHandler<WorkerPiglinData> CIVIL_PIGLIN_DATA = new TrackedDataHandler.ImmutableHandler<>() {
        @Override
        public void write(PacketByteBuf buf, WorkerPiglinData value) {
            buf.writeRegistryValue(Registries.VILLAGER_PROFESSION, value.getProfession());
            buf.writeVarInt(value.getLevel());
        }

        @Override
        public WorkerPiglinData read(PacketByteBuf buf) {
            return new WorkerPiglinData(buf.readRegistryValue(Registries.VILLAGER_PROFESSION), buf.readVarInt());
        }
    };

    public static EntityType<CivilizedPiglinEntity> CIVIL_PIGLIN = register("civil_piglin",
            EntityType.Builder.create(CivilizedPiglinEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.6f,1.95f)
                    .maxTrackingRange(8)
                    .build("civil_piglin"));

    public static <T extends Entity> EntityType<T> register(String id, EntityType<T> entityType){
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(CivilizedMobs.MOD_ID,id),entityType);
    }

    public static void init(){}
}
