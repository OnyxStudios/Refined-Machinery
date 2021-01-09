package dev.onyxstudios.refinedmachinery.client.models;

import dev.onyxstudios.refinedmachinery.registry.ModRenders;
import dev.onyxstudios.refinedmachinery.utils.MachineConfigType;
import dev.onyxstudios.refinedmachinery.utils.ModelDataTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MachineBakedModel implements IDynamicBakedModel {

    private IBakedModel previousModel;

    public MachineBakedModel(IBakedModel bakedModel) {
        this.previousModel = bakedModel;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData modelData) {
        List<BakedQuad> quads = new ArrayList<>(previousModel.getQuads(state, side, rand, modelData));
        if(side == null)
            return quads;

        if(modelData.getData(ModelDataTypes.CONFIG_PROPERTY) != null) {
            MachineConfigType type = modelData.getData(ModelDataTypes.CONFIG_PROPERTY)[side.ordinal()];

            if(type != MachineConfigType.NONE) {
                quads.addAll(createConfigQuads(side, type, rand));
            }
        }

        return quads;
    }

    public List<BakedQuad> createConfigQuads(Direction direction, MachineConfigType type, Random random) {
        switch (type) {
            case INPUT:
                return ModRenders.INPUT_MODEL.getQuads(null, direction, new Random(), EmptyModelData.INSTANCE);
            case EXTRACT:
                return ModRenders.OUTPUT_MODEL.getQuads(null, direction, new Random(), EmptyModelData.INSTANCE);
            case INPUT_EXTRACT:
                return ModRenders.INPUT_OUTPUT_MODEL.getQuads(null, direction, new Random(), EmptyModelData.INSTANCE);
            default:
                break;
        }

        return new ArrayList<>();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return previousModel.getItemCameraTransforms();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return previousModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return previousModel.isGui3d();
    }

    @Override
    public boolean isSideLit() {
        return previousModel.isSideLit();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return previousModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return previousModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return previousModel.getOverrides();
    }
}
