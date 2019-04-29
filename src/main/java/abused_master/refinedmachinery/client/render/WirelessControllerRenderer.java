package abused_master.refinedmachinery.client.render;

import abused_master.abusedlib.client.render.RenderHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.blocks.transport.BlockWirelessController;
import abused_master.refinedmachinery.registry.ModBlocks;
import abused_master.refinedmachinery.tiles.transport.BlockEntityWirelessController;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class WirelessControllerRenderer extends BlockEntityRenderer<BlockEntityWirelessController> {

    @Override
    public void render(BlockEntityWirelessController tile, double x, double y, double z, float float_1, int int_1) {
        super.render(tile, x, y, z, float_1, int_1);
        MinecraftClient mc = MinecraftClient.getInstance();

        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        mc.getTextureManager().bindTexture(new Identifier(RefinedMachinery.MODID, "textures/blocks/machine_top.png"));

        long angle = (System.currentTimeMillis() / 10) % 360;
        if(!tile.tilePositions.isEmpty() && tile.storage.getEnergyStored() > 0) {
            mc.getTextureManager().bindTexture(new Identifier(RefinedMachinery.MODID, "textures/blocks/energy_charger_front.png"));
            GlStateManager.translated(0.5, 0.5, 0.5);
            GlStateManager.rotatef(angle, 0, 1, 0);
            GlStateManager.translated(-0.5, -0.5, -0.5);
        }

        BakedModel model = mc.getBlockRenderManager().getModel(ModBlocks.WIRELESS_CONTROLLER.getDefaultState().with(BlockWirelessController.STATE, BlockWirelessController.ControllerState.running));
        mc.getBlockRenderManager().getModelRenderer().render(model, 1, 1, 1, 1);
        GlStateManager.popMatrix();

        for (BlockPos pos : tile.tilePositions) {
            RenderHelper.renderLaser(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 120, 0.35F, 0.07, new float[]{85 / 255f, 130 / 255f, 101 / 255f});
        }
    }

    @Override
    public boolean method_3563(BlockEntityWirelessController blockEntity_1) {
        return true;
    }
}
