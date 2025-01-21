package net.firemuffin303.villagefoe.common.entity;

import net.minecraft.server.world.ServerWorld;

public interface LeaderSummoner {
    void summonLeader(ServerWorld serverWorld, long worldTime);

    boolean canSummonLeader();
}
