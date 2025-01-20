package net.firemuffin303.villagefoe.common.entity;

import com.google.common.collect.Sets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.*;
import java.util.function.BiPredicate;

public interface WorkerContainer {
    void setWorkerData(WorkerData workerData);

    WorkerData getWorkerData();

    default void playWorkSound(LivingEntity entity){
        SoundEvent soundEvent = this.getWorkerData().getProfession().workSound();
        if(soundEvent != null){
            entity.playSound(soundEvent,1.0f,entity.getSoundPitch());
        }
    }

    boolean shouldRestock();

    void restock();

    void fillTrade();

    int getReputation(PlayerEntity player);

    TradeOfferList getMerchantOffers();

    default void fillRecipesFromPool(LivingEntity livingEntity, TradeOfferList recipeList, List<TradeOffers.Factory> list, int count) {
        Set<Integer> set = Sets.newHashSet();
        if (list.size() > count) {
            while(set.size() < count) {
                set.add(livingEntity.getRandom().nextInt(list.size()));
            }
        } else {
            for(int i = 0; i < list.size(); ++i) {
                set.add(i);
            }
        }

        for (Integer integer : set) {
            TradeOffer tradeOffer = list.get(integer).create(livingEntity,livingEntity.getRandom());
            if (tradeOffer != null) {
                recipeList.add(tradeOffer);
            }
        }

    }

    default void prepareOffersFor(PlayerEntity player, StatusEffect statusEffect) {
        int i = this.getReputation(player);
        if (i != 0) {
            for (TradeOffer tradeOffer : this.getMerchantOffers()) {
                tradeOffer.increaseSpecialPrice(-MathHelper.floor((float) i * tradeOffer.getPriceMultiplier()));
            }
        }

        if (player.hasStatusEffect(statusEffect)) {
            StatusEffectInstance statusEffectInstance = player.getStatusEffect(statusEffect);
            int j = statusEffectInstance.getAmplifier();

            for (TradeOffer tradeOffer2 : this.getMerchantOffers()) {
                double d = 0.3 + 0.0625 * (double) j;
                int k = (int) Math.floor(d * (double) tradeOffer2.getOriginalFirstBuyItem().getCount());
                tradeOffer2.increaseSpecialPrice(-Math.max(k, 1));
            }
        }
    }

    default <T extends LivingEntity> void releaseTicketFor(T entity, MemoryModuleType<GlobalPos> pos, Map<MemoryModuleType<GlobalPos>, BiPredicate<T, RegistryEntry<PointOfInterestType>>> pointOfInterest) {
        if (entity.getWorld() instanceof ServerWorld) {
            MinecraftServer minecraftServer = ((ServerWorld)entity.getWorld()).getServer();
            entity.getBrain().getOptionalRegisteredMemory(pos).ifPresent((posx) -> {
                ServerWorld serverWorld = minecraftServer.getWorld(posx.getDimension());
                if (serverWorld != null) {
                    PointOfInterestStorage pointOfInterestStorage = serverWorld.getPointOfInterestStorage();
                    Optional<RegistryEntry<PointOfInterestType>> optional = pointOfInterestStorage.getType(posx.getPos());
                    BiPredicate<T, RegistryEntry<PointOfInterestType>> biPredicate = pointOfInterest.get(pos);
                    if (optional.isPresent() && biPredicate.test(entity,optional.get())) {
                        pointOfInterestStorage.releaseTicket(posx.getPos());
                        DebugInfoSender.sendPointOfInterest(serverWorld, posx.getPos());
                    }

                }
            });
        }
    }



    void reinitializeBrain(ServerWorld world);
}
