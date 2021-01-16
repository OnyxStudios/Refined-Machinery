package dev.onyxstudios.refinedmachinery.data;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.registry.ModBlocks;
import dev.onyxstudios.refinedmachinery.registry.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, RefinedMachinery.MODID, existingFileHelper);
    }

    @Override
    public void registerTags() {
        getOrCreateBuilder(ModTags.COPPER_BLOCK).add(ModBlocks.COPPER.getBlockObject().get());
        getOrCreateBuilder(ModTags.SILVER_BLOCK).add(ModBlocks.SILVER.getBlockObject().get());
        getOrCreateBuilder(ModTags.TIN_BLOCK).add(ModBlocks.TIN.getBlockObject().get());
        getOrCreateBuilder(ModTags.BRONZE_BLOCK).add(ModBlocks.BRONZE.getBlockObject().get());
        getOrCreateBuilder(ModTags.STEEL_BLOCK).add(ModBlocks.STEEL.getBlockObject().get());
        getOrCreateBuilder(ModTags.URANIUM_BLOCK).add(ModBlocks.URANIUM.getBlockObject().get());
        getOrCreateBuilder(ModTags.PLATINUM_BLOCK).add(ModBlocks.PLATINUM.getBlockObject().get());

        getOrCreateBuilder(ModTags.COPPER_ORE).add(ModBlocks.COPPER.getOreObject().get());
        getOrCreateBuilder(ModTags.SILVER_ORE).add(ModBlocks.SILVER.getOreObject().get());
        getOrCreateBuilder(ModTags.TIN_ORE).add(ModBlocks.TIN.getOreObject().get());
        getOrCreateBuilder(ModTags.URANIUM_ORE).add(ModBlocks.URANIUM.getOreObject().get());
        getOrCreateBuilder(ModTags.PLATINUM_ORE).add(ModBlocks.PLATINUM.getOreObject().get());
    }
}
