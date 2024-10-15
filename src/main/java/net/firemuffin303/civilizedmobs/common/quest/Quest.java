package net.firemuffin303.civilizedmobs.common.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public record Quest(ItemStack firstQuestItem,ItemStack secondQuestItem,int time,int experience) {

    public static final Codec<Quest> CODEC = RecordCodecBuilder.create(questInstance -> questInstance.group(
            ItemStack.CODEC.fieldOf("first_item").forGetter(Quest::firstQuestItem),
            ItemStack.CODEC.fieldOf("second_item").forGetter(Quest::secondQuestItem),
            Codec.INT.fieldOf("time").forGetter(Quest::time),
            Codec.INT.fieldOf("experience").forGetter(Quest::experience)
    ).apply(questInstance,Quest::new));


    public Quest(ItemStack firstQuestItem,ItemStack secondQuestItem,int time,int experience){
        this.firstQuestItem = firstQuestItem;
        this.secondQuestItem = secondQuestItem;
        this.time = time;
        this.experience = experience;
    }

    public static Quest fromNbt(NbtCompound compound) {
        ItemStack firstQuestItem = ItemStack.fromNbt(compound.getCompound("firstItem"));
        ItemStack secondQuestItem = ItemStack.fromNbt(compound.getCompound("secondItem"));
        int time = compound.getInt("time");
        int experience = compound.getInt("experience");
        return new Quest(firstQuestItem,secondQuestItem,time,experience);
    }

    public NbtCompound toNbt(){
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.put("firstItem",this.firstQuestItem.writeNbt(new NbtCompound()));
        nbtCompound.put("secondItem",this.secondQuestItem.writeNbt(new NbtCompound()));
        nbtCompound.putInt("time",this.time);
        nbtCompound.putInt("experience",this.experience);
        return nbtCompound;
    }

}
