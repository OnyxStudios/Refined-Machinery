package dev.onyxstudios.refinedmachinery.registry;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.blocks.generators.CoalGeneratorBlock;
import dev.onyxstudios.refinedmachinery.blocks.generators.GeneratorBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static final DeferredRegister<Block> blockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, RefinedMachinery.MODID);
    public static final DeferredRegister<Item> itemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, RefinedMachinery.MODID);

    public static RegistryObject<GeneratorBlock> coalGenObject = blockRegistry.register("coal_generator", () -> new CoalGeneratorBlock());
    public static RegistryObject<Item> coalGenItemObject = itemRegistry.register("coal_generator", () -> new BlockItem(coalGenObject.get(), new Item.Properties().group(RefinedMachinery.TAB)));
}
