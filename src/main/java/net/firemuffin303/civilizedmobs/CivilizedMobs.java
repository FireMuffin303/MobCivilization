package net.firemuffin303.civilizedmobs;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.firemuffin303.civilizedmobs.common.entity.piglin.quest.PiglinQuestEntity;
import net.firemuffin303.civilizedmobs.common.quest.QuestPool;
import net.firemuffin303.civilizedmobs.common.quest.QuestPoolTypes;
import net.firemuffin303.civilizedmobs.common.entity.piglin.worker.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.common.event.ModServerEntityEvents;
import net.firemuffin303.civilizedmobs.registry.ModBrains;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.firemuffin303.civilizedmobs.registry.ModItems;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.raid.Raid;
import org.slf4j.Logger;

public class CivilizedMobs implements ModInitializer {

    public static String MOD_ID = "civil_mobs";
    public static Logger LOGGER = LogUtils.getLogger();


    public static final RegistryKey<Registry<QuestPool>> QUEST_POOL = RegistryKey.ofRegistry(new Identifier(MOD_ID,"quest_pool"));
    //public static final Registry<QuestPool> QUEST_POOL_REGISTRY = FabricRegistryBuilder.createSimple(QUEST_POOL).attribute(RegistryAttribute.MODDED).buildAndRegister();

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.of(MOD_ID+".item_group"))
            .entries((displayContext, entries) -> {
                ModItems.ITEMS.forEach(entries::add);
            })
            .icon(() -> new ItemStack(Items.GOLD_INGOT)).build();

    @Override
    public void onInitialize() {
        DynamicRegistries.register(QUEST_POOL,QuestPool.CODEC);

        Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID,"main"),ITEM_GROUP);

        ModBrains.init();
        ModEntityType.init();
        ModItems.init();
        QuestPoolTypes.init();


        TrackedDataHandlerRegistry.register(ModEntityType.WORKER_DATA);
        FabricDefaultAttributeRegistry.register(ModEntityType.CIVIL_PIGLIN, WorkerPiglinEntity.createAttribute());
        FabricDefaultAttributeRegistry.register(ModEntityType.PIGLIN_QUEST_ENTITY, PiglinQuestEntity.createAttribute());

        ServerEntityEvents.ENTITY_LOAD.register(ModServerEntityEvents::IllagerLoaded);
    }



    public static boolean isHoldingOminousBanner(ItemStack itemStack){
        return !itemStack.isEmpty() && itemStack.hasNbt() && itemStack.getNbt().equals(Raid.getOminousBanner().getNbt());
    }
}
