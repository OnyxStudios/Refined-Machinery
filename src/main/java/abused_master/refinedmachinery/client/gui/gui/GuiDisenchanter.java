package abused_master.refinedmachinery.client.gui.gui;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.client.gui.container.ContainerDisenchanter;
import abused_master.refinedmachinery.tiles.machine.BlockEntityDisenchanter;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GuiDisenchanter extends AbstractContainerScreen {

    public Identifier disenchanterGui = new Identifier(RefinedMachinery.MODID, "textures/gui/disenchanter_gui.png");
    public BlockEntityDisenchanter tile;
    public int guiLeft, guiTop;

    public GuiDisenchanter(BlockEntityDisenchanter tile, ContainerDisenchanter containerFarmer) {
        super(containerFarmer, containerFarmer.playerInventory, new TextComponent("Disenchanter"));
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

        if (this.isPointWithinBounds(10, 9, 20, 64, var1, var2)) {
            this.renderTooltip(tile.storage.getEnergyStored() + " / " + tile.storage.getCapacity() + " CE", var1, var2);
        }

        if (this.isPointWithinBounds(160, 9, 8, 64, var1, var2)) {
            this.renderTooltip(tile.workTime + " / " + tile.requiredTime + " Progress", var1, var2);
        }
    }

    @Override
    public void drawForeground(int int_1, int int_2) {
        String string_1 = "Disenchanter";
        this.font.draw(string_1, (float) (this.containerWidth / 2 - this.font.getStringWidth(string_1) / 2), 6.0F, 4210752);
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(disenchanterGui);
        blit(guiLeft, guiTop, 0, 0, containerWidth, containerHeight);

        renderEnergy();
        renderWork();
    }

    public void renderWork() {
        if(this.tile.workTime > 0) {
            int k = 62;
            int i = tile.workTime * k / tile.requiredTime;
            this.blit(guiLeft + 160, guiTop + 71 - i, 201, 66 - i, 6, i);
        }
    }

    public void renderEnergy() {
        if (this.tile.storage.getEnergyStored() > 0) {
            int k = 62;
            int i = tile.storage.getEnergyStored() * k / tile.storage.getCapacity();
            this.blit(guiLeft + 10, guiTop + 71 - i, 178, 66 - i, 18, i);
        }
    }
}
