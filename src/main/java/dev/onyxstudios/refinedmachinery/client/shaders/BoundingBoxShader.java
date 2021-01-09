package dev.onyxstudios.refinedmachinery.client.shaders;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import net.minecraft.util.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BoundingBoxShader extends ShaderProgram {

    public static ResourceLocation VS = new ResourceLocation(RefinedMachinery.MODID, "shaders/boundingbox.vs");
    public static ResourceLocation FS = new ResourceLocation(RefinedMachinery.MODID, "shaders/boundingbox.fs");

    protected int projectionMatrix;
    protected int transformationMatrix;
    protected int viewMatrix;
    protected int color;

    public BoundingBoxShader() {
        super(VS, FS);
    }

    @Override
    public void getAllUniformLocations() {
        projectionMatrix = super.getUniformLocation("projectionMatrix");
        transformationMatrix = super.getUniformLocation("transformationMatrix");
        viewMatrix = super.getUniformLocation("viewMatrix");
        color = super.getUniformLocation("color");
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
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

    public void loadColor(Vector3f vector) {
        super.loadVector(color, vector);
    }
}
