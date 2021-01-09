package dev.onyxstudios.refinedmachinery.client.render.scene;

import net.minecraft.util.math.BlockPos;

public class BlockModel {

    private int vaoId;
    private int vertexCount;
    private BlockPos pos;
    private float alpha;

    public BlockModel(int vaoId, int vertexCount, BlockPos pos, float alpha) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
        this.pos = pos;
        this.alpha = alpha;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public BlockPos getPos() {
        return pos;
    }

    public float getAlpha() {
        return alpha;
    }
}
