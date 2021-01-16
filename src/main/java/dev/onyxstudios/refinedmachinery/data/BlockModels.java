package dev.onyxstudios.refinedmachinery.data;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.registry.resource.Resource;
import dev.onyxstudios.refinedmachinery.registry.resource.ResourceBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModels extends BlockStateProvider {

    public static ResourceLocation ORE_PARENT = new ResourceLocation(RefinedMachinery.MODID, "block/ore_overlay");
    public static ResourceLocation RESOURCE_PARENT = new ResourceLocation(RefinedMachinery.MODID, "block/resource_base");

    public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, RefinedMachinery.MODID, existingFileHelper);
    }

    @Override
    public void registerStatesAndModels() {
        for (Resource resource : ResourceBuilder.resourceList) {
            if(resource.isOreValid()) {
                ResourceLocation name = resource.getOreObject().getId();
                simpleBlock(resource.getOreObject().get(), models().withExistingParent(name.toString(), ORE_PARENT.toString()));
            }

            if(resource.isBlockValid()) {
                ResourceLocation name = resource.getBlockObject().getId();
                simpleBlock(resource.getBlockObject().get(), models().withExistingParent(name.toString(), RESOURCE_PARENT.toString()));
            }
        }
    }
}
