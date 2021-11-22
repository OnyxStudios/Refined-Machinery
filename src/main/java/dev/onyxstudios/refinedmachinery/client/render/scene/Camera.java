package dev.onyxstudios.refinedmachinery.client.render.scene;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    protected Vector3f position;
    protected Vector3f rotation;

    protected Matrix4f projectionMatrix = new Matrix4f();
    protected Matrix4f viewMatrix = new Matrix4f();

    public Camera() {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void createProjectionMatrix(int width, int height) {
        float aspectRatio = (float) width / (float) height;
        projectionMatrix = new Matrix4f().perspective(Math.toRadians(70), aspectRatio, 0.01f, 1000.0f);
    }

    public Matrix4f createViewMatrix() {
        viewMatrix = new Matrix4f();
        viewMatrix.rotate(Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        viewMatrix.rotate(Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        viewMatrix.translate(new Vector3f(-position.x, -position.y, -position.z));

        return viewMatrix;
    }
}
