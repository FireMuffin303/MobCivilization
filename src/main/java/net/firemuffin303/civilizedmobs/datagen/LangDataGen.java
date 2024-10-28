package net.firemuffin303.civilizedmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.firemuffin303.civilizedmobs.registry.ModEntityType;

public class LangDataGen extends FabricLanguageProvider {
    protected LangDataGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModEntityType.PIGLIN_QUEST_ENTITY,"Quest Entity");
        translationBuilder.add(ModEntityType.CIVIL_PIGLIN,"Piglin Worker");
        translationBuilder.add("quest.level.0","Level 1");
        translationBuilder.add("quest.level.1","Level 2");
        translationBuilder.add("quest.level.2","Level 3");
        translationBuilder.add("quest.level.3","Level 4");
        translationBuilder.add("quest.level.4","Level 5");
        translationBuilder.add("command.civil_mobs.quest.clear.success","Removed Quest from {}");
    }
}
