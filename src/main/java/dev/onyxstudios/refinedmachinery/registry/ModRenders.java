package dev.onyxstudios.refinedmachinery.registry;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.client.gui.CoalGenScreen;
import dev.onyxstudios.refinedmachinery.client.models.MachineModelLoader;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ModRenders {

    public static ResourceLocation INPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/input_config");
    public static ResourceLocation OUTPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/output_overlay");
    public static ResourceLocation INPUT_OUTPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/input_output_overlay");

    public static IBakedModel INPUT_MODEL;
    public static IBakedModel OUTPUT_MODEL;
    public static IBakedModel INPUT_OUTPUT_MODEL;

    public static void registerScreens() {
        ScreenManager.registerFactory(ModEntities.coalGenContainerType.get(), CoalGenScreen::new);
    }

    public static void registerRenderLayers() {
        RenderTypeLookup.setRenderLayer(ModBlocks.coalGenObject.get(), RenderType.getCutout());
    }

    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(RefinedMachinery.MODID, "machine"), MachineModelLoader.INSTANCE);
        ModelLoader.addSpecialModel(INPUT_OVERLAY);
        ModelLoader.addSpecialModel(OUTPUT_OVERLAY);
        ModelLoader.addSpecialModel(INPUT_OUTPUT_OVERLAY);
    }

    public static void onModelBake(ModelBakeEvent event) {
        INPUT_MODEL = event.getModelManager().getModel(INPUT_OVERLAY);
        OUTPUT_MODEL = event.getModelManager().getModel(OUTPUT_OVERLAY);
        INPUT_OUTPUT_MODEL = event.getModelManager().getModel(INPUT_OUTPUT_OVERLAY);
    }
}
