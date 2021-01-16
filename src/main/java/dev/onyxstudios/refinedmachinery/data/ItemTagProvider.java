package dev.onyxstudios.refinedmachinery.data;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.registry.ModBlocks;
import dev.onyxstudios.refinedmachinery.registry.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagProvider extends ItemTagsProvider {

    public ItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, RefinedMachinery.MODID, existingFileHelper);
    }

    @Override
    public void registerTags() {
        getOrCreateBuilder(ModTags.COPPER_DUST).add(ModBlocks.COPPER.getDustObject().get());
        getOrCreateBuilder(ModTags.SILVER_DUST).add(ModBlocks.SILVER.getDustObject().get());
        getOrCreateBuilder(ModTags.TIN_DUST).add(ModBlocks.TIN.getDustObject().get());
        getOrCreateBuilder(ModTags.BRONZE_DUST).add(ModBlocks.BRONZE.getDustObject().get());
        getOrCreateBuilder(ModTags.STEEL_DUST).add(ModBlocks.STEEL.getDustObject().get());
        getOrCreateBuilder(ModTags.PLATINUM_DUST).add(ModBlocks.PLATINUM.getDustObject().get());
        getOrCreateBuilder(ModTags.IRON_DUST).add(ModBlocks.IRON.getDustObject().get());
        getOrCreateBuilder(ModTags.GOLD_DUST).add(ModBlocks.GOLD.getDustObject().get());

        getOrCreateBuilder(ModTags.COPPER_INGOT).add(ModBlocks.COPPER.getIngotObject().get());
        getOrCreateBuilder(ModTags.SILVER_INGOT).add(ModBlocks.SILVER.getIngotObject().get());
        getOrCreateBuilder(ModTags.TIN_INGOT).add(ModBlocks.TIN.getIngotObject().get());
        getOrCreateBuilder(ModTags.BRONZE_INGOT).add(ModBlocks.BRONZE.getIngotObject().get());
        getOrCreateBuilder(ModTags.STEEL_INGOT).add(ModBlocks.STEEL.getIngotObject().get());
        getOrCreateBuilder(ModTags.URANIUM_INGOT).add(ModBlocks.URANIUM.getIngotObject().get());
        getOrCreateBuilder(ModTags.PLATINUM_INGOT).add(ModBlocks.PLATINUM.getIngotObject().get());
        getOrCreateBuilder(ModTags.SILICON).add(ModBlocks.SILICON.getIngotObject().get());

        getOrCreateBuilder(ModTags.COPPER_NUGGET).add(ModBlocks.COPPER.getNuggetObject().get());
        getOrCreateBuilder(ModTags.SILVER_NUGGET).add(ModBlocks.SILVER.getNuggetObject().get());
        getOrCreateBuilder(ModTags.TIN_NUGGET).add(ModBlocks.TIN.getNuggetObject().get());
        getOrCreateBuilder(ModTags.BRONZE_NUGGET).add(ModBlocks.BRONZE.getNuggetObject().get());
        getOrCreateBuilder(ModTags.STEEL_NUGGET).add(ModBlocks.STEEL.getNuggetObject().get());
        getOrCreateBuilder(ModTags.PLATINUM_NUGGET).add(ModBlocks.PLATINUM.getNuggetObject().get());

        getOrCreateBuilder(ModTags.COPPER_BLOCK_ITEM).add(ModBlocks.COPPER.getBlockItemObject().get());
        getOrCreateBuilder(ModTags.SILVER_BLOCK_ITEM).add(ModBlocks.SILVER.getBlockItemObject().get());
        getOrCreateBuilder(ModTags.TIN_BLOCK_ITEM).add(ModBlocks.TIN.getBlockItemObject().get());
        getOrCreateBuilder(ModTags.BRONZE_BLOCK_ITEM).add(ModBlocks.BRONZE.getBlockItemObject().get());
        getOrCreateBuilder(ModTags.STEEL_BLOCK_ITEM).add(ModBlocks.STEEL.getBlockItemObject().get());
        getOrCreateBuilder(ModTags.URANIUM_BLOCK_ITEM).add(ModBlocks.URANIUM.getBlockItemObject().get());
        getOrCreateBuilder(ModTags.PLATINUM_BLOCK_ITEM).add(ModBlocks.PLATINUM.getBlockItemObject().get());

        getOrCreateBuilder(ModTags.COPPER_ORE_ITEM).add(ModBlocks.COPPER.getOreItemObject().get());
        getOrCreateBuilder(ModTags.SILVER_ORE_ITEM).add(ModBlocks.SILVER.getOreItemObject().get());
        getOrCreateBuilder(ModTags.TIN_ORE_ITEM).add(ModBlocks.TIN.getOreItemObject().get());
        getOrCreateBuilder(ModTags.URANIUM_ORE_ITEM).add(ModBlocks.URANIUM.getOreItemObject().get());
        getOrCreateBuilder(ModTags.PLATINUM_ORE_ITEM).add(ModBlocks.PLATINUM.getOreItemObject().get());
    }
}
