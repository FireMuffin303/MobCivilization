package net.firemuffin303.civilizedmobs.registry;

import net.firemuffin303.civilizedmobs.CivilizedMobs;
import net.firemuffin303.civilizedmobs.common.screen.QuestScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerType {
    public static final ScreenHandlerType<QuestScreenHandler> QUEST_SCREEN =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier(CivilizedMobs.MOD_ID,"quest"),new ScreenHandlerType<>(QuestScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
}
