package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.registry.RecipeGenerator;

public class ModRecipes {

    public static RecipeGenerator registerRecipes() {
        RecipeGenerator generator =  new RecipeGenerator($ -> {
            /**
             * TODO redo all these
            $.createShaped(new ItemStack(ModBlocks.ENERGY_FURNACE), null, new RecipeGenerator.ShapedParser("GFG", "PRP", "PCP", 'G', Blocks.GLASS, 'F', Blocks.FURNACE, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'C', ModBlocks.PHASE_CELL, 'R', Items.REDSTONE));
            $.createShaped(new ItemStack(ModBlocks.PULVERIZER), null, new RecipeGenerator.ShapedParser("PRP", "PRP", "FCF", 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'R', Items.REDSTONE, 'F', Items.FLINT, 'C', ModBlocks.PHASE_CELL));
            $.createShaped(new ItemStack(ModBlocks.QUARRY), null, new RecipeGenerator.ShapedParser("ORO", "PDP", "OCO", 'O', Blocks.OBSIDIAN, 'R', Items.REDSTONE, 'P', Items.DIAMOND_PICKAXE, 'C', ModBlocks.PHASE_CELL, 'D', Blocks.CRAFTING_TABLE));
            $.createShaped(new ItemStack(ModBlocks.CONVEYOR_BELT, 8), null, new RecipeGenerator.ShapedParser("LLL", "IRI", 'L', Items.LEATHER, 'I', Items.IRON_INGOT, 'R', Items.REDSTONE));
            $.createShaped(new ItemStack(ModBlocks.LAVA_GENERATOR), null, new RecipeGenerator.ShapedParser("PFP", "LCL", "PFP", 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'F', Blocks.FURNACE, 'L', Items.LAVA_BUCKET, 'C', ModBlocks.PHASE_CELL));
            $.createShaped(new ItemStack(ModBlocks.VACUUM), null, new RecipeGenerator.ShapedParser("HPH", "C C", "HPH", 'H', Blocks.HOPPER, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'C', Blocks.CHEST));
            $.createShaped(new ItemStack(ModBlocks.FLUID_PUMP), null, new RecipeGenerator.ShapedParser("BPB", "PCP", "BPB", 'B', Items.BUCKET, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'C', Blocks.CHEST));
            $.createShaped(new ItemStack(ModBlocks.MOB_GRINDER), null, new RecipeGenerator.ShapedParser("SCS", "DND", "SCS", 'S', Blocks.WITHER_SKELETON_SKULL, 'C', ModBlocks.PHASE_CELL, 'D', Items.DIAMOND_SWORD, 'N', Blocks.CHEST));
            $.createShaped(new ItemStack(ModBlocks.ENERGY_CHARGER), null, new RecipeGenerator.ShapedParser("GRG", "PCP", "GRG", 'G', Items.GOLD_INGOT, 'R', Items.REDSTONE, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'C', ModBlocks.PHASE_CELL));
            $.createShaped(new ItemStack(ModBlocks.FARMER), null, new RecipeGenerator.ShapedParser("PDP", "ACH", "PCP", 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'D', Items.DIAMOND, 'A', Items.IRON_AXE, 'C', Blocks.CHEST, 'H', Items.IRON_HOE, 'C', ModBlocks.PHASE_CELL));
            $.createShaped(new ItemStack(ModBlocks.PHASE_CELL), null, new RecipeGenerator.ShapedParser("PLP", "GRG", "PLP", 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'L', Items.LAPIS_LAZULI, 'G', Blocks.GLASS, 'R', Blocks.REDSTONE_BLOCK));
            $.createShaped(new ItemStack(EnumSolarPanelTypes.SOLAR_PANEL_MK1.getBlockSolar()), null, new RecipeGenerator.ShapedParser("DDD", "LCL", "PGP", 'D', Blocks.DAYLIGHT_DETECTOR, 'L', Items.LAPIS_LAZULI, 'C', ModBlocks.PHASE_CELL, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'G', Items.GLOWSTONE_DUST));
            $.createShaped(new ItemStack(EnumSolarPanelTypes.SOLAR_PANEL_MK2.getBlockSolar()), null, new RecipeGenerator.ShapedParser("SSS", "IGI", "PLP", 'S', EnumSolarPanelTypes.SOLAR_PANEL_MK1.getBlockSolar(), 'I', Items.IRON_INGOT, 'G', Blocks.GLOWSTONE, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'L', Items.LAPIS_LAZULI));
            $.createShaped(new ItemStack(EnumSolarPanelTypes.SOLAR_PANEL_MK3.getBlockSolar()), null, new RecipeGenerator.ShapedParser("SSS", "GDG", "PLP", 'S', EnumSolarPanelTypes.SOLAR_PANEL_MK2.getBlockSolar(), 'G', Items.GOLD_INGOT, 'D', Blocks.DIAMOND_BLOCK, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'L', Blocks.SEA_LANTERN));

            $.createShaped(new ItemStack(ModBlocks.WIRELESS_CONTROLLER), null, new RecipeGenerator.ShapedParser("GPG", "GPG", "GPG", 'G', Items.GOLD_INGOT, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot()));
            $.createShaped(new ItemStack(ModBlocks.WIRELESS_TRANSMITTER), null, new RecipeGenerator.ShapedParser("GPG", "GPG", "OOO", 'G', Items.GOLD_INGOT, 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'O', Blocks.OBSIDIAN));

            $.createShaped(new ItemStack(ModBlocks.GLASS_BLOCK, 8), null, new RecipeGenerator.ShapedParser("GGG", "GIG", "GGG", 'G', Blocks.GLASS, 'I', Items.BONE_MEAL));
            $.createShaped(new ItemStack(ModBlocks.BLACK_GLASS_BLOCK, 8), null, new RecipeGenerator.ShapedParser("GGG", "GIG", "GGG", 'G', Blocks.GLASS, 'I', Items.INK_SAC));

            $.createShaped(new ItemStack(ModItems.RECORDER), null, new RecipeGenerator.ShapedParser("PGP", "IBI", "PGP", 'P', EnumResourceItems.FLOW_INGOT.getItemIngot(), 'G', Items.GOLD_INGOT, 'I', Items.INK_SAC, 'B', Items.WRITABLE_BOOK));
            $.createShaped(new ItemStack(ModItems.LINKER), null, new RecipeGenerator.ShapedParser("P P", " P ", " P ", 'P', EnumResourceItems.FLOW_INGOT.getItemIngot()));
            $.createShapeless(new ItemStack(EnumResourceItems.FLOW_INGOT.getItemIngot(), 4), null, new RecipeGenerator.ShapelessParser(Items.IRON_INGOT, Items.ENDER_PEARL, Items.REDSTONE));
             */
        });

        generator.accept();
        return generator;
    }
}
