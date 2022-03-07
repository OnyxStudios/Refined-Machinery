package dev.onyxstudios.refinedmachinery.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.client.container.BaseMachineContainer;
import dev.onyxstudios.refinedmachinery.client.container.LavaGenContainer;
import dev.onyxstudios.refinedmachinery.client.render.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class LavaGenScreen extends BaseMachineScreen<LavaGenContainer> {

    public int fluidBarX = 27;
    public int fluidBarY = 67;
    public int fluidBarWidth = 13;
    public int fluidBarHeight = 25;

    public LavaGenScreen(BaseMachineContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn, new ResourceLocation(RefinedMachinery.MODID, "textures/gui/gui_lavagen.png"));
    }

    @Override
    public void renderGui(MatrixStack matrixStack, int x, int y) {
        renderFluidBar(matrixStack, container.getTileEntity(), x, y);
    }

    public void renderFluidBar(MatrixStack stack, TileEntity tile, int x, int y) {
        LazyOptional<IFluidHandler> fluidCapability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        fluidCapability.ifPresent(fluidHandler -> {
            if(fluidHandler instanceof FluidTank) {
                FluidTank tank = (FluidTank) fluidHandler;
                RenderHelper.renderFluid(stack, tank, guiLeft + fluidBarX, guiLeft + fluidBarY, fluidBarWidth, fluidBarHeight);

                if(this.isPointInRegion(fluidBarX - 1, fluidBarY - fluidBarHeight, fluidBarWidth, fluidBarHeight, x, y)) {
                    List<StringTextComponent> energy = new ArrayList<>();
                    energy.add(new StringTextComponent(tank.getFluidAmount() + " / " + tank.getCapacity() + "  MB"));
                    GuiUtils.drawHoveringText(stack, energy, x, y, getMinecraft().currentScreen.width, getMinecraft().currentScreen.height, -1, getMinecraft().fontRenderer);
                }
            }
        });
    }
}
