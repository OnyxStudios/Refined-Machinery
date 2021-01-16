package dev.onyxstudios.refinedmachinery.data;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = RefinedMachinery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if(event.includeServer()) {
            BlockTagProvider blockTagProvider = new BlockTagProvider(event.getGenerator(), event.getExistingFileHelper());
            event.getGenerator().addProvider(blockTagProvider);
            event.getGenerator().addProvider(new ItemTagProvider(event.getGenerator(), blockTagProvider, event.getExistingFileHelper()));
            event.getGenerator().addProvider(new RecipeGenerator(event.getGenerator()));
        }

        if(event.includeClient()) {
            event.getGenerator().addProvider(new ItemModels(event.getGenerator(), event.getExistingFileHelper()));
            event.getGenerator().addProvider(new BlockModels(event.getGenerator(), event.getExistingFileHelper()));
        }
    }
}
