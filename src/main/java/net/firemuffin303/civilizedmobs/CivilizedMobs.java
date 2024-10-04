package net.firemuffin303.civilizedmobs;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.firemuffin303.civilizedmobs.common.entity.WorkerPiglinEntity;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;
import net.firemuffin303.civilizedmobs.registry.ModItems;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

public class CivilizedMobs implements ModInitializer {

    public static String MOD_ID = "civil_mobs";
    public static Logger LOGGER = LogUtils.getLogger();


    private static ItemGroup ITEM_GROUP = FabricItemGroup.builder()
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
    }
}
