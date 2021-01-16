package dev.onyxstudios.refinedmachinery.registry.resource;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class Resource {

    private RegistryObject<Block> oreObject = null;
    private RegistryObject<Item> oreItemObject = null;
    private RegistryObject<Block> blockObject = null;
    private RegistryObject<Item> blockItemObject = null;

    private RegistryObject<Item> ingotObject = null;
    private RegistryObject<Item> dustObject = null;
    private RegistryObject<Item> nuggetObject = null;

    private boolean isOreValid = false;
    private boolean isBlockValid = false;
    private boolean isIngotValid = false;
    private boolean isDustValid = false;
    private boolean isNuggetValid = false;

    private ResourceLocation itemTexture;
    private int color = 0xFFFFFF;

    public boolean isOreValid() {
        return isOreValid;
    }

    public boolean isBlockValid() {
        return isBlockValid;
    }

    public boolean isDustValid() {
        return isDustValid;
    }

    public boolean isIngotValid() {
        return isIngotValid;
    }

    public boolean isNuggetValid() {
        return isNuggetValid;
    }

    public RegistryObject<Block> getOreObject() {
        return oreObject;
    }

    public RegistryObject<Item> getOreItemObject() {
        return oreItemObject;
    }

    public RegistryObject<Block> getBlockObject() {
        return blockObject;
    }

    public RegistryObject<Item> getBlockItemObject() {
        return blockItemObject;
    }

    public RegistryObject<Item> getIngotObject() {
        return ingotObject;
    }

    public RegistryObject<Item> getDustObject() {
        return dustObject;
    }

    public RegistryObject<Item> getNuggetObject() {
        return nuggetObject;
    }

    public int getColor() {
        return color;
    }

    public ResourceLocation getItemTexture() {
        return itemTexture;
    }

    protected void buildOre(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry, String name, Block block) {
        oreObject = blockRegistry.register(name, () -> block);
        oreItemObject = itemRegistry.register(name, () -> new BlockItem(block, new Item.Properties().group(RefinedMachinery.TAB)));
        isOreValid = true;
    }

    protected void buildBlock(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry, String name, Block block) {
        blockObject = blockRegistry.register(name, () -> block);
        blockItemObject = itemRegistry.register(name, () -> new BlockItem(block, new Item.Properties().group(RefinedMachinery.TAB)));
        isBlockValid = true;
    }

    protected void buildIngot(DeferredRegister<Item> deferredRegister, String name, Item item) {
        ingotObject = deferredRegister.register(name, () -> item);
        isIngotValid = true;
    }

    protected void buildDust(DeferredRegister<Item> deferredRegister, String name, Item item) {
        dustObject = deferredRegister.register(name, () -> item);
        isDustValid = true;
    }

    protected void buildNugget(DeferredRegister<Item> deferredRegister, String name, Item item) {
        nuggetObject = deferredRegister.register(name, () -> item);
        isNuggetValid = true;
    }

    protected void setItemTexture(ResourceLocation itemTexture) {
        this.itemTexture = itemTexture;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
