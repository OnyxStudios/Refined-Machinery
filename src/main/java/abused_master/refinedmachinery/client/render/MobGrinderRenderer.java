package abused_master.refinedmachinery.client.render;

import abused_master.abusedlib.client.render.hud.HudRender;
import abused_master.refinedmachinery.tiles.machine.BlockEntityMobGrinder;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class MobGrinderRenderer extends BlockEntityRenderer<BlockEntityMobGrinder> {

    @Override
    public void render(BlockEntityMobGrinder tile, double x, double y, double z, float float_1, int int_1) {
        super.render(tile, x, y, z, float_1, int_1);
        HudRender.renderHud(tile, x, y, z);
    }

    @Override
    public boolean method_3563(BlockEntityMobGrinder blockEntity_1) {
        return true;
    }
}
