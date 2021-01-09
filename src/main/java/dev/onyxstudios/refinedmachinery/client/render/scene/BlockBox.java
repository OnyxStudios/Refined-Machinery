package dev.onyxstudios.refinedmachinery.client.render.scene;

import net.minecraft.util.Direction;
import org.joml.Vector3f;

public class BlockBox {

    private Direction direction;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f color;

    public BlockBox(Direction direction, Vector3f position, Vector3f rotation, Vector3f color) {
        this.direction = direction;
        this.position = position;
        this.rotation = rotation;
        this.color = color;
    }

    public Direction getDirection() {
        return direction;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getColor() {
        return color;
    }
}
