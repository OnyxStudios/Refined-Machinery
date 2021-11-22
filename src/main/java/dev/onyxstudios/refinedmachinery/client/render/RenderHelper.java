package dev.onyxstudios.refinedmachinery.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    public static final int MAX_LIGHT_X = 0xF000F0;
    public static final int MAX_LIGHT_Y = 0xF000F0;

    public static void renderFluid(MatrixStack matrixStack, FluidTank tank, int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null)
            return;

        int i = tank.getFluidAmount() * height / tank.getCapacity();
        int color = tank.getFluid().getFluid().getAttributes().getColor();
        int brightness = mc.world.getLightSubtracted(new BlockPos(x, y, 0), tank.getFluid().getFluid().getAttributes().getLuminosity());
        TextureAtlasSprite still = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(tank.getFluid().getFluid().getAttributes().getStillTexture());

        matrixStack.push();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        mc.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);

        addTexturedQuad(buffer, still, x, y - i, width, i, color, brightness);
        tessellator.draw();

        GlStateManager.disableBlend();
        matrixStack.pop();
    }

    public static void addTexturedQuad(BufferBuilder buffer, TextureAtlasSprite sprite, double x, double y, double width, double height, int color, int brightness) {
        if (sprite == null) {
            return;
        }

        int firstLightValue = brightness >> 0x10 & 0xFFFF;
        int secondLightValue = brightness & 0xFFFF;
        int alpha = color >> 24 & 0xFF;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;

        addTextureQuad(buffer, sprite, x, y, width, height, red, green, blue, alpha, firstLightValue, secondLightValue);
    }

    public static void addTextureQuad(BufferBuilder buffer, TextureAtlasSprite sprite, double x, double y, double width, double height, int red, int green, int blue, int alpha, int light1, int light2) {
        double minU;
        double maxU;
        double minV;
        double maxV;

        double x2 = x + width;
        double y2 = y + height;

        double u = x % 1d;
        double u1 = u + width;

        while (u1 > 1f) {
            u1 -= 1f;
        }

        double vy = y % 1d;
        double vy1 = vy + height;

        while (vy1 > 1f) {
            vy1 -= 1f;
        }

        minU = sprite.getMinU();
        maxU = sprite.getMaxU();
        minV = sprite.getMinV();
        maxV = sprite.getMaxV();

        buffer.pos(x, y, 0).color(red, green, blue, alpha).tex((float) minU, (float) maxV).lightmap(light1, light2).endVertex();
        buffer.pos(x, y2, 0).color(red, green, blue, alpha).tex((float) minU, (float) minV).lightmap(light1, light2).endVertex();
        buffer.pos(x2, y2, 0).color(red, green, blue, alpha).tex((float) maxU, (float) minV).lightmap(light1, light2).endVertex();
        buffer.pos(x2, y, 0).color(red, green, blue, alpha).tex((float) maxU, (float) maxV).lightmap(light1, light2).endVertex();
    }

    public static void renderLaser(MatrixStack stack, IRenderTypeBuffer buffers, double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ, double rotationTime, float alpha, double beamWidth, float[] color) {
        World world = Minecraft.getInstance().world;
        if (world == null)
            return;

        float r = color[0];
        float g = color[1];
        float b = color[2];

        Vector3d vec1 = new Vector3d(firstX, firstY, firstZ);
        Vector3d vec2 = new Vector3d(secondX, secondY, secondZ);
        Vector3d combinedVec = vec2.subtract(vec1);

        double rot = rotationTime > 0 ? (360D * ((world.getGameTime() % rotationTime) / rotationTime)) : 0;
        double pitch = Math.atan2(combinedVec.y, Math.sqrt(combinedVec.x * combinedVec.x + combinedVec.z * combinedVec.z));
        double yaw = Math.atan2(-combinedVec.z, combinedVec.x);
        double length = combinedVec.length();


        Vector3d renderOffset = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        stack.push();

        stack.translate(firstX - renderOffset.x, firstY - renderOffset.y, firstZ - renderOffset.z);
        stack.rotate(Vector3f.YP.rotationDegrees((float) (180 * yaw / Math.PI)));
        stack.rotate(Vector3f.ZP.rotationDegrees((float) (180 * pitch / Math.PI)));
        stack.rotate(Vector3f.XP.rotationDegrees((float) rot));

        IVertexBuilder buffer = buffers.getBuffer(getLaserInstance(color));
        for (double i = 0; i < 4; i++) {
            double size = beamWidth * (i / 4.0);
            buffer.pos(length, size, size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(0, size, size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(0, -size, size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(length, -size, size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            buffer.pos(length, -size, -size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(0, -size, -size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(0, size, -size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(length, size, -size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            buffer.pos(length, size, -size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(0, size, -size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(0, size, size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(length, size, size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();

            buffer.pos(length, -size, size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(0, -size, size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(0, -size, -size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
            buffer.pos(length, -size, -size).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
        }
        stack.pop();
    }

    public static RenderType getLaserInstance(float[] color) {
        return RenderType.makeType(
                "laser_layer" + color[0] + color[1] + color[2],
                DefaultVertexFormats.POSITION_TEX_LIGHTMAP_COLOR,
                GL11.GL_QUADS,
                256,
                true,
                true,
                RenderType.State.getBuilder()
                        .shadeModel(new RenderState.ShadeModelState(false))
                        .transparency(new RenderState.TransparencyState("laser_transparency", () -> {
                            GlStateManager.disableLighting();
                            GlStateManager.enableBlend();
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

                            GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
                            GlStateManager.disableTexture();
                        }, () -> {
                            int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
                            float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);

                            GlStateManager.enableTexture();
                            GlStateManager.alphaFunc(func, ref);
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            GlStateManager.disableBlend();
                            GlStateManager.enableLighting();
                        })).build(false)

        );
    }
}
