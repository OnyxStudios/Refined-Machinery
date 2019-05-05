package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.registry.RecipeGenerator;
import abused_master.refinedmachinery.blocks.BlockResources;
import abused_master.refinedmachinery.blocks.generators.EnumSolarPanelTypes;
import abused_master.refinedmachinery.blocks.tanks.EnumTankTypes;
import abused_master.refinedmachinery.items.EnumResourceItems;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;

public class ModRecipes {

    public static RecipeGenerator registerRecipes() {
        RecipeGenerator generator =  new RecipeGenerator($ -> {
            //Smelting recipes
            $.createBlasting(new ItemStack(ModItems.STEEL_INGOT), null, Ingredient.ofItems(Items.IRON_INGOT), 5, 1200);
            $.createFurnace(new ItemStack(EnumResourceItems.COPPER_INGOT.getItemIngot()), null, Ingredient.ofItems(BlockResources.EnumResourceOres.COPPER_ORE.getBlockOres()), 1.5f, 200);
            $.createFurnace(new ItemStack(EnumResourceItems.LEAD_INGOT.getItemIngot()), null, Ingredient.ofItems(BlockResources.EnumResourceOres.LEAD_ORE.getBlockOres()), 1.5f, 200);
            $.createFurnace(new ItemStack(EnumResourceItems.TIN_INGOT.getItemIngot()), null, Ingredient.ofItems(BlockResources.EnumResourceOres.TIN_ORE.getBlockOres()), 1.5f, 200);
            $.createFurnace(new ItemStack(EnumResourceItems.SILVER_INGOT.getItemIngot()), null, Ingredient.ofItems(BlockResources.EnumResourceOres.SILVER_ORE.getBlockOres()), 1.5f, 200);
            $.createFurnace(new ItemStack(EnumResourceItems.NICKEL_INGOT.getItemIngot()), null, Ingredient.ofItems(BlockResources.EnumResourceOres.NICKEL_ORE.getBlockOres()), 1.5f, 200);

            //Crafting Recipes
            $.createShaped(new ItemStack(ModBlocks.MACHINE_FRAME), null, new RecipeGenerator.ShapedParser("III", "IDI", "III", 'I', Items.IRON_INGOT, 'D', Items.DIAMOND));
            $.createShaped(new ItemStack(ModBlocks.PULVERIZER), null, new RecipeGenerator.ShapedParser("SFS", "SMS", "SFS", 'S', RMTags.STEEL_INGOT, 'F', Items.FLINT, 'M', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(ModBlocks.ENERGY_FURNACE), null, new RecipeGenerator.ShapedParser("SDS", "DFD", "SDS", 'S', RMTags.STEEL_INGOT, 'D', RMTags.DIAMOND_DUST, 'F', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(ModBlocks.ENERGY_CHARGER), null, new RecipeGenerator.ShapedParser("LGL", "GMG", "LGL", 'L', RMTags.LEAD_INGOT, 'G', Items.GOLD_INGOT, 'M', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(ModBlocks.FLUID_PUMP), null, new RecipeGenerator.ShapedParser("CBC", "CMC", "CBC", 'C', RMTags.COPPER_INGOT, 'B', Items.BUCKET, 'M', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(ModBlocks.LAVA_GENERATOR), null, new RecipeGenerator.ShapedParser("NCN", "GMG", "NCN", 'N', RMTags.NICKEL_INGOT, 'C', RMTags.COPPER_INGOT, 'G', Items.GOLD_INGOT, 'M', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(ModItems.WRENCH), null, new RecipeGenerator.ShapedParser(" T ", " IT", "I  ", 'T', RMTags.SILVER_INGOT, 'I', Items.IRON_INGOT));
            $.createShaped(new ItemStack(ModItems.ENERGIZED_SWORD), null, new RecipeGenerator.ShapedParser("  E", " E ", "R  ", 'E', EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot(), 'R', RMTags.SILVER_INGOT));
            $.createShaped(new ItemStack(ModBlocks.PHASE_CELL), null, new RecipeGenerator.ShapedParser("SES", "GMG", "SES", 'S', RMTags.STEEL_INGOT, 'E', EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot(), 'G', RMTags.GLASS, 'M', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(ModBlocks.ENERGY_CABLE, 8), null, new RecipeGenerator.ShapedParser("RL ", "LGL", " LR", 'L', RMTags.LEAD_INGOT, 'G', RMTags.GLASS, 'R', Items.REDSTONE));
            $.createShaped(new ItemStack(ModBlocks.FARMER), null, new RecipeGenerator.ShapedParser("SCS", "WMW", "SCS", 'S', RMTags.STEEL_INGOT, 'C', Items.SUGAR_CANE, 'W', Items.WHEAT, 'M', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(ModBlocks.MOB_GRINDER), null, new RecipeGenerator.ShapedParser("SFS", "EMB", "SNS", 'S', EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot(), 'F', Items.ROTTEN_FLESH, 'E', Items.SPIDER_EYE, 'M', ModBlocks.MACHINE_FRAME, 'B', Items.BONE, 'N', Items.NETHER_STAR));
            $.createShaped(new ItemStack(ModBlocks.VACUUM), null, new RecipeGenerator.ShapedParser("SES", "OMO", "SES", 'S', RMTags.STEEL_INGOT, 'E', Items.ENDER_PEARL, 'M', ModBlocks.MACHINE_FRAME, 'O', Blocks.OBSIDIAN));
            $.createShaped(new ItemStack(ModBlocks.CONVEYOR_BELT, 8), null, new RecipeGenerator.ShapedParser("SSS", "TBT", "   ", 'S', Items.SUGAR, 'T', RMTags.TIN_INGOT, 'B', Items.SLIME_BALL));
            $.createShaped(new ItemStack(ModBlocks.COAL_GENERATOR), null, new RecipeGenerator.ShapedParser("SCS", "CMC", "ScS", 'S', RMTags.STEEL_INGOT, 'C', ItemTags.COALS, 'M', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(EnumSolarPanelTypes.SOLAR_PANEL_MK1.getBlockSolar()), null, new RecipeGenerator.ShapedParser("GGG", "LML", "LSL", 'G', RMTags.GLASS, 'L', Items.GOLD_INGOT, 'M', ModBlocks.MACHINE_FRAME, 'S', RMTags.STEEL_INGOT));
            $.createShaped(new ItemStack(EnumSolarPanelTypes.SOLAR_PANEL_MK2.getBlockSolar()), null, new RecipeGenerator.ShapedParser("GGG", "LPL", "LSL", 'G', RMTags.GLASS, 'L', RMTags.LEAD_INGOT, 'P', EnumSolarPanelTypes.SOLAR_PANEL_MK1.getBlockSolar(), 'S', RMTags.STEEL_INGOT));
            $.createShaped(new ItemStack(EnumSolarPanelTypes.SOLAR_PANEL_MK3.getBlockSolar()), null, new RecipeGenerator.ShapedParser("GGG", "LPL", "LSL", 'G', RMTags.GLASS, 'L', Items.LAPIS_LAZULI, 'S', EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot(), 'P', EnumSolarPanelTypes.SOLAR_PANEL_MK2.getBlockSolar()));
            $.createShaped(new ItemStack(ModBlocks.DISENCHANTER), null, new RecipeGenerator.ShapedParser("SBS", "ETE", "SMS", 'S', RMTags.STEEL_INGOT, 'B', Items.BOOK, 'E', Items.EMERALD, 'M', ModBlocks.MACHINE_FRAME));
            $.createShaped(new ItemStack(EnumTankTypes.COPPER_TANK.getTank()), null, new RecipeGenerator.ShapedParser("IGI", "GBG", "IGI", 'I', RMTags.COPPER_INGOT, 'G', RMTags.GLASS, 'B', Items.BUCKET));
            $.createShaped(new ItemStack(EnumTankTypes.SILVER_TANK.getTank()), null, new RecipeGenerator.ShapedParser("IGI", "GBG", "IGI", 'I', RMTags.SILVER_INGOT, 'G', RMTags.GLASS, 'B', EnumTankTypes.COPPER_TANK.getTank()));
            $.createShaped(new ItemStack(EnumTankTypes.STEEL_TANK.getTank()), null, new RecipeGenerator.ShapedParser("IGI", "GBG", "IGI", 'I', RMTags.STEEL_INGOT, 'G', RMTags.GLASS, 'B', EnumTankTypes.SILVER_TANK.getTank()));
            $.createShaped(new ItemStack(ModItems.RECORDER), null, new RecipeGenerator.ShapedParser("RSR", "SLS", "RSR", 'S', RMTags.STEEL_INGOT, 'L', RMTags.LEAD_INGOT, 'R', Items.REDSTONE));
            $.createShaped(new ItemStack(ModBlocks.QUARRY), null, new RecipeGenerator.ShapedParser("SSS", "VFP", "SCS", 'S', RMTags.STEEL_INGOT, 'V', Items.DIAMOND_SHOVEL, 'F', ModBlocks.MACHINE_FRAME, 'P', Items.DIAMOND_PICKAXE, 'C', ModBlocks.PHASE_CELL));
            $.createShaped(new ItemStack(ModItems.EXCHANGER), null, new RecipeGenerator.ShapedParser("SDS", "EIE", "SDS", 'S', RMTags.STEEL_INGOT, 'D', Items.DIAMOND, 'E', Items.EMERALD, 'I', ModBlocks.MACHINE_FRAME));
        });

        generator.accept();
        return generator;
    }
}
