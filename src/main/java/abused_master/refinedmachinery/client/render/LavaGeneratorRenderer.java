package abused_master.refinedmachinery.client.render;

import abused_master.abusedlib.client.render.RenderHelper;
import abused_master.abusedlib.client.render.hud.HudRender;
import abused_master.refinedmachinery.tiles.generator.BlockEntityLavaGenerator;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class LavaGeneratorRenderer extends BlockEntityRenderer<BlockEntityLavaGenerator> {

    @Override
    public void render(BlockEntityLavaGenerator tile, double x, double y, double z, float float_1, int int_1) {
        super.render(tile, x, y, z, float_1, int_1);
        HudRender.renderHud(tile, x, y, z);

        if (tile.tank.getFluidStack() != null && tile.tank.getFluidAmount() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            RenderHelper.translateAgainstPlayer(tile.getPos(), false);
            RenderHelper.renderFluid(tile.tank.getFluidStack(), tile.getPos(), 0.20d, 0.26d, 0.20d, 0.01d, 0.0d, 0.01d, 0.59d, (double) tile.tank.getFluidAmount() / (double) tile.tank.getFluidCapacity() * 0.46d, 0.59d);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean method_3563(BlockEntityLavaGenerator blockEntity_1) {
        return true;
    }
}
