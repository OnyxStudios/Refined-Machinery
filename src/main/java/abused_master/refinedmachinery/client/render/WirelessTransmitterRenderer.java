package abused_master.refinedmachinery.client.render;

import abused_master.abusedlib.client.render.RenderHelper;
import abused_master.refinedmachinery.tiles.transport.BlockEntityWirelessTransmitter;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class WirelessTransmitterRenderer extends BlockEntityRenderer<BlockEntityWirelessTransmitter> {

    @Override
    public void render(BlockEntityWirelessTransmitter tile, double x, double y, double z, float float_1, int int_1) {
        super.render(tile, x, y, z, float_1, int_1);
        if(tile.getCrystalPos() != null) {
            RenderHelper.renderLaser(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5, tile.getCrystalPos().getX() + 0.5, tile.getCrystalPos().getY() + 0.5, tile.getCrystalPos().getZ() + 0.5, 120, 0.35F, 0.07, new float[] {104 / 255f, 163 / 255f, 124 / 255f});
        }
    }

    @Override
    public boolean method_3563(BlockEntityWirelessTransmitter blockEntity_1) {
        return true;
    }
}
