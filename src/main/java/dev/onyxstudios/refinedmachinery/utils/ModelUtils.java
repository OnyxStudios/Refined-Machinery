package dev.onyxstudios.refinedmachinery.utils;

import dev.onyxstudios.refinedmachinery.client.render.scene.BlockModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ModelUtils {

    public static Random random = new Random();
    public static Direction[] DIRECTIONS = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN, null};

    public static BlockModel createBlockModel(BlockState state, BlockPos pos, IModelData modelData, float alpha) {
        IBakedModel model = Minecraft.getInstance().getModelManager().getBlockModelShapes().getModel(state);

        List<Float> positions = new ArrayList<>();
        List<Float> uvs = new ArrayList<>();
        for (Direction direction : DIRECTIONS) {
            List<BakedQuad> quads = model.getQuads(state, direction, random, modelData);

            for (BakedQuad quad : quads) {
                int[] vertex = quad.getVertexData();
                int j = vertex.length / 8;

                try (MemoryStack memorystack = MemoryStack.stackPush()) {
                    ByteBuffer bytebuffer = memorystack.malloc(DefaultVertexFormats.BLOCK.getSize());
                    IntBuffer intbuffer = bytebuffer.asIntBuffer();

                    for (int i = 0; i < j; i++) {
                        intbuffer.clear();
                        Map.Entry<Vector3f, Vector2f> entry = unpackData(bytebuffer, intbuffer, vertex, i);
                        positions.add(entry.getKey().x);
                        positions.add(entry.getKey().y);
                        positions.add(entry.getKey().z);

                        uvs.add(entry.getValue().x);
                        uvs.add(entry.getValue().y);
                    }
                }
            }
        }

        float[] vertices = new float[positions.size()];
        float[] textureCoords = new float[uvs.size()];
        for (int i = 0; i < positions.size(); i++) {
            vertices[i] = positions.get(i);
        }

        for (int i = 0; i < uvs.size(); i++) {
            textureCoords[i] = uvs.get(i);
        }

        return loadToVAO(pos, alpha, vertices, textureCoords);
    }

    private static Map.Entry<Vector3f, Vector2f> unpackData(ByteBuffer byteBuffer, IntBuffer intBuffer, int[] vertexData, int i) {
        intBuffer.put(vertexData, i * 8, 8);
        float x = byteBuffer.getFloat(0);
        float y = byteBuffer.getFloat(4);
        float z = byteBuffer.getFloat(8);
        float u = byteBuffer.getFloat(16);
        float v = byteBuffer.getFloat(20);

        return new AbstractMap.SimpleEntry<>(new Vector3f(x, y, z), new Vector2f(u, v));
    }

    public static BlockModel loadToVAO(BlockPos pos, float alpha, float[] positions, float[] textureCoords) {
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        ModelUtils.storeDataInAttribsList(0, 3, positions);
        ModelUtils.storeDataInAttribsList(1, 2, textureCoords);
        glBindVertexArray(0);
        return new BlockModel(vao, positions.length / 3, pos, alpha);
    }

    public static BlockModel loadToVAO(float[] positions) {
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        ModelUtils.storeDataInAttribsList(0, 3, positions);
        glBindVertexArray(0);
        return new BlockModel(vao, positions.length / 3, null, 1);
    }

    private static void storeDataInAttribsList(int attribsNum, int coordinatesize, float[] data) {
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribsNum, coordinatesize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
