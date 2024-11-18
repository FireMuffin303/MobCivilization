package net.firemuffin303.civilizedmobs.registry;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.WorkerData;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.common.entity.piglin.quest.PiglinQuestEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntityType {
    public static final TrackedDataHandler<WorkerData> WORKER_DATA = new TrackedDataHandler.ImmutableHandler<>() {
        @Override
        public void write(PacketByteBuf buf, WorkerData value) {
            buf.writeRegistryValue(Registries.VILLAGER_PROFESSION, value.getProfession());
            buf.writeVarInt(value.getLevel());
        }

        @Override
        public WorkerData read(PacketByteBuf buf) {
            return new WorkerData(buf.readRegistryValue(Registries.VILLAGER_PROFESSION), buf.readVarInt());
        }
    };

    public static EntityType<WorkerPiglinEntity> PIGLIN_WORKER = register("worker_piglin",
            EntityType.Builder.create(WorkerPiglinEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.6f,1.95f)
                    .maxTrackingRange(8)
                    .build("worker_piglin"));

    public static EntityType<PiglinQuestEntity> PIGLIN_LEADER_ENTITY = register("piglin_quest_entity",
            EntityType.Builder.create(PiglinQuestEntity::new,SpawnGroup.MISC)
                    .setDimensions(1.5f,2.55f)
                    .maxTrackingRange(8)
                    .build("quest_entity")
            );

    public static <T extends Entity> EntityType<T> register(String id, EntityType<T> entityType){
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(CivilizedMobs.MOD_ID,id),entityType);
    }

    public static void init(){}
}
