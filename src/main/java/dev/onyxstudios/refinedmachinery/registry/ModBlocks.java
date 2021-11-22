package dev.onyxstudios.refinedmachinery.registry;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.blocks.generators.CoalGeneratorBlock;
import dev.onyxstudios.refinedmachinery.blocks.generators.GeneratorBlock;
import dev.onyxstudios.refinedmachinery.blocks.generators.GeothermalGenBlock;
import dev.onyxstudios.refinedmachinery.blocks.generators.WindTurbineBlock;
import dev.onyxstudios.refinedmachinery.items.WindTurbineItem;
import dev.onyxstudios.refinedmachinery.registry.resource.Resource;
import dev.onyxstudios.refinedmachinery.registry.resource.ResourceBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static final DeferredRegister<Block> blockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, RefinedMachinery.MODID);
    public static final DeferredRegister<Item> itemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, RefinedMachinery.MODID);

    public static RegistryObject<GeneratorBlock> coalGenObject = blockRegistry.register("coal_generator", CoalGeneratorBlock::new);
    public static RegistryObject<Item> coalGenItemObject = itemRegistry.register("coal_generator", () -> new BlockItem(coalGenObject.get(), new Item.Properties().group(RefinedMachinery.TAB)));

    public static RegistryObject<WindTurbineBlock> windTurbineObject = blockRegistry.register("wind_turbine", WindTurbineBlock::new);
    public static RegistryObject<Item> windTurbineItemObject = itemRegistry.register("wind_turbine", WindTurbineItem::new);

    public static RegistryObject<GeothermalGenBlock> geothermalGenObject = blockRegistry.register("geothermal_generator", GeothermalGenBlock::new);
    public static RegistryObject<Item> geothermalGenItemObject = itemRegistry.register("geothermal_generator", () -> new BlockItem(geothermalGenObject.get(), new Item.Properties().group(RefinedMachinery.TAB)));

    public static Resource COPPER = new ResourceBuilder("copper").setColor(0xFFB32A).setHardness(3).setHarvestLevel(1).addIngot().addDust().addNugget().addOre().addResourceBlock().build(blockRegistry, itemRegistry);
    public static Resource SILVER = new ResourceBuilder("silver").setColor(0xDEEFEE).setHardness(3).setHarvestLevel(2).addIngot().addDust().addNugget().addOre().addResourceBlock().build(blockRegistry, itemRegistry);
    public static Resource TIN = new ResourceBuilder("tin").setColor(0xEEF3F3).setHardness(1.5f).setHarvestLevel(1).addIngot().addDust().addNugget().addOre().addResourceBlock().build(blockRegistry, itemRegistry);
    public static Resource BRONZE = new ResourceBuilder("bronze").setColor(0xf5b15d).setHardness(2).setHarvestLevel(2).addIngot().addNugget().addDust().addResourceBlock().build(blockRegistry, itemRegistry);
    public static Resource STEEL = new ResourceBuilder("steel").setColor(0x757575).setHardness(3).setHarvestLevel(2).addIngot().addNugget().addDust().addResourceBlock().build(blockRegistry, itemRegistry);
    public static Resource URANIUM = new ResourceBuilder("uranium").setColor(0x78C283).setItemTexture(new ResourceLocation(RefinedMachinery.MODID, "item/uranium")).setHardness(3).setHarvestLevel(3).addIngot().addOre().addResourceBlock().build(blockRegistry, itemRegistry);
    public static Resource PLATINUM = new ResourceBuilder("platinum").setColor(0x52D9FF).setHardness(3).setHarvestLevel(3).addIngot().addDust().addNugget().addOre().addResourceBlock().build(blockRegistry, itemRegistry);
    public static Resource SILICON = new ResourceBuilder("silicon").addIngot().setItemTexture(new ResourceLocation(RefinedMachinery.MODID, "item/silicon")).build(blockRegistry, itemRegistry);

    //Vanilla Dusts
    public static Resource IRON = new ResourceBuilder("iron").setColor(0xFFE0BC).addDust().build(blockRegistry, itemRegistry);
    public static Resource GOLD = new ResourceBuilder("gold").setColor(0xFFDD54).addDust().build(blockRegistry, itemRegistry);
    public static Resource QUARTZ = new ResourceBuilder("quartz").setColor(0xE5DFD6).addDust().build(blockRegistry, itemRegistry);
}
