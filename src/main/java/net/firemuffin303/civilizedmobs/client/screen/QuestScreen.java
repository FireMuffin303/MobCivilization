package net.firemuffin303.civilizedmobs.client.screen;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.quest.Quest;
import net.firemuffin303.civilizedmobs.common.quest.QuestList;
import net.firemuffin303.civilizedmobs.common.screen.QuestScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class QuestScreen extends HandledScreen<QuestScreenHandler> {
    private static final Text SEPARATOR_TEXT = Text.literal(" - ");
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/villager2.png");
    private int selectedIndex = -1;
    private final QuestButtonWidget[] questButtonWidgets = new QuestButtonWidget[7];
    private DoneButtonWidget doneButtonWidget;

    public QuestScreen(QuestScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 276;
        this.playerInventoryTitleX = 107;
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        int k = j + 16 + 2;
        for(int index =0; index < 7; ++index){
            this.questButtonWidgets[index] = this.addDrawableChild(new QuestButtonWidget(i + 5, k, index, button -> {
                if (button instanceof QuestButtonWidget questButtonWidget) {
                    this.selectedIndex = questButtonWidget.index;
                }
            }));
            k += 20;
        }

        this.doneButtonWidget = this.addDrawable(new DoneButtonWidget(i +20,j+10,buttonWidget -> {
            if(buttonWidget instanceof DoneButtonWidget){
                Objects.requireNonNull(this.client);
                Objects.requireNonNull(this.client.player);

                PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                packetByteBuf.writeInt(this.client.player.currentScreenHandler.syncId);
                packetByteBuf.writeInt(this.selectedIndex);
                ClientPlayNetworking.send(CivilizedMobs.UPDATE_QUEST_C2S_PAYLOAD_ID,packetByteBuf);
            }
        }));

    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 512, 256);
    }


    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        int level = this.handler.getLevel();
        if(level <= 5){
            Text text = this.title.copy().append(SEPARATOR_TEXT).append(Text.translatable("quest.level."+level));
            int textWidth = this.textRenderer.getWidth(text);
            int k = 49 + this.backgroundWidth / 2 - textWidth / 2;
            context.drawText(this.textRenderer,text,k,6,4210752,false);
        }

        context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 4210752, false);
        super.drawForeground(context, mouseX, mouseY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        int xpProgress = this.handler.getXpProgress();
        int levelProgress = this.handler.getLevel();
        if(levelProgress < 5){
            context.drawTexture(TEXTURE, x + 136, y + 16, 0, 0.0F, 186.0F, 102, 5, 512, 256);
            /*if (xpProgress > 0){
                context.drawTexture(TEXTURE,);
            }

             */
        }



        QuestList questList = this.handler.getQuestList();
        if(questList != null && !questList.isEmpty()){
            int i = (this.width - this.backgroundWidth) / 2;
            int j = (this.height - this.backgroundHeight) / 2;
            int k = j + 16 + 1;
            int l = i + 5 + 5;
            for(Quest quest : questList){
                ItemStack itemStack = quest.firstQuestItem();
                ItemStack itemStack2 = quest.secondQuestItem();
                context.getMatrices().push();
                context.getMatrices().translate(0.0f,0.0f,100.0f);
                context.drawItemWithoutEntity(itemStack,l,k+2);
                context.drawItemWithoutEntity(itemStack2,l+5,k+2);
                context.getMatrices().pop();
                k +=20;

            }

            for(QuestButtonWidget questButtonWidget : this.questButtonWidgets){
                questButtonWidget.visible = questButtonWidget.index < questList.size();
            }
        }

        this.doneButtonWidget.active = this.selectedIndex != 1;



        this.drawMouseoverTooltip(context,mouseX,mouseY);
    }

    static class QuestButtonWidget extends ButtonWidget{
        final int index;

        protected QuestButtonWidget(int x, int y,int index,PressAction onPress) {
            super(x, y, 88, 20, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    static class DoneButtonWidget extends ButtonWidget{

        protected DoneButtonWidget(int x, int y,PressAction onPress) {
            super(x, y, 22, 22, ScreenTexts.DONE, onPress, DEFAULT_NARRATION_SUPPLIER);
        }
    }

}
