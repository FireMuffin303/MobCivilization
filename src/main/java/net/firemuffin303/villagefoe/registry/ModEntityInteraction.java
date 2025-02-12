package net.firemuffin303.villagefoe.registry;

import net.minecraft.entity.EntityInteraction;

public class ModEntityInteraction {
    public static final EntityInteraction WORKER_PIGLIN_HURT = EntityInteraction.create("worker_piglin_hurt");
    public static final EntityInteraction WORKER_PIGLIN_KILLED = EntityInteraction.create("worker_piglin_killed");

    public static final EntityInteraction PILLAGER_WORKER_HURT = EntityInteraction.create("pillager_worker_hurt");
    public static final EntityInteraction PILLAGER_WORKER_KILLED = EntityInteraction.create("pillager_worker_killed");
    public static final EntityInteraction ILLAGER_HURT = EntityInteraction.create("illager_hurt");
    public static final EntityInteraction ILLAGER_KILLED = EntityInteraction.create("illager_killed");
    public static final EntityInteraction ILLAGER_LEADER_KILLED = EntityInteraction.create("illager_leader_killed");
    public static final EntityInteraction RAVENGER_KILLED = EntityInteraction.create("ravenger_killed");

    public static final EntityInteraction WITHER_SKELETON_KILLED = EntityInteraction.create("wither_skeleton_killed");
    public static final EntityInteraction WITHER_KILLED = EntityInteraction.create("wither_killed");
    public static final EntityInteraction WOLF_KILLED = EntityInteraction.create("wolf_killed");
}
