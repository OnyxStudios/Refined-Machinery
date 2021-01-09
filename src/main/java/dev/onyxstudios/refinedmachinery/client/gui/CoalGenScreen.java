package dev.onyxstudios.refinedmachinery.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.client.container.CoalGenContainer;
import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityCoalGen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CoalGenScreen extends BaseMachineScreen<CoalGenContainer> {

    public CoalGenScreen(CoalGenContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn, new ResourceLocation(RefinedMachinery.MODID, "textures/gui/gui_coalgen.png"));
    }

    @Override
    public void renderGui(MatrixStack matrixStack, int x, int y) {
        renderBurning(matrixStack);
    }

    public void renderBurning(MatrixStack stack) {
        if(container.getTileEntity() != null) {
            TileEntityCoalGen coalGen = (TileEntityCoalGen) container.getTileEntity();

            if(coalGen.isBurning) {
                int i = coalGen.burnTime * 13 / coalGen.maxBurnTime;
                this.blit(stack, guiLeft + 80, guiTop + 54 + 12 - i, 176, 12 - i, 14, i + 1);
            }
        }
    }
}
