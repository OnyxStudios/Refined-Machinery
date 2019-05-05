package abused_master.refinedmachinery.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Box;
import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class RoboWingsModel <T extends LivingEntity> extends EntityModel<T> {

    public static RoboWingsModel INSTANCE = new RoboWingsModel();

    private final Cuboid Base;
    private final Cuboid LeftWing;
    private final Cuboid RightWing;

    public RoboWingsModel() {
        textureWidth = 32;
        textureHeight = 32;

        Base = new Cuboid(this);
        Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        Base.boxes.add(new Box(Base, 0, 0, -1.0F, -12.0F, -8.0F, 2, 8, 1, 0.0F, false));
        Base.boxes.add(new Box(Base, 28, 0, 3.0F, -12.0F, -8.0F, 1, 8, 1, 0.0F, false));
        Base.boxes.add(new Box(Base, 28, 0, 4.0F, -12.0F, -7.0F, 1, 8, 1, 0.0F, false));
        Base.boxes.add(new Box(Base, 28, 0, -5.0F, -12.0F, -7.0F, 1, 8, 1, 0.0F, false));
        Base.boxes.add(new Box(Base, 28, 0, -4.0F, -12.0F, -8.0F, 1, 8, 1, 0.0F, false));

        LeftWing = new Cuboid(this);
        LeftWing.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(LeftWing, 0.0F, 0.3491F, 0.0F);
        LeftWing.boxes.add(new Box(LeftWing, 2, 18, -23.658F, -3.0F, -6.9397F, 14, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 0, 22, -19.658F, -4.0F, -6.9397F, 12, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 16, 30, -16.658F, -5.0F, -6.9397F, 6, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 14, 30, -13.658F, -6.0F, -6.9397F, 6, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 16, 30, -10.658F, -7.0F, -6.9397F, 6, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 3, 19, -7.658F, -6.0F, -6.9397F, 3, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 18, 22, -10.658F, -5.0F, -6.9397F, 5, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 2, 17, -18.658F, -10.0F, -6.9397F, 14, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 0, 20, -20.658F, -11.0F, -6.9397F, 16, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 10, 30, -14.658F, -12.0F, -6.9397F, 10, 1, 1, 0.0F, false));
        LeftWing.boxes.add(new Box(LeftWing, 18, 30, -10.658F, -13.0F, -6.9397F, 6, 1, 1, 0.0F, false));

        RightWing = new Cuboid(this);
        RightWing.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(RightWing, 0.0F, -0.3491F, 0.0F);
        RightWing.boxes.add(new Box(RightWing, 0, 22, 9.658F, -3.0F, -6.9397F, 14, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 2, 18, 7.658F, -4.0F, -6.9397F, 12, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 13, 30, 10.658F, -5.0F, -6.9397F, 6, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 16, 30, 7.658F, -6.0F, -6.9397F, 6, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 14, 30, 4.658F, -7.0F, -6.9397F, 6, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 1, 20, 4.658F, -10.0F, -6.9397F, 14, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 0, 18, 4.658F, -11.0F, -6.9397F, 16, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 10, 30, 4.658F, -12.0F, -6.9397F, 10, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 18, 30, 4.658F, -13.0F, -6.9397F, 6, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 15, 22, 5.658F, -5.0F, -6.9397F, 5, 1, 1, 0.0F, false));
        RightWing.boxes.add(new Box(RightWing, 5, 17, 4.658F, -6.0F, -6.9397F, 3, 1, 1, 0.0F, false));
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableCull();

        GlStateManager.translated(0, -0.74, 0.5);
        Base.render(f5);
        GlStateManager.translated(0, 0.74, -0.5);

        GlStateManager.translated(0, 0.8, 0.6);
        LeftWing.render(f5);
        RightWing.render(f5);
        GlStateManager.translated(0, -0.8, -0.6);
    }

    public void setRotationAngle(Cuboid modelRenderer, float x, float y, float z) {
        modelRenderer.rotationPointX = x;
        modelRenderer.rotationPointY = y;
        modelRenderer.rotationPointZ = z;
    }
}