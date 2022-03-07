package dev.onyxstudios.refinedmachinery.registry;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.client.gui.CoalGenScreen;
import dev.onyxstudios.refinedmachinery.client.gui.LavaGenScreen;
import dev.onyxstudios.refinedmachinery.client.gui.WindTurbineScreen;
import dev.onyxstudios.refinedmachinery.client.models.machine.MachineModelLoader;
import dev.onyxstudios.refinedmachinery.client.render.WindTurbineRenderer;
import dev.onyxstudios.refinedmachinery.registry.resource.Resource;
import dev.onyxstudios.refinedmachinery.registry.resource.ResourceBuilder;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModRenders {

    private static final ResourceLocation INPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/input_config");
    private static final ResourceLocation OUTPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/output_overlay");
    private static final ResourceLocation INPUT_OUTPUT_OVERLAY = new ResourceLocation(RefinedMachinery.MODID, "block/input_output_overlay");
    private static final ResourceLocation WIND_TURBINE_ROTORS = new ResourceLocation(RefinedMachinery.MODID, "block/wind_turbine/wind_turbine_rotors");

    public static IBakedModel INPUT_MODEL;
    public static IBakedModel OUTPUT_MODEL;
    public static IBakedModel INPUT_OUTPUT_MODEL;
    public static IBakedModel TURBINE_ROTORS_MODEL;

    public static void register(FMLClientSetupEvent event) {
        Minecraft minecraft = event.getMinecraftSupplier().get();
        for (Resource resource : ResourceBuilder.resourceList) {
            if(resource.isIngotValid() && resource.getItemTexture().equals(ResourceBuilder.BASE_INGOT_TEXTURE))
                minecraft.getItemColors().register((itemStack, tintIndex) -> resource.getColor(), resource.getIngotObject().get());

            if(resource.isDustValid())
                minecraft.getItemColors().register((itemStack, tintIndex) -> resource.getColor(), resource.getDustObject().get());

            if(resource.isNuggetValid())
                minecraft.getItemColors().register((itemStack, tintIndex) -> resource.getColor(), resource.getNuggetObject().get());

            if(resource.isOreValid()) {
                minecraft.getBlockColors().register((state, displayReader, pos, tintIndex) -> tintIndex == 1 ? resource.getColor() : 0xFFFFFF, resource.getOreObject().get());
                minecraft.getItemColors().register((itemStack, tintIndex) -> tintIndex == 1 ? resource.getColor() : 0xFFFFFF, resource.getOreItemObject().get());
            }

            if(resource.isBlockValid()) {
                minecraft.getBlockColors().register((state, displayReader, pos, tintIndex) -> resource.getColor(), resource.getBlockObject().get());
                minecraft.getItemColors().register((itemStack, tintIndex) -> resource.getColor(), resource.getBlockItemObject().get());
            }
        }
    }

    public static void registerScreens() {
        ScreenManager.registerFactory(ModEntities.coalGenContainerType.get(), CoalGenScreen::new);
        ScreenManager.registerFactory(ModEntities.turbineContainerType.get(), WindTurbineScreen::new);
        ScreenManager.registerFactory(ModEntities.lavaGenContainerType.get(), LavaGenScreen::new);
    }

    public static void registerRenderLayers() {
        RenderTypeLookup.setRenderLayer(ModBlocks.coalGenObject.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.windTurbineObject.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.geothermalGenObject.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.lavaGenObject.get(), RenderType.getCutout());
        for (Resource resource : ResourceBuilder.resourceList) {
            if(resource.isOreValid())
                RenderTypeLookup.setRenderLayer(resource.getOreObject().get(), RenderType.getCutout());

            if(resource.isBlockValid())
                RenderTypeLookup.setRenderLayer(resource.getBlockObject().get(), RenderType.getCutout());
        }

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
