package dev.onyxstudios.refinedmachinery.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.onyxstudios.refinedmachinery.client.container.BaseMachineContainer;
import dev.onyxstudios.refinedmachinery.client.gui.button.RedstoneButton;
import dev.onyxstudios.refinedmachinery.client.gui.button.SettingsButton;
import dev.onyxstudios.refinedmachinery.client.render.scene.MachineSettingsScene;
import dev.onyxstudios.refinedmachinery.network.ConfigureMachineMessage;
import dev.onyxstudios.refinedmachinery.network.ModPackets;
import dev.onyxstudios.refinedmachinery.network.OpenSettingsMessage;
import dev.onyxstudios.refinedmachinery.network.UpdateRedstoneMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMachineScreen<T extends BaseMachineContainer> extends ContainerScreen<T> {

    public ResourceLocation MACHINE_GUI;
    public int powerBarX = 8;
    public int powerBarY = 67;
    public int powerBarWidth = 13;
    public int powerBarHeight = 58;

    public int redstoneButtonX = 151;
    public int redstoneButtonY = 8;

    public int settingsButtonX = 152;
    public int settingsButtonY = 34;

    public int buttonWidth = 18;
    public int buttonHeight = 18;

    public RedstoneButton redstoneButton;
    public SettingsButton settingsButton;

    public MachineSettingsScene scene;

    public BaseMachineScreen(BaseMachineContainer container, PlayerInventory inv, ITextComponent titleIn, ResourceLocation screenLocation) {
        super((T) container, inv, titleIn);
        this.MACHINE_GUI = screenLocation;
    }

    public abstract void renderGui(MatrixStack matrixStack, int x, int y);

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
        this.redstoneButton = this.addButton(new RedstoneButton(
                guiLeft + redstoneButtonX,
                guiTop + redstoneButtonY,
                buttonWidth,
                buttonHeight,
                new StringTextComponent(""),
                MACHINE_GUI,
                container.getTileEntity().isRedstoneRequired(),
                btn -> {
                    boolean requiresRedstone = !container.getTileEntity().isRedstoneRequired();
                    ((RedstoneButton) btn).requiresRedstone = requiresRedstone;
                    ModPackets.INSTANCE.sendToServer(new UpdateRedstoneMessage(container.getTileEntity().getPos(), requiresRedstone));
                    container.getTileEntity().setRequiresRedstone(requiresRedstone);
                }));

        this.settingsButton = this.addButton(new SettingsButton(
                guiLeft + settingsButtonX,
                guiTop + settingsButtonY,
                buttonWidth,
                buttonHeight,
                new StringTextComponent(""),
                MACHINE_GUI,
                btn -> {
                    boolean isOpen = !container.getTileEntity().isSettingsOpen();
                    ModPackets.INSTANCE.sendToServer(new OpenSettingsMessage(container.getTileEntity().getPos(), isOpen));
                    container.getTileEntity().setSettingsOpen(isOpen);
                    container.isSettingsOpen = isOpen;
                    container.togglePlayerSlots();
                }
        ));

        scene = new MachineSettingsScene();
        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = container.getTileEntity().getPos().offset(direction);
            scene.addNeighbor(container.getTileEntity().getPos().subtract(offsetPos), getMinecraft().world.getBlockState(offsetPos));
        }
        scene.init(container.getTileEntity().getBlockState(), container.getTileEntity());
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        getMinecraft().getTextureManager().bindTexture(MACHINE_GUI);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        renderGui(matrixStack, x, y);
        renderEnergy(matrixStack, x, y);

        if(container.getTileEntity().isSettingsOpen()) {
            renderSettingsOverlay(matrixStack);
        }
    }

    public void renderSettingsOverlay(MatrixStack matrixStack) {
        blit(matrixStack, guiLeft + 7, guiTop + 83, 7, 168, 162, 76);
        scene.renderSceneBuffer();
        RenderSystem.bindTexture(scene.framebuffer.getTextureId());
        innerBlit(matrixStack.getLast().getMatrix(), guiLeft + 7, guiLeft + 7 + 162, guiTop + 83, guiTop + 83 + 76, this.getBlitOffset(), 0, 1, 1, 0);
    }

    public void renderEnergy(MatrixStack stack, int x, int y) {
        LazyOptional<IEnergyStorage> energyCapability = container.getTileEntity().getCapability(CapabilityEnergy.ENERGY);
        energyCapability.ifPresent(storage -> {
            int i = 58;
            int j = storage.getEnergyStored() * i / storage.getMaxEnergyStored();
            blit(stack, guiLeft + powerBarX, guiTop + powerBarY - j, 191, powerBarHeight - j, 13, j);

            if(this.isPointInRegion(powerBarX - 1, powerBarY - powerBarHeight, powerBarWidth, powerBarHeight, x, y)) {
                List<StringTextComponent> energy = new ArrayList<>();
                energy.add(new StringTextComponent(storage.getEnergyStored() + " / " + storage.getMaxEnergyStored() + "  FE"));
                GuiUtils.drawHoveringText(stack, energy, x, y, getMinecraft().currentScreen.width, getMinecraft().currentScreen.height, -1, getMinecraft().fontRenderer);
            }
        });
    }

    @Override
    public void onClose() {
        super.onClose();
        ModPackets.INSTANCE.sendToServer(new OpenSettingsMessage(container.getTileEntity().getPos(), false));
        container.getTileEntity().setSettingsOpen(false);
        container.isSettingsOpen = false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(this.isPointInRegion(6, 82, 163, 77, mouseX, mouseY) && (button == 1 || button == 2) && container.getTileEntity().isSettingsOpen()) {
            scene.onMouseDragged(dragX, dragY);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isPointInRegion(6, 82, 163, 77, mouseX, mouseY) && button == 0 && container.getTileEntity().isSettingsOpen()) {
            int mx = (int) (mouseX - (guiLeft + 7));
            int my = (int) (mouseY - (guiTop + 83));

            Direction direction = scene.getDirectionClicked(mx, my);
            if(direction != null) {
                ModPackets.INSTANCE.sendToServer(new ConfigureMachineMessage(container.getTileEntity().getPos(), direction));
                container.getTileEntity().updateConfiguration(direction);
                container.getTileEntity().requestModelDataUpdate();
                scene.updateMachineState(getMinecraft().world.getBlockState(container.getTileEntity().getPos()), container.getTileEntity());
                getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
