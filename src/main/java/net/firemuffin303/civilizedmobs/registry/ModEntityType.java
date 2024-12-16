package net.firemuffin303.civilizedmobs.registry;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.entity.WorkerData;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.common.entity.piglin.quest.PiglinQuestEntity;
import net.firemuffin303.civilizedmobs.common.entity.pillager.worker.PillagerWorkerEntity;
import net.firemuffin303.civilizedmobs.common.entity.pillager.quest.PillagerQuestEntity;
import net.firemuffin303.civilizedmobs.common.entity.witherSkelton.quest.WitherSkeletonQuestEntity;
import net.firemuffin303.civilizedmobs.common.entity.witherSkelton.worker.WitherSkeletonWorkerEntity;
import net.minecraft.block.Blocks;
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

    public static EntityType<WorkerPiglinEntity> PIGLIN_WORKER = register("piglin_worker",
            EntityType.Builder.create(WorkerPiglinEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.6f,1.95f)
                    .maxTrackingRange(8)
                    .build("piglin_worker"));

    public static EntityType<PiglinQuestEntity> PIGLIN_LEADER_ENTITY = register("piglin_leader",
            EntityType.Builder.create(PiglinQuestEntity::new,SpawnGroup.MISC)
                    .setDimensions(1.5f,2.55f)
                    .maxTrackingRange(8)
                    .build("piglin_leader")
            );

    public static EntityType<PillagerWorkerEntity> PILLAGER_WORKER = register("pillager_worker",
            EntityType.Builder.create(PillagerWorkerEntity::new,SpawnGroup.MISC)
                    .setDimensions(0.6f,1.95f)
                    .maxTrackingRange(8).build("pillager_worker")
    );

    public static EntityType<PillagerQuestEntity> PILLAGER_LEADER = register("pillager_leader",
            EntityType.Builder.create(PillagerQuestEntity::new,SpawnGroup.MISC)
                    .setDimensions(0.6f,1.95f)
                    .maxTrackingRange(8).build("pillager_leader")
    );

    public static EntityType<WitherSkeletonWorkerEntity> WITHER_SKELETON_WORKER = register("wither_skeleton_worker",
            EntityType.Builder.create(WitherSkeletonWorkerEntity::new,SpawnGroup.MONSTER)
                    .makeFireImmune()
                    .allowSpawningInside(Blocks.WITHER_ROSE)
                    .setDimensions(0.7F, 2.4F)
                    .maxTrackingRange(8).build("wither_skeleton_worker")
    );

    public static EntityType<WitherSkeletonQuestEntity> WITHER_SKELETON_QUEST = register("wither_skeleton_leader",
            EntityType.Builder.create(WitherSkeletonQuestEntity::new,SpawnGroup.MONSTER)
                    .makeFireImmune()
                    .allowSpawningInside(Blocks.WITHER_ROSE)
                    .setDimensions(0.7F, 2.4F)
                    .maxTrackingRange(8).build("wither_skeleton_leader")
    );

    public static <T extends Entity> EntityType<T> register(String id, EntityType<T> entityType){
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(CivilizedMobs.MOD_ID,id),entityType);
    }

    public static void init(){}
}
