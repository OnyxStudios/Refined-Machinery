package dev.onyxstudios.refinedmachinery.client.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class MachineModelLoader implements IModelLoader {

    public static final MachineModelLoader INSTANCE = new MachineModelLoader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
    }

    @Override
    public IModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        ModelLoaderRegistry.VanillaProxy proxy = ModelLoaderRegistry.VanillaProxy.Loader.INSTANCE.read(deserializationContext, modelContents);

        return new MachineModelGeometry(proxy);
    }
}
