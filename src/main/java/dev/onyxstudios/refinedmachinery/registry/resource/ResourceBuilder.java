package dev.onyxstudios.refinedmachinery.registry.resource;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.blocks.resources.ResourceBlock;
import dev.onyxstudios.refinedmachinery.items.ResourceItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourceBuilder {

    public static List<Resource> resourceList = new ArrayList<>();
    public static ResourceLocation BASE_INGOT_TEXTURE = new ResourceLocation(RefinedMachinery.MODID, "item/base_resource_ingot");
    public static ResourceLocation BASE_DUST_TEXTURE = new ResourceLocation(RefinedMachinery.MODID, "item/base_resource_dust");
    public static ResourceLocation BASE_NUGGET_TEXTURE = new ResourceLocation(RefinedMachinery.MODID, "item/base_resource_nugget");

    private String name;
    private float hardness = 1;
    private int harvestLevel = 1;

    private Resource result;
    private Block oreBlock;
    private Block resourceBlock;
    private Item ingotItem;
    private Item dustItem;
    private Item nuggetItem;

    private String oreSuffix = "_ore";
    private String blockSuffix = "_block";
    private String ingotSuffix = "_ingot";
    private String dustSuffix = "_dust";
    private String nuggetSuffix = "_nugget";

    public ResourceBuilder(String name) {
        this.name = name;
        this.result = new Resource();
        this.result.setItemTexture(BASE_INGOT_TEXTURE);
    }

    public ResourceBuilder setColor(int color) {
        result.setColor(color);
        return this;
    }

    public ResourceBuilder setItemTexture(ResourceLocation texture) {
        result.setItemTexture(texture);
        return this;
    }

    public ResourceBuilder setHardness(float hardness) {
        this.hardness = hardness;
        return this;
    }

    public ResourceBuilder setHarvestLevel(int harvestLevel) {
        this.harvestLevel = harvestLevel;
        return this;
    }

    public ResourceBuilder addOre() {
        return addOre("");
    }

    public ResourceBuilder addOre(String suffix) {
        this.oreBlock = new ResourceBlock(this.hardness, this.harvestLevel);
        if(!suffix.isEmpty())
            this.oreSuffix = suffix;

        return this;
    }

    public ResourceBuilder addResourceBlock() {
        return addResourceBlock("");
    }

    public ResourceBuilder addResourceBlock(String suffix) {
        this.resourceBlock = new ResourceBlock(this.hardness, this.harvestLevel);
        if(!suffix.isEmpty())
            this.blockSuffix = suffix;

        return this;
    }

    public ResourceBuilder addIngot() {
        return addIngot("");
    }

    public ResourceBuilder addIngot(String suffix) {
        this.ingotItem = new ResourceItem();
        if(!suffix.isEmpty())
            this.ingotSuffix = suffix;

        return this;
    }

    public ResourceBuilder addDust() {
        return addDust("");
    }

    public ResourceBuilder addDust(String suffix) {
        this.dustItem = new ResourceItem();
        if(!suffix.isEmpty())
            this.dustSuffix = suffix;

        return this;
    }

    public ResourceBuilder addNugget() {
        return addNugget("");
    }

    public ResourceBuilder addNugget(String suffix) {
        this.nuggetItem = new ResourceItem();
        if(!suffix.isEmpty())
            this.nuggetSuffix = suffix;

        return this;
    }

    public Resource build(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
        Objects.requireNonNull(itemRegistry);
        Objects.requireNonNull(blockRegistry);
        if(oreBlock != null)
            result.buildOre(blockRegistry, itemRegistry, name + oreSuffix, oreBlock);

        if(resourceBlock != null)
            result.buildBlock(blockRegistry, itemRegistry, name + blockSuffix, resourceBlock);

        if(ingotItem != null)
            result.buildIngot(itemRegistry, name + ingotSuffix, ingotItem);

        if(dustItem != null)
            result.buildDust(itemRegistry, name + dustSuffix, dustItem);

        if(nuggetItem != null)
            result.buildNugget(itemRegistry, name + nuggetSuffix, nuggetItem);

        resourceList.add(result);
        return this.result;
    }
}
