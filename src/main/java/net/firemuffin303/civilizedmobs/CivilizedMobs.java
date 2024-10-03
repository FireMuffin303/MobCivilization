package net.firemuffin303.civilizedmobs;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.firemuffin303.civilizedmobs.common.entity.CivilizedPiglinEntity;
import net.firemuffin303.civilizedmobs.common.entity.CivilziedProfession;
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
import org.slf4j.Logger;

public class CivilizedMobs implements ModInitializer {

    public static String MOD_ID = "civil_mobs";
    public static Logger LOGGER = LogUtils.getLogger();

    public static final RegistryKey<Registry<CivilziedProfession>> CIVILIZED_PROFESSION = RegistryKey.ofRegistry(new Identifier(MOD_ID,"civilized_profession"));
    public static final Registry<CivilziedProfession> CIVILZIED_PROFESSIONS = FabricRegistryBuilder.createDefaulted(CIVILIZED_PROFESSION,new Identifier(MOD_ID,"none")).attribute(RegistryAttribute.MODDED).buildAndRegister();

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
        CivilziedProfession.init();

        //DynamicRegistries.register(CivilizedMobs.CIVILIZED_PROFESSION,CivilziedProfession.CODEC);
        TrackedDataHandlerRegistry.register(ModEntityType.CIVIL_PIGLIN_DATA);
        FabricDefaultAttributeRegistry.register(ModEntityType.CIVIL_PIGLIN, CivilizedPiglinEntity.createAttribute());
    }
}
