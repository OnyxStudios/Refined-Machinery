package abused_master.refinedmachinery.client.render;

import abused_master.abusedlib.client.render.RenderHelper;
import abused_master.abusedlib.client.render.hud.HudRender;
import abused_master.refinedmachinery.tiles.machine.BlockEntityQuarry;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class QuarryRenderer extends BlockEntityRenderer<BlockEntityQuarry> {

    @Override
    public void render(BlockEntityQuarry tile, double x, double y, double z, float float_1, int int_1) {
        super.render(tile, x, y, z, float_1, int_1);

        if (tile.miningPos != null && tile.miningBlock != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.scaled(0.6, 0.6, 0.6);
            long angle = (System.currentTimeMillis() / 10) % 360;
            GlStateManager.rotatef(angle, 0, 1, 0);
            GlStateManager.alphaFunc(516, 0.003921569F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(tile.miningBlock.getBlock()), ModelTransformation.Type.GROUND);
            GlStateManager.popMatrix();
        }

        if(tile.isRunning()) {
            long angle = (System.currentTimeMillis() / 10) % 360;
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.scaled(0.4, 0.4, 0.4);
            GlStateManager.rotatef(angle, 0, 1, 0);
            GlStateManager.translated(0.4, 0.4, 0.4);
            GlStateManager.alphaFunc(516, 0.003921569F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.DIAMOND_PICKAXE), ModelTransformation.Type.GROUND);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.scaled(0.4, 0.4, 0.4);
            GlStateManager.rotatef(angle, 0, -1, 0);
            GlStateManager.translated(0.4, 0.4, 0.4);
            GlStateManager.alphaFunc(516, 0.003921569F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.DIAMOND_PICKAXE), ModelTransformation.Type.GROUND);
            GlStateManager.popMatrix();
        }

        if (tile.listFourCorners() != null) {
            BlockPos first = tile.listFourCorners()[0];
            BlockPos second = tile.listFourCorners()[1];
            BlockPos third = tile.listFourCorners()[2];
            BlockPos fourth = tile.listFourCorners()[3];
            int y1 = tile.secondCorner.getY();

            double beamWidth = 0.07;
            float[] beamColor = new float[] {0, 191 / 255f, 255 / 255f};

            //Render 4 Beams
            RenderHelper.renderLaser(first.getX() + 0.5, first.getY() + 0.5, first.getZ() + 0.5, third.getX() + 0.5, third.getY() + 0.5, third.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);
            RenderHelper.renderLaser(first.getX() + 0.5, first.getY() + 0.5, first.getZ() + 0.5, second.getX() + 0.5, second.getY() + 0.5, second.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);
            RenderHelper.renderLaser(second.getX() + 0.5, second.getY() + 0.5, second.getZ() + 0.5, fourth.getX() + 0.5, fourth.getY() + 0.5, fourth.getZ() + 0.5, 120, 0.35F, beamWidth,  beamColor);
            RenderHelper.renderLaser(third.getX() + 0.5, third.getY() + 0.5, third.getZ() + 0.5, fourth.getX() + 0.5, fourth.getY() + 0.5, fourth.getZ() + 0.5, 120, 0.35F, beamWidth,  beamColor);

            //Render 4 beams down
            RenderHelper.renderLaser(first.getX() + 0.5, first.getY() + 0.5, first.getZ() + 0.5, first.getX() + 0.5, y1 + 0.5, first.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);
            RenderHelper.renderLaser(second.getX() + 0.5, second.getY() + 0.5, second.getZ() + 0.5, second.getX() + 0.5, y1 + 0.5, second.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);
            RenderHelper.renderLaser(third.getX() + 0.5, third.getY() + 0.5, third.getZ() + 0.5, third.getX() + 0.5, y1 + 0.5, third.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);
            RenderHelper.renderLaser(fourth.getX() + 0.5, fourth.getY() + 0.5, fourth.getZ() + 0.5, fourth.getX() + 0.5, y1 + 0.5, fourth.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);

            RenderHelper.renderLaser(first.getX() + 0.5, y1 + 0.5, first.getZ() + 0.5, third.getX() + 0.5, y1 + 0.5, third.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);
            RenderHelper.renderLaser(first.getX() + 0.5, y1 + 0.5, first.getZ() + 0.5, second.getX() + 0.5, y1 + 0.5, second.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);
            RenderHelper.renderLaser(second.getX() + 0.5, y1 + 0.5, second.getZ() + 0.5, fourth.getX() + 0.5, y1 + 0.5, fourth.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);
            RenderHelper.renderLaser(third.getX() + 0.5, y1 + 0.5, third.getZ() + 0.5, fourth.getX() + 0.5, y1 + 0.5, fourth.getZ() + 0.5, 120, 0.35F, beamWidth, beamColor);

            //Render 4 model pillars
            RenderHelper.renderLaser(tile.getPos().getX() + 0.025, tile.getPos().getY() + 0.25, tile.getPos().getZ() + 0.025, tile.getPos().getX() + 0.025, tile.getPos().getY() + 0.75, tile.getPos().getZ() + 0.025, 120, 0.35f, 0.05, beamColor);
            RenderHelper.renderLaser(tile.getPos().getX() + 0.975, tile.getPos().getY() + 0.25, tile.getPos().getZ() + 0.025, tile.getPos().getX() + 0.975, tile.getPos().getY() + 0.75, tile.getPos().getZ() + 0.025, 120, 0.35f, 0.05, beamColor);
            RenderHelper.renderLaser(tile.getPos().getX() + 0.025, tile.getPos().getY() + 0.25, tile.getPos().getZ() + 0.975, tile.getPos().getX() + 0.025, tile.getPos().getY() + 0.75, tile.getPos().getZ() + 0.975, 120, 0.35f, 0.05, beamColor);
            RenderHelper.renderLaser(tile.getPos().getX() + 0.975, tile.getPos().getY() + 0.25, tile.getPos().getZ() + 0.975, tile.getPos().getX() + 0.975, tile.getPos().getY() + 0.75, tile.getPos().getZ() + 0.975, 120, 0.35f, 0.05, beamColor);
        }
    }

    @Override
    public boolean method_3563(BlockEntityQuarry blockEntity_1) {
        return true;
    }
}
