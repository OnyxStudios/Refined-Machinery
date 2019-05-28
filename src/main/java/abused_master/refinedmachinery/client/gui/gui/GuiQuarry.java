package abused_master.refinedmachinery.client.gui.gui;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.client.gui.container.ContainerQuarry;
import abused_master.refinedmachinery.client.gui.widget.QuarryActionWidget;
import abused_master.refinedmachinery.registry.ModPackets;
import abused_master.refinedmachinery.tiles.machine.BlockEntityQuarry;
import com.mojang.blaze3d.platform.GlStateManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class GuiQuarry extends AbstractContainerScreen {

    public static Identifier quarryGui = new Identifier(RefinedMachinery.MODID, "textures/gui/quarry_gui.png");
    public BlockEntityQuarry tile;
    public int guiLeft, guiTop;
    public QuarryActionWidget startButton, stopButton;

    public GuiQuarry(BlockEntityQuarry tile, ContainerQuarry containerQuarry) {
        super(containerQuarry, containerQuarry.playerInventory, new TextComponent("Quarry"));
        this.tile = tile;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.containerWidth) / 2;
        this.guiTop = (this.height - this.containerHeight) / 2;

        this.startButton = this.addButton(new QuarryActionWidget(guiLeft + 60, guiTop + 7, tile.running, "Start", (startButton) -> {
            if(tile.isRunning()) {
                ((QuarryActionWidget) startButton).selected = true;
                stopButton.selected = false;
                return;
            }

            if(tile.canRun()) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeBlockPos(tile.getPos());
                buf.writeInt(0);

                tile.setRunning(true);
                minecraft.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(ModPackets.PACKET_HANDLE_QUARRY, buf));
                ((QuarryActionWidget) startButton).selected = true;
                stopButton.selected = false;
            }
        }));

        this.stopButton = this.addButton(new QuarryActionWidget(guiLeft + 60, guiTop + 24, !tile.running, "Stop", (stopButton) -> {
            if(!tile.isRunning()) {
                ((QuarryActionWidget) stopButton).selected = true;
                startButton.selected = false;
            }

            if(tile.isRunning()) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeBlockPos(tile.getPos());
                buf.writeInt(1);

                tile.setRunning(false);
                minecraft.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(ModPackets.PACKET_HANDLE_QUARRY, buf));
                ((QuarryActionWidget) stopButton).selected = true;
                startButton.selected = false;
            }
        }));
    }


    @Override
    public void render(int var1, int var2, float var3) {
        this.renderBackground();
        super.render(var1, var2, var3);
        this.drawMouseoverTooltip(var1, var2);

        if(this.isPointWithinBounds(10, 10, 14, 42, var1, var2)) {
            this.renderTooltip(tile.storage.getEnergyStored() + " / " + tile.storage.getCapacity() + " CE", var1, var2);
        }
    }

    @Override
    protected void drawForeground(int int_1, int int_2) {
        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.6, 0.6, 0.6);
        String miningPos = tile.isRunning() && tile.miningPos != null ? "X: " + tile.miningPos.getX() + ", Y: " + tile.miningPos.getY() + ", Z: " + tile.miningPos.getZ() : "Not Running";
        this.font.draw(miningPos, 98, 79, 0xFFFFFF);
        GlStateManager.scaled(1, 1, 1);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(quarryGui);
        blit(guiLeft, guiTop, 0, 0, containerWidth, containerHeight);

        renderEnergy();
    }

    public void renderEnergy() {
        if(this.tile.storage.getEnergyStored() > 0) {
            int k = 40;
            int i = tile.storage.getEnergyStored() * k / tile.storage.getCapacity();
            this.blit(guiLeft + 10, guiTop + 50 - i, 178, 44 - i, 12, i);
        }
    }
}
