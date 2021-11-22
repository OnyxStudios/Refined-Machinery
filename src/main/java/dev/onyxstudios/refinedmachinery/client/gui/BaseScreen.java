package dev.onyxstudios.refinedmachinery.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen<T extends Container> extends ContainerScreen<T> {

    public ResourceLocation MACHINE_GUI;
    public int powerBarX = 8;
    public int powerBarY = 67;
    public int powerBarWidth = 13;
    public int powerBarHeight = 58;
    public int powerBarU = 191;

    public BaseScreen(Container container, PlayerInventory inv, ITextComponent titleIn, ResourceLocation screenLocation) {
        super((T) container, inv, titleIn);
        this.MACHINE_GUI = screenLocation;
    }

    @Override
    public void init() {
        super.init();
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        getMinecraft().getTextureManager().bindTexture(MACHINE_GUI);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
        renderGui(matrixStack, x, y);
        renderGuiLast(matrixStack, x, y);
    }

    public abstract void renderGui(MatrixStack matrixStack, int x, int y);
    public abstract void renderGuiLast(MatrixStack matrixStack, int x, int y);

    public void renderEnergy(MatrixStack stack, TileEntity tile, int x, int y) {
        renderEnergyBar(stack, tile);
        renderEnergyText(stack, tile, x, y);
    }

    public void renderEnergyBar(MatrixStack stack, TileEntity tile) {
        LazyOptional<IEnergyStorage> energyCapability = tile.getCapability(CapabilityEnergy.ENERGY);
        energyCapability.ifPresent(storage -> {
            int i = powerBarHeight;
            int j = 0;
            if(storage.getEnergyStored() > 0)
                j = storage.getEnergyStored() * i / storage.getMaxEnergyStored();

            blit(stack, guiLeft + powerBarX, guiTop + powerBarY - j, powerBarU, powerBarHeight - j, powerBarWidth, j);
        });
    }

    public void renderEnergyText(MatrixStack stack, TileEntity tile, int x, int y) {
        LazyOptional<IEnergyStorage> energyCapability = tile.getCapability(CapabilityEnergy.ENERGY);
        energyCapability.ifPresent(storage -> {
            if(this.isPointInRegion(powerBarX - 1, powerBarY - powerBarHeight, powerBarWidth, powerBarHeight, x, y)) {
                List<StringTextComponent> energy = new ArrayList<>();
                energy.add(new StringTextComponent(storage.getEnergyStored() + " / " + storage.getMaxEnergyStored() + "  FE"));
                GuiUtils.drawHoveringText(stack, energy, x, y, getMinecraft().currentScreen.width, getMinecraft().currentScreen.height, -1, getMinecraft().fontRenderer);
            }
        });
    }
}
