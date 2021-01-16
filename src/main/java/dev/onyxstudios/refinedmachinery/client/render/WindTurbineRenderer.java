package dev.onyxstudios.refinedmachinery.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.refinedmachinery.registry.ModRenders;
import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityWindTurbine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class WindTurbineRenderer extends TileEntityRenderer<TileEntityWindTurbine> {

    public WindTurbineRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TileEntityWindTurbine tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        int blockOffset = tile.getPos().getY() > TileEntityWindTurbine.Y_LEVEL ? tile.getPos().getY() - TileEntityWindTurbine.Y_LEVEL : 0;
        int t = (int) (blockOffset * 0.05);
        long speed = (6 + (tile.getNearbyTurbines())) - t;
        if(speed <= 1)
            speed = 2;

        long angle = (System.currentTimeMillis() / speed) % 360;
        matrixStack.push();
        matrixStack.translate(0.5, 2, 0.5);
        matrixStack.rotate(Vector3f.YN.rotationDegrees(angle));
        matrixStack.translate(-0.5, 0, -0.5);
        BlockModelRenderer modelRenderer = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer();
        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        modelRenderer.renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getCutout()), null, ModRenders.TURBINE_ROTORS_MODEL, 1, 1, 1, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
        matrixStack.pop();
    }
}
