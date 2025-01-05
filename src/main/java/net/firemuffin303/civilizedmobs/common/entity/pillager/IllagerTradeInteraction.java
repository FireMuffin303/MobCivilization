package net.firemuffin303.civilizedmobs.common.entity.pillager;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class IllagerTradeInteraction {
    private PlayerEntity customer;
    private TradeOfferList offers;
    private final LivingEntity livingEntity;
    private final Int2ObjectMap<TradeOffers.Factory[]> trade;

    public IllagerTradeInteraction(LivingEntity livingEntity, Int2ObjectMap<TradeOffers.Factory[]> trade){
        this.livingEntity = livingEntity;
        this.trade = trade;
    }

    public void save(NbtCompound nbt) {
        TradeOfferList tradeOffers = this.getOffers();
        if(!tradeOffers.isEmpty()){
            nbt.put("Offers",tradeOffers.toNbt());
        }
    }

    public void load(NbtCompound nbt) {
        if(nbt.contains("Offers",10)){
            this.offers = new TradeOfferList(nbt.getCompound("Offers"));
        }
    }

    public void trade(TradeOffer offer){
        offer.use();
        if (offer.shouldRewardPlayerExperience()) {
            int i = 3 + this.livingEntity.getRandom().nextInt(4);
            this.livingEntity.getWorld().spawnEntity(new ExperienceOrbEntity(this.livingEntity.getWorld(), this.livingEntity.getX(), this.livingEntity.getY() + 0.5, this.livingEntity.getZ(), i));
        }
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    public @Nullable PlayerEntity getCustomer() {
        return customer;
    }

    public TradeOfferList getOffers() {
        if(this.offers == null){
            this.offers = new TradeOfferList();
            this.fillTrade();
        }
        return this.offers;
    }

    protected void fillTrade() {
        TradeOffers.Factory[] factorys = this.trade.get(1);
        TradeOffers.Factory[] factorys2 = this.trade.get(2);
        if (factorys != null && factorys2 != null) {
            TradeOfferList tradeOfferList = this.getOffers();
            this.fillRecipesFromPool(tradeOfferList, factorys, 5);
            int i = livingEntity.getRandom().nextInt(factorys2.length);
            TradeOffers.Factory factory = factorys2[i];
            TradeOffer tradeOffer = factory.create(livingEntity, livingEntity.getRandom());
            if (tradeOffer != null) {
                tradeOfferList.add(tradeOffer);
            }

        }
    }

    protected void fillRecipesFromPool(TradeOfferList recipeList, TradeOffers.Factory[] pool, int count) {
        Set<Integer> set = Sets.newHashSet();
        if (pool.length > count) {
            while(set.size() < count) {
                set.add(livingEntity.getRandom().nextInt(pool.length));
            }
        } else {
            for(int i = 0; i < pool.length; ++i) {
                set.add(i);
            }
        }

        for (Integer integer : set) {
            TradeOffers.Factory factory = pool[integer];
            TradeOffer tradeOffer = factory.create(livingEntity, livingEntity.getRandom());
            if (tradeOffer != null) {
                recipeList.add(tradeOffer);
            }
        }

    }

    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
    }
}
