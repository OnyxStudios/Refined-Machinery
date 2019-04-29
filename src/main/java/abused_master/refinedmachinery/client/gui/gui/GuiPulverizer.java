package abused_master.refinedmachinery.client.gui.gui;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.client.gui.container.ContainerPulverizer;
import abused_master.refinedmachinery.tiles.machine.BlockEntityPulverizer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GuiPulverizer extends ContainerScreen {

    public Identifier pulverizerGui = new Identifier(RefinedMachinery.MODID, "textures/gui/pulverizer_gui.png");
    public BlockEntityPulverizer tile;
    public int guiLeft, guiTop;

    public GuiPulverizer(BlockEntityPulverizer tile, ContainerPulverizer containerPulverizer) {
        super(containerPulverizer, containerPulverizer.playerInventory, new StringTextComponent("Pulverizer"));
        this.tile = tile;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.containerWidth) / 2;
        this.guiTop = (this.height - this.containerHeight) / 2;
    }

    @Override
    public void render(int var1, int var2, float var3) {
        this.renderBackground();
        super.render(var1, var2, var3);
        this.drawMouseoverTooltip(var1, var2);
    }

    @Override
    public void drawForeground(int int_1, int int_2) {
        String string_1 = "Pulverizer";
        this.font.draw(string_1, (float)(this.containerWidth / 2 - this.font.getStringWidth(string_1) / 2), 6.0F, 4210752);
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(pulverizerGui);
        blit(guiLeft, guiTop, 0, 0, containerWidth, containerHeight);

        renderEnergy(guiLeft, guiTop);
        renderProgress(guiLeft, guiTop);

        if(this.isPointWithinBounds(10, 9, 20, 64, i, i1)) {
            this.renderTooltip(tile.storage.getEnergyStored() + " / " + tile.storage.getEnergyCapacity() + " CE", i, i1);
        }
    }

    public void renderEnergy(int guiLeft, int guiTop) {
        if(this.tile.storage.getEnergyStored() > 0) {
            int k = 62;
            int i = tile.storage.getEnergyStored() * k / tile.storage.getEnergyCapacity();
            this.blit(guiLeft + 10, guiTop + 71 - i, 178, 66 - i, 18, i);
        }
    }

    public void renderProgress(int guiLeft, int guiTop) {
        if(tile.getPulverizeTime() > 0) {
            int k = 22;
            int i = tile.getPulverizeTime() * k / tile.getTotalPulverizeTime();
            this.blit(guiLeft + 80, guiTop + 26, 177, 68, i, 15);
        }
    }
}
