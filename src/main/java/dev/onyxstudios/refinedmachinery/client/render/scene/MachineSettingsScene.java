package dev.onyxstudios.refinedmachinery.client.render.scene;

import dev.onyxstudios.refinedmachinery.client.shaders.Shaders;
import dev.onyxstudios.refinedmachinery.tileentity.TileEntityConfigurable;
import dev.onyxstudios.refinedmachinery.utils.ModelUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class MachineSettingsScene {

    public static final int DEFAULT_WIDTH = 162;
    public static final int DEFAULT_HEIGHT = 76;
    private static float[] quadPositions = new float[] {
            -0.5f, 0.5f, 0,
            0.5f, 0.5f, 0,
            0.5f, -0.5f, 0,
            -0.5f, -0.5f, 0
    };

    private static List<BlockBox> mappedBounds = new ArrayList<>();
    {
        mappedBounds.add(new BlockBox(Direction.NORTH, new Vector3f(0, 0, -0.5f), new Vector3f(), new Vector3f(0, 1, 0)));
        mappedBounds.add(new BlockBox(Direction.SOUTH, new Vector3f(0, 0, 0.5f), new Vector3f(0, 180, 0), new Vector3f(1, 0, 0)));
        mappedBounds.add(new BlockBox(Direction.EAST, new Vector3f(0.5f, 0, 0), new Vector3f(0, -90, 0), new Vector3f(0, 0, 1)));
        mappedBounds.add(new BlockBox(Direction.WEST, new Vector3f(-0.5f, 0, 0), new Vector3f(0, 90, 0), new Vector3f(1, 1, 1)));
        mappedBounds.add(new BlockBox(Direction.UP, new Vector3f(0, 0.5f, 0), new Vector3f(90, 0, 0), new Vector3f(1, 1, 0)));
        mappedBounds.add(new BlockBox(Direction.DOWN, new Vector3f(0, -0.5f, 0), new Vector3f(-90, 0, 0), new Vector3f(0, 1, 1)));
    }

    private List<BlockModel> models = new ArrayList<>();
    private BlockModel machineModel;
    private Camera camera = new Camera();

    public RMFramebuffer framebuffer;
    public RMFramebuffer pickerBuffer;

    private BlockModel quad;

    public void init(BlockState machineState, TileEntityConfigurable tile) {
        machineModel = ModelUtils.createBlockModel(machineState, new BlockPos(0, 0, 0), tile.getModelData(), 1);
        rotateAroundOrigin(0, 0);

        MainWindow window = Minecraft.getInstance().getMainWindow();
        framebuffer = new RMFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight());
        pickerBuffer = new RMFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight());
        framebuffer.init();
        pickerBuffer.init();
        quad = ModelUtils.loadToVAO(quadPositions);
    }

    public void onMouseDragged(double dragX, double dragY) {
        rotateAroundOrigin((float) -dragX * 2, (float) dragY * 2);
    }

    public void renderSceneBuffer() {
        int[] previousFBO = new int[1];
        glGetIntegerv(GL_FRAMEBUFFER_BINDING, previousFBO);
        Matrix4f viewMatrix = camera.createViewMatrix();

        MainWindow window = Minecraft.getInstance().getMainWindow();
        double scaledWidth = DEFAULT_WIDTH * window.getGuiScaleFactor();
        double scaledHeight = DEFAULT_HEIGHT * window.getGuiScaleFactor();
        camera.createProjectionMatrix((int) scaledWidth, (int) scaledHeight);

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer.getFbo());
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0, 0, 0, 1);

        Shaders.SCENE_SHADER.start();
        Shaders.SCENE_SHADER.loadViewMatrix(viewMatrix);
        Shaders.SCENE_SHADER.loadProjectionMatrix(camera.getProjectionMatrix());
        for (BlockModel model : models) {
            renderBlock(model);
        }
        renderBlock(machineModel);
        Shaders.SCENE_SHADER.stop();

        //Reset Scene
        glBindFramebuffer(GL_FRAMEBUFFER, previousFBO[0]);
    }

    private void renderToFBO(Matrix4f viewMatrix) {
        glBindFramebuffer(GL_FRAMEBUFFER, pickerBuffer.getFbo());
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0, 0, 0, 1);

        Shaders.BB_SHADER.start();
        Shaders.BB_SHADER.loadViewMatrix(viewMatrix);
        Shaders.BB_SHADER.loadProjectionMatrix(camera.getProjectionMatrix());
        for (BlockBox box : mappedBounds) {
            renderBoundingBox(box);
        }
        Shaders.BB_SHADER.stop();
    }

    private void renderBlock(BlockModel model) {
        Matrix4f transformationMatrix = createTransformationMatrix(new Vector3f(model.getPos().getX() + 0.5f, model.getPos().getY() + 0.5f, model.getPos().getZ() + 0.5f).negate(), new Vector3f(0));
        Shaders.SCENE_SHADER.loadTransformationMatrix(transformationMatrix);
        Shaders.SCENE_SHADER.loadAlpha(model.getAlpha());

        glBindVertexArray(model.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);
        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        glDrawArrays(GL_QUADS, 0, model.getVertexCount());
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private void renderBoundingBox(BlockBox box) {
        Matrix4f transformMatrix = createTransformationMatrix(box.getPosition(), box.getRotation());
        Shaders.BB_SHADER.loadTransformationMatrix(transformMatrix);
        Shaders.BB_SHADER.loadColor(box.getColor());

        glBindVertexArray(quad.getVaoId());
        glEnableVertexAttribArray(0);
        glDepthMask(true);
        glDrawArrays(GL_QUADS, 0, quad.getVertexCount());
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public Direction getDirectionClicked(int x, int y) {
        renderToFBO(camera.createViewMatrix());
        glBindFramebuffer(GL_READ_FRAMEBUFFER, pickerBuffer.getFbo());
        float[] pixels = new float[3];

        int xPos = (x * framebuffer.getWidth()) / DEFAULT_WIDTH;
        int yPos = framebuffer.getHeight() - ((y * framebuffer.getHeight()) / DEFAULT_HEIGHT);

        glReadPixels(xPos, yPos, 1, 1, GL_RGB, GL_FLOAT, pixels);

        float r = pixels[0];
        float g = pixels[1];
        float b = pixels[2];
        for (BlockBox box : mappedBounds) {
            if(box.getColor().equals(r, g, b)) {
                return box.getDirection();
            }
        }

        return null;
    }

    public void updateMachineState(BlockState state, TileEntityConfigurable tile) {
        machineModel = ModelUtils.createBlockModel(state, new BlockPos(0, 0, 0), tile.getModelData(), 1);
    }

    private void rotateAroundOrigin(float x, float y) {
        float rotationFactor = 100;
        float radius = 1.8f;
        float ry = (-x * rotationFactor) / 360.0f;
        float rx = (y * rotationFactor) / 180.0f;

        camera.rotation.add(rx, ry, 0);
        float degree = (float) (Math.PI / 180.0f);
        float camX = radius * -Math.sin(camera.rotation.y * degree) * Math.cos(camera.rotation.x * degree);
        float camY = radius * -Math.sin(camera.rotation.x * degree);
        float camZ = radius * Math.cos(camera.rotation.y * degree) * Math.cos(camera.rotation.x * degree);
        camera.position.set(new Vector3f(camX, -camY, camZ));
    }

    private Matrix4f createTransformationMatrix(Vector3f pos, Vector3f rotation) {
        return new Matrix4f().translate(pos)
                .rotateX(Math.toRadians(rotation.x))
                .rotateY(Math.toRadians(rotation.y))
                .rotateZ(Math.toRadians(rotation.z))
                .scale(1);
    }

    public void addNeighbor(BlockPos pos, BlockState state) {
        models.add(ModelUtils.createBlockModel(state, pos, EmptyModelData.INSTANCE, 0.70f));
    }
}
