package net.firemuffin303.civilizedmobs;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.mixin.ActiveTargetGoalAccessor;
import net.firemuffin303.civilizedmobs.mixin.FleeEntityGoalAccessor;
import net.firemuffin303.civilizedmobs.mixin.MobAccessor;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.firemuffin303.civilizedmobs.registry.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.raid.Raid;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class CivilizedMobs implements ModInitializer {

    public static String MOD_ID = "civil_mobs";
    public static Logger LOGGER = LogUtils.getLogger();


    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.of(MOD_ID+".item_group"))
            .entries((displayContext, entries) -> {
                ModItems.ITEMS.forEach(entries::add);
            })
            .icon(() -> new ItemStack(Items.GOLD_INGOT)).build();

    @Override
    public void onInitialize() {

        Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID,"main"),ITEM_GROUP);

        ModEntityType.init();
        ModItems.init();

        TrackedDataHandlerRegistry.register(ModEntityType.WORKER_DATA);
        FabricDefaultAttributeRegistry.register(ModEntityType.CIVIL_PIGLIN, WorkerPiglinEntity.createAttribute());

        ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
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
                    ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
                    if(itemStack != null && itemStack.getNbt() != null){
                        return !itemStack.getNbt().equals(Raid.getOminousBanner().getNbt());
                    }
                    return true;
                }));
            }

            if(entity instanceof IllagerEntity || entity instanceof VexEntity){
                replaceTargetGoal((PathAwareEntity) entity);
            }

            if(entity instanceof RavagerEntity ravagerEntity){
                replaceTargetGoal(ravagerEntity);
            }
        });
    }

    private void replaceTargetGoal(PathAwareEntity pathAwareEntity){
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
                ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
                if(itemStack != null && itemStack.getNbt() != null){
                    return !itemStack.getNbt().equals(Raid.getOminousBanner().getNbt());
                }
                return true;
            }).setMaxTimeWithoutVisibility(300));
        }
    }

    public static boolean isHoldingOminousBanner(ItemStack itemStack){
        return !itemStack.isEmpty() && itemStack.hasNbt() && itemStack.getNbt().equals(Raid.getOminousBanner().getNbt());
    }
}
