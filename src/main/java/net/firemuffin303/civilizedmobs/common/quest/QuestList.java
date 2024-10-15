package net.firemuffin303.civilizedmobs.common.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class QuestList extends ArrayList<Quest> {
    public static final Codec<QuestList> CODEC = RecordCodecBuilder.create(questListInstance -> questListInstance.group(
      Quest.CODEC.listOf().fieldOf("questList").forGetter(QuestList::new)
    ).apply(questListInstance,QuestList::new));


    public QuestList(){}

    public QuestList(int size){
        super(size);
    }

    public QuestList(NbtList nbtList){
        for(int i = 0 ; i < nbtList.size();++i){
            this.add(Quest.fromNbt(nbtList.getCompound(i)));
        }
    }

    public QuestList(List<Quest> quests) {
        this.addAll(quests);
    }


    public void toPacket(PacketByteBuf buf){
        buf.writeCollection(this,(buf1, quest) -> {
            buf1.writeItemStack(quest.firstQuestItem());
            buf1.writeItemStack(quest.secondQuestItem());
            buf1.writeInt(quest.time());
            buf1.writeInt(quest.experience());
        });
    }

    public static QuestList fromPacket(PacketByteBuf packetByteBuf){
        return  packetByteBuf.readCollection(QuestList::new,buf2 -> {
           ItemStack firstItem = buf2.readItemStack();
           ItemStack secondItem = buf2.readItemStack();
           int time = buf2.readInt();
           int experience = buf2.readInt();
           return new Quest(firstItem,secondItem,time,experience);
        });
    }

    public NbtCompound toNbt(){
        NbtCompound nbtCompound = new NbtCompound();
        NbtList nbtList = new NbtList();
        for(Quest quest : this){
            nbtList.add(quest.toNbt());
        }
        nbtCompound.put("QuestList",nbtList);
        return nbtCompound;
    }
}
