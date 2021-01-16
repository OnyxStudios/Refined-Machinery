package dev.onyxstudios.refinedmachinery;

import dev.onyxstudios.refinedmachinery.client.shaders.Shaders;
import dev.onyxstudios.refinedmachinery.network.ModPackets;
import dev.onyxstudios.refinedmachinery.registry.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("refinedmachinery")
public class RefinedMachinery {

    public static final Logger LOGGER = LogManager.getLogger(RefinedMachinery.class);
    public static final String MODID = "refinedmachinery";
    public static ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.coalGenObject.get());
        }
    };

    public RefinedMachinery() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModRenders::onModelBake);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModRenders::onModelRegistry);

        ModBlocks.blockRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.itemRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.itemRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModEntities.tileRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModEntities.containersRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModSounds.soundRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void init(FMLCommonSetupEvent event) {
        ModPackets.registerPackets();
    }

    private void initClient(FMLClientSetupEvent event) {
        ModRenders.register(event);
        ModRenders.registerScreens();
        ModRenders.registerRenderLayers();
        event.getMinecraftSupplier().get().runAsync(() -> Shaders.init());
    }
}
