package dev.onyxstudios.refinedmachinery.data;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.registry.resource.Resource;
import dev.onyxstudios.refinedmachinery.registry.resource.ResourceBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public static ResourceLocation ITEM_GENERATED = new ResourceLocation("item/generated");

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, RefinedMachinery.MODID, existingFileHelper);
    }

    @Override
    public void registerModels() {
        for (Resource resource : ResourceBuilder.resourceList) {
            if(resource.isIngotValid()) {
                withExistingParent(resource.getIngotObject().getId().toString(), ITEM_GENERATED).texture("layer0", resource.getItemTexture());
            }

            if(resource.isDustValid()) {
                withExistingParent(resource.getDustObject().getId().toString(), ITEM_GENERATED).texture("layer0", ResourceBuilder.BASE_DUST_TEXTURE);
            }

            if(resource.isNuggetValid()) {
                withExistingParent(resource.getNuggetObject().getId().toString(), ITEM_GENERATED).texture("layer0", ResourceBuilder.BASE_NUGGET_TEXTURE);
            }

            if(resource.isBlockValid()) {
                ResourceLocation name = resource.getBlockObject().getId();
                getBuilder(name.toString()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RefinedMachinery.MODID, "block/" + name.getPath())));
            }

            if(resource.isOreValid()) {
                ResourceLocation name = resource.getOreObject().getId();
                getBuilder(name.toString()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(RefinedMachinery.MODID, "block/" + name.getPath())));
            }
        }
    }
}
