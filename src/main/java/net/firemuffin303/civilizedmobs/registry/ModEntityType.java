package net.firemuffin303.civilizedmobs.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
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
import net.minecraft.entity.EntityDimensions;
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
            FabricEntityTypeBuilder.create(SpawnGroup.MISC,WorkerPiglinEntity::new)
                    .dimensions(new EntityDimensions(0.6f,1.95f,true))
                    .build());

    public static EntityType<PiglinQuestEntity> PIGLIN_LEADER_ENTITY = register("piglin_leader",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC,PiglinQuestEntity::new)
                    .dimensions(new EntityDimensions(1.5f,2.55f,true))
                    .build()
            );

    public static EntityType<PillagerWorkerEntity> PILLAGER_WORKER = register("pillager_worker",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC,PillagerWorkerEntity::new)
                    .dimensions(new EntityDimensions(0.6f,1.95f,true)).build()
    );

    public static EntityType<PillagerQuestEntity> PILLAGER_LEADER = register("pillager_leader",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC,PillagerQuestEntity::new)
                    .dimensions(new EntityDimensions(0.6f,1.95f,true)).build()
    );

    public static EntityType<WitherSkeletonWorkerEntity> WITHER_SKELETON_WORKER = register("wither_skeleton_worker",
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,WitherSkeletonWorkerEntity::new)
                    .fireImmune()
                    .specificSpawnBlocks(Blocks.WITHER_ROSE)
                    .dimensions(new EntityDimensions(0.7F, 2.4F,true)).build()
    );

    public static EntityType<WitherSkeletonQuestEntity> WITHER_SKELETON_LEADER = register("wither_skeleton_leader",
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,WitherSkeletonQuestEntity::new)
                    .fireImmune()
                    .specificSpawnBlocks(Blocks.WITHER_ROSE)
                    .dimensions(new EntityDimensions(0.7F, 2.4F,true))
                    .build()
    );

    public static <T extends Entity> EntityType<T> register(String id, EntityType<T> entityType){
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(CivilizedMobs.MOD_ID,id),entityType);
    }

    public static void init(){}
}
