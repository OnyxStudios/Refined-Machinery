package abused_master.refinedmachinery.client.render;

import abused_master.abusedlib.client.render.RenderHelper;
import abused_master.abusedlib.client.render.hud.HudRender;
import abused_master.refinedmachinery.tiles.tanks.BlockEntityTank;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class TankRenderer extends BlockEntityRenderer<BlockEntityTank> {

    @Override
    public void render(BlockEntityTank blockEntity, double x, double y, double z, float float_1, int int_1) {
        super.render(blockEntity, x, y, z, float_1, int_1);
        if(blockEntity != null) {
            if (blockEntity.tank != null && blockEntity.tank.getFluidStack() != null && blockEntity.tank.getFluidAmount() > 0) {
                HudRender.renderHud(blockEntity, x, y, z);

                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();

                RenderHelper.translateAgainstPlayer(blockEntity.getPos(), false);
                RenderHelper.renderFluid(blockEntity.tank.getFluidStack(), blockEntity.getPos(), 0.06d, 0.08d, 0.06d, 0.01d, 0.0d, 0.01d, 0.87d, (double) blockEntity.tank.getFluidAmount() / (double) blockEntity.tank.getFluidCapacity() * 0.84d, 0.87d);

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean method_3563(BlockEntityTank blockEntity) {
        return true;
    }
}
