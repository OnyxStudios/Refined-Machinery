package dev.onyxstudios.refinedmachinery.client.models.machine;

import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.geometry.ISimpleModelGeometry;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class MachineModelGeometry implements ISimpleModelGeometry<MachineModelGeometry> {

    private ModelLoaderRegistry.VanillaProxy proxy;

    public MachineModelGeometry(ModelLoaderRegistry.VanillaProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        IBakedModel model = proxy.bake(owner, bakery, spriteGetter, modelTransform, overrides, modelLocation);
        return new MachineBakedModel(model);
    }

    @Override
    public void addQuads(IModelConfiguration owner, IModelBuilder<?> modelBuilder, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ResourceLocation modelLocation) {
        proxy.addQuads(owner, modelBuilder, bakery, spriteGetter, modelTransform, modelLocation);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function modelGetter, Set missingTextureErrors) {
        return proxy.getTextures(owner, modelGetter, missingTextureErrors);
    }
}
