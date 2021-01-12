package dev.onyxstudios.refinedmachinery.registry;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.client.gui.CoalGenScreen;
import dev.onyxstudios.refinedmachinery.client.gui.WindTurbineScreen;
import dev.onyxstudios.refinedmachinery.client.models.MachineModelLoader;
import dev.onyxstudios.refinedmachinery.client.render.WindTurbineRenderer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenders {

    private static ResourceLocation INPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/input_config");
    private static ResourceLocation OUTPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/output_overlay");
    private static ResourceLocation INPUT_OUTPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/input_output_overlay");
    private static ResourceLocation WIND_TURBINE_ROTORS = new ResourceLocation(RefinedMachinery.MODID, "block/wind_turbine/wind_turbine_rotors");

    public static IBakedModel INPUT_MODEL;
    public static IBakedModel OUTPUT_MODEL;
    public static IBakedModel INPUT_OUTPUT_MODEL;
    public static IBakedModel TURBINE_ROTORS_MODEL;

    public static void registerScreens() {
        ScreenManager.registerFactory(ModEntities.coalGenContainerType.get(), CoalGenScreen::new);
        ScreenManager.registerFactory(ModEntities.turbineContainerType.get(), WindTurbineScreen::new);
    }

    public static void registerRenderLayers() {
        RenderTypeLookup.setRenderLayer(ModBlocks.coalGenObject.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.windTurbineObject.get(), RenderType.getCutout());

        ClientRegistry.bindTileEntityRenderer(ModEntities.windTurbineTileType.get(), WindTurbineRenderer::new);
    }

    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(RefinedMachinery.MODID, "machine"), MachineModelLoader.INSTANCE);
        ModelLoader.addSpecialModel(INPUT_OVERLAY);
        ModelLoader.addSpecialModel(OUTPUT_OVERLAY);
        ModelLoader.addSpecialModel(INPUT_OUTPUT_OVERLAY);
        ModelLoader.addSpecialModel(WIND_TURBINE_ROTORS);
    }

    public static void onModelBake(ModelBakeEvent event) {
        INPUT_MODEL = event.getModelManager().getModel(INPUT_OVERLAY);
        OUTPUT_MODEL = event.getModelManager().getModel(OUTPUT_OVERLAY);
        INPUT_OUTPUT_MODEL = event.getModelManager().getModel(INPUT_OUTPUT_OVERLAY);
        TURBINE_ROTORS_MODEL = event.getModelManager().getModel(WIND_TURBINE_ROTORS);
    }
}
