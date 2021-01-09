package dev.onyxstudios.refinedmachinery.client.shaders;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import net.minecraft.util.ResourceLocation;
import org.joml.Matrix4f;

public class SettingsSceneShader extends ShaderProgram {

    public static ResourceLocation VS = new ResourceLocation(RefinedMachinery.MODID, "shaders/settings_scene.vs");
    public static ResourceLocation FS = new ResourceLocation(RefinedMachinery.MODID, "shaders/settings_scene.fs");

    protected int projectionMatrix;
    protected int transformationMatrix;
    protected int viewMatrix;
    protected int alpha;

    public SettingsSceneShader() {
        super(VS, FS);
    }

    @Override
    public void getAllUniformLocations() {
        projectionMatrix = super.getUniformLocation("projectionMatrix");
        transformationMatrix = super.getUniformLocation("transformationMatrix");
        viewMatrix = super.getUniformLocation("viewMatrix");
        alpha = super.getUniformLocation("alpha");
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(projectionMatrix, matrix);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(transformationMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrix(viewMatrix, matrix);
    }

    public void loadAlpha(float value) {
        super.loadFloat(alpha, value);
    }
}
