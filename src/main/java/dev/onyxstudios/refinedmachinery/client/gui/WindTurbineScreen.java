package dev.onyxstudios.refinedmachinery.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.client.container.WindTurbineContainer;
import dev.onyxstudios.refinedmachinery.utils.RMEnergyStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class WindTurbineScreen extends BaseScreen<WindTurbineContainer> {

    public static int TEXT_COLOR = 0x039dfc;

    public WindTurbineScreen(Container container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn, new ResourceLocation(RefinedMachinery.MODID, "textures/gui/gui_information.png"));
    }

    @Override
    public void renderGui(MatrixStack matrixStack, int x, int y) {
        renderEnergy(matrixStack, container.getTileEntity(), x, y);
    }

    @Override
    public void renderGuiLast(MatrixStack matrixStack, int x, int y) {
        LazyOptional<IEnergyStorage> energyCapability = container.getTileEntity().getCapability(CapabilityEnergy.ENERGY);
        energyCapability.ifPresent(storage -> {
            String energy = storage.getEnergyStored() + " FE";
            String generating = "Gen: " + container.getTileEntity().getGenAmount() + " FE";
            String extract = "Extract: " + ((RMEnergyStorage) storage).getMaxExtract() + " FE";

            matrixStack.push();
            minecraft.fontRenderer.drawString(matrixStack, energy, guiLeft + 48, guiTop + 29, TEXT_COLOR);
            minecraft.fontRenderer.drawString(matrixStack, generating, guiLeft + 48, guiTop + 39, TEXT_COLOR);
            minecraft.fontRenderer.drawString(matrixStack, extract, guiLeft + 48, guiTop + 49, TEXT_COLOR);
            matrixStack.pop();
        });
    }
}
