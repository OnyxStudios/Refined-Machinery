package dev.onyxstudios.refinedmachinery.registry;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {

    public static final ITag.INamedTag<Item> COOLANT = createModTag("coolant_item");
    //Item Tags
    public static final ITag.INamedTag<Item> COPPER_DUST = createForgeTag("dusts/copper");
    public static final ITag.INamedTag<Item> SILVER_DUST = createForgeTag("dusts/silver");
    public static final ITag.INamedTag<Item> TIN_DUST = createForgeTag("dusts/tin");
    public static final ITag.INamedTag<Item> BRONZE_DUST = createForgeTag("dusts/bronze");
    public static final ITag.INamedTag<Item> STEEL_DUST = createForgeTag("dusts/steel");
    public static final ITag.INamedTag<Item> PLATINUM_DUST = createForgeTag("dusts/platinum");
    public static final ITag.INamedTag<Item> IRON_DUST = createForgeTag("dusts/iron");
    public static final ITag.INamedTag<Item> GOLD_DUST = createForgeTag("dusts/gold");

    public static final ITag.INamedTag<Item> COPPER_INGOT = createForgeTag("ingots/copper");
    public static final ITag.INamedTag<Item> SILVER_INGOT = createForgeTag("ingots/silver");
    public static final ITag.INamedTag<Item> TIN_INGOT = createForgeTag("ingots/tin");
    public static final ITag.INamedTag<Item> BRONZE_INGOT = createForgeTag("ingots/bronze");
    public static final ITag.INamedTag<Item> STEEL_INGOT = createForgeTag("ingots/steel");
    public static final ITag.INamedTag<Item> URANIUM_INGOT = createForgeTag("ingots/uranium");
    public static final ITag.INamedTag<Item> PLATINUM_INGOT = createForgeTag("ingots/platinum");
    public static final ITag.INamedTag<Item> SILICON = createForgeTag("silicon");

    public static final ITag.INamedTag<Item> COPPER_NUGGET = createForgeTag("nuggets/copper");
    public static final ITag.INamedTag<Item> SILVER_NUGGET = createForgeTag("nuggets/silver");
    public static final ITag.INamedTag<Item> TIN_NUGGET = createForgeTag("nuggets/tin");
    public static final ITag.INamedTag<Item> BRONZE_NUGGET = createForgeTag("nuggets/bronze");
    public static final ITag.INamedTag<Item> STEEL_NUGGET = createForgeTag("nuggets/steel");
    public static final ITag.INamedTag<Item> PLATINUM_NUGGET = createForgeTag("nuggets/platinum");

    public static final ITag.INamedTag<Item> COPPER_BLOCK_ITEM = createForgeTag("storage_blocks/copper");
    public static final ITag.INamedTag<Item> SILVER_BLOCK_ITEM = createForgeTag("storage_blocks/silver");
    public static final ITag.INamedTag<Item> TIN_BLOCK_ITEM = createForgeTag("storage_blocks/tin");
    public static final ITag.INamedTag<Item> BRONZE_BLOCK_ITEM = createForgeTag("storage_blocks/bronze");
    public static final ITag.INamedTag<Item> STEEL_BLOCK_ITEM = createForgeTag("storage_blocks/steel");
    public static final ITag.INamedTag<Item> URANIUM_BLOCK_ITEM = createForgeTag("storage_blocks/uranium");
    public static final ITag.INamedTag<Item> PLATINUM_BLOCK_ITEM = createForgeTag("storage_blocks/platinum");

    public static final ITag.INamedTag<Item> COPPER_ORE_ITEM = createForgeTag("ores/copper");
    public static final ITag.INamedTag<Item> SILVER_ORE_ITEM = createForgeTag("ores/silver");
    public static final ITag.INamedTag<Item> TIN_ORE_ITEM = createForgeTag("ores/tin");
    public static final ITag.INamedTag<Item> URANIUM_ORE_ITEM = createForgeTag("ores/uranium");
    public static final ITag.INamedTag<Item> PLATINUM_ORE_ITEM = createForgeTag("ores/platinum");

    //Block Tags final
    public static final ITag.INamedTag<Block> COPPER_BLOCK = createBlockWrapper(COPPER_BLOCK_ITEM);
    public static final ITag.INamedTag<Block> SILVER_BLOCK = createBlockWrapper(SILVER_BLOCK_ITEM);
    public static final ITag.INamedTag<Block> TIN_BLOCK = createBlockWrapper(TIN_BLOCK_ITEM);
    public static final ITag.INamedTag<Block> BRONZE_BLOCK = createBlockWrapper(BRONZE_BLOCK_ITEM);
    public static final ITag.INamedTag<Block> STEEL_BLOCK = createBlockWrapper(STEEL_BLOCK_ITEM);
    public static final ITag.INamedTag<Block> URANIUM_BLOCK = createBlockWrapper(URANIUM_BLOCK_ITEM);
    public static final ITag.INamedTag<Block> PLATINUM_BLOCK = createBlockWrapper(PLATINUM_BLOCK_ITEM);

    public static final ITag.INamedTag<Block> COPPER_ORE = createBlockWrapper(COPPER_ORE_ITEM);
    public static final ITag.INamedTag<Block> SILVER_ORE = createBlockWrapper(SILVER_ORE_ITEM);
    public static final ITag.INamedTag<Block> TIN_ORE = createBlockWrapper(TIN_ORE_ITEM);
    public static final ITag.INamedTag<Block> URANIUM_ORE = createBlockWrapper(URANIUM_ORE_ITEM);
    public static final ITag.INamedTag<Block> PLATINUM_ORE = createBlockWrapper(PLATINUM_ORE_ITEM);

    public static ITag.INamedTag<Item> createModTag(String name) {
        return ItemTags.makeWrapperTag(new ResourceLocation(RefinedMachinery.MODID, name).toString());
    }

    public static ITag.INamedTag<Item> createForgeTag(String name) {
        return ItemTags.makeWrapperTag(new ResourceLocation("forge", name).toString());
    }

    public static ITag.INamedTag<Block> createBlockWrapper(ITag.INamedTag<?> tag) {
        return BlockTags.makeWrapperTag(tag.getName().toString());
    }
}
