package net.firemuffin303.civilizedmobs.common.entity;

import com.google.common.collect.Sets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
}
