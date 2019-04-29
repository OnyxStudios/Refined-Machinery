package abused_master.refinedmachinery.registry;

import abused_master.refinedmachinery.RefinedMachinery;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class RMTags {

    //Item Tags
    public static final Tag<Item> COPPER_INGOT = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "copper_ingot"));
    public static final Tag<Item> TIN_INGOT = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "tin_ingot"));
    public static final Tag<Item> LEAD_INGOT = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "lead_ingot"));
    public static final Tag<Item> SILVER_INGOT = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "silver_ingot"));
    public static final Tag<Item> NICKEL_INGOT = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "nickel_ingot"));

    public static final Tag<Item> COAL_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "coal_dust"));
    public static final Tag<Item> DIAMOND_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "diamond_dust"));
    public static final Tag<Item> IRON_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "iron_dust"));
    public static final Tag<Item> GOLD_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "gold_dust"));
    public static final Tag<Item> COPPER_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "copper_dust"));
    public static final Tag<Item> TIN_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "tin_dust"));
    public static final Tag<Item> LEAD_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "lead_dust"));
    public static final Tag<Item> SILVER_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "silver_dust"));
    public static final Tag<Item> NICKEL_DUST = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "nickel_dust"));

    //Block Tags
    public static final Tag<Item> COPPER_BLOCK = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "copper_block"));
    public static final Tag<Item> TIN_BLOCK = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "tin_block"));
    public static final Tag<Item> LEAD_BLOCK = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "lead_block"));
    public static final Tag<Item> SILVER_BLOCK = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "silver_block"));
    public static final Tag<Item> NICKEL_BLOCK = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "nickel_block"));

    public static final Tag<Item> COPPER_ORE = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "copper_ore"));
    public static final Tag<Item> TIN_ORE = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "tin_ore"));
    public static final Tag<Item> LEAD_ORE = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "lead_ore"));
    public static final Tag<Item> SILVER_ORE = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "silver_ore"));
    public static final Tag<Item> NICKEL_ORE = TagRegistry.item(new Identifier(RefinedMachinery.RESOURCES_TAG, "nickel_ore"));
}
