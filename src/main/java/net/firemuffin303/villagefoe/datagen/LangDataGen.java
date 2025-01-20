package net.firemuffin303.villagefoe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.firemuffin303.villagefoe.registry.ModEntityType;
import net.firemuffin303.villagefoe.registry.ModItems;

public class LangDataGen extends FabricLanguageProvider {
    protected LangDataGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemgroup.village_foe","Village Foe");
        translationBuilder.add(ModItems.PIGLIN_WORKER_SPAWN_EGG,"Piglin Worker Spawn Egg");
        translationBuilder.add(ModItems.PIGLIN_LEADER_SPAWN_EGG,"Piglin Leader Spawn Egg");
        translationBuilder.add(ModItems.PILLAGER_WORKER_SPAWN_EGG,"Pillager Worker Spawn Egg");
        translationBuilder.add(ModItems.PILLAGER_LEADER_SPAWN_EGG,"Pillager Leader Spawn Egg");
        translationBuilder.add(ModItems.WITHTER_SKELETON_WORKER_SPAWN_EGG,"Wither Skeleton Worker Spawn Egg");
        translationBuilder.add(ModItems.WITHTER_SKELETON_LEADER_SPAWN_EGG,"Wither Skeleton Leader Spawn Egg");
        translationBuilder.add(ModEntityType.PIGLIN_LEADER_ENTITY,"Piglin Leader");
        translationBuilder.add(ModEntityType.PIGLIN_WORKER,"Piglin Worker");
        translationBuilder.add(ModEntityType.PILLAGER_WORKER,"Pillager Worker");
        translationBuilder.add(ModEntityType.PILLAGER_LEADER,"Pillager Leader");
        translationBuilder.add(ModEntityType.WITHER_SKELETON_WORKER,"Wither Skeleton Worker");
        translationBuilder.add(ModEntityType.WITHER_SKELETON_LEADER,"Wither Skeleton Leader");
        translationBuilder.add("filled_map.fortress","Nether Fortress Map");
        translationBuilder.add("filled_map.bastion","Bastion Remnant Map");
        translationBuilder.add("filled_map.ancient_city","Ancient City Map");

        //modmenu
        translationBuilder.add("modmenu.descriptionTranslation.village_foe","Added More Village Type!");
        translationBuilder.add("modmenu.nameTranslation.village_foe","Village Foe");
        translationBuilder.add("modmenu.summaryTranslation.village_foe","Added More Village Type!");
    }

    public static class ThaiLangDataGen extends FabricLanguageProvider{

        protected ThaiLangDataGen(FabricDataOutput dataOutput) {
            super(dataOutput,"th_th");
        }

        @Override
        public void generateTranslations(TranslationBuilder translationBuilder) {
            translationBuilder.add("itemgroup.village_foe","Village Foe");
            translationBuilder.add(ModItems.PIGLIN_WORKER_SPAWN_EGG,"ไข่เกิดพิกลินงาน");
            translationBuilder.add(ModItems.PIGLIN_LEADER_SPAWN_EGG,"ไข่เกิดหัวหน้าพิกลิน");
            translationBuilder.add(ModItems.PILLAGER_WORKER_SPAWN_EGG,"ไข่เกิดกองโจรงาน");
            translationBuilder.add(ModItems.PILLAGER_LEADER_SPAWN_EGG,"ไข่เกิดหัวหน้ากองโจร");
            translationBuilder.add(ModItems.WITHTER_SKELETON_WORKER_SPAWN_EGG,"ไข่เกิดโครงกระดูกวิทเธอร์งาน");
            translationBuilder.add(ModItems.WITHTER_SKELETON_LEADER_SPAWN_EGG,"ไข่เกิดโครงหัวหน้ากระดูกวิทเธอร์");
            translationBuilder.add(ModEntityType.PIGLIN_LEADER_ENTITY,"หัวหน้าพิกลิน");
            translationBuilder.add(ModEntityType.PIGLIN_WORKER,"พิกลินงาน");
            translationBuilder.add(ModEntityType.PILLAGER_WORKER,"กองโจรงาน");
            translationBuilder.add(ModEntityType.PILLAGER_LEADER,"หัวหน้ากองโจร");
            translationBuilder.add(ModEntityType.WITHER_SKELETON_WORKER,"โครงกระดูกวิทเธอร์งาน");
            translationBuilder.add(ModEntityType.WITHER_SKELETON_LEADER,"หัวหน้ากระดูกวิทเธอร์");
            translationBuilder.add("filled_map.fortress","แผนที่ปราสาทเนเธอร์");
            translationBuilder.add("filled_map.bastion","แผนที่ป้อมปราการพิกลิน");
            translationBuilder.add("filled_map.ancient_city","แผนที่เมืองโบราณ");

            //modmenu
            translationBuilder.add("modmenu.descriptionTranslation.village_foe","เพิ่มรูปแบบหมู่บ้านให้กับมอนสเตอร์อื่น ๆ");
            translationBuilder.add("modmenu.nameTranslation.village_foe","Village Foe");
            translationBuilder.add("modmenu.summaryTranslation.village_foe","เพิ่มรูปแบบหมู่บ้านให้กับมอนสเตอร์อื่น ๆ");
        }
    }
}
