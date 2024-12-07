package net.firemuffin303.civilizedmobs.common.event;

import net.firemuffin303.civilizedmobs.common.entity.brain.IllagerHostileSensor;
import net.firemuffin303.civilizedmobs.mixin.ActiveTargetGoalAccessor;
import net.firemuffin303.civilizedmobs.mixin.FleeEntityGoalAccessor;
import net.firemuffin303.civilizedmobs.mixin.MobAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.Raid;

import java.util.concurrent.atomic.AtomicInteger;

public class ModServerEntityEvents {

    public static void IllagerLoaded(Entity entity, ServerWorld world){
        if(entity instanceof EvokerEntity evokerEntity){
            GoalSelector goalSelector = ((MobAccessor)evokerEntity).getGoalSelector();
            AtomicInteger priority = new AtomicInteger(0);
            goalSelector.getGoals().removeIf(prioritizedGoal -> {

                if(prioritizedGoal.getGoal() instanceof FleeEntityGoal<?> fleeEntityGoal&& ((FleeEntityGoalAccessor)fleeEntityGoal).getClassToFleeFrom() == PlayerEntity.class){
                    priority.set(prioritizedGoal.getPriority());
                    return true;
                }
                return false;
            });

            goalSelector.add(priority.get(),new FleeEntityGoal<>(evokerEntity,PlayerEntity.class,8.0f,0.6,1.0,livingEntity -> {
                PlayerEntity player = (PlayerEntity) livingEntity;
                return !IllagerHostileSensor.isHoldingOminousBanner(player);
            }));
        }

        if(entity instanceof IllagerEntity || entity instanceof VexEntity){
            replaceTargetGoal((PathAwareEntity) entity);
        }

        if(entity instanceof RavagerEntity ravagerEntity){
            replaceTargetGoal(ravagerEntity);
        }
    }

    private static void replaceTargetGoal(PathAwareEntity pathAwareEntity){
        GoalSelector goalSelector = ((MobAccessor)pathAwareEntity).getTargetSelector();
        AtomicInteger priority = new AtomicInteger(0);
        goalSelector.getGoals().removeIf(prioritizedGoal -> {
            if(prioritizedGoal.getGoal() instanceof ActiveTargetGoal<?> activeTargetGoal){
                if(((ActiveTargetGoalAccessor<?>)activeTargetGoal).getTargetClass() == PlayerEntity.class){
                    priority.set(prioritizedGoal.getPriority());
                    return true;
                }
            }
            return false;
        });

        if(priority.get() != 0){
            goalSelector.add(priority.get(),new ActiveTargetGoal<>(pathAwareEntity, PlayerEntity.class,true,livingEntity -> {
                PlayerEntity player = (PlayerEntity) livingEntity;
                return !IllagerHostileSensor.isHoldingOminousBanner(player);
            }).setMaxTimeWithoutVisibility(300));
        }
    }
}
