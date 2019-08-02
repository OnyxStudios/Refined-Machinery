package abused_master.refinedmachinery.client.gui.gui;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.client.gui.container.ContainerCoalGen;
import abused_master.refinedmachinery.tiles.generator.BlockEntityCoalGen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GuiCoalGen extends AbstractContainerScreen {

    public Identifier coalGenGui = new Identifier(RefinedMachinery.MODID, "textures/gui/coalgen_gui.png");
    public BlockEntityCoalGen tile;
    public int guiLeft, guiTop;

    public GuiCoalGen(BlockEntityCoalGen tile, ContainerCoalGen containerCoalGen) {
        super(containerCoalGen, containerCoalGen.playerInventory, new LiteralText("Coal Generator"));
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

        if(this.isPointWithinBounds(10, 10, 14, 42, var1, var2)) {
            this.renderTooltip(tile.storage.getEnergyStored() + " / " + tile.storage.getCapacity() + " CE", var1, var2);
        }
    }

    @Override
    public void drawForeground(int int_1, int int_2) {
        String string_1 = "Coal Generator";
        this.font.draw(string_1, (float)(this.containerWidth / 2 - this.font.getStringWidth(string_1) / 2), 6.0F, 4210752);
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(coalGenGui);
        blit(guiLeft, guiTop, 0, 0, containerWidth, containerHeight);

        renderEnergy();
        renderBurning();
    }

    public void renderBurning() {
        if(this.tile.burnTime > 0) {
            int burnTime = tile.burnTotalTime;
            if (burnTime == 0) {
                burnTime = 200;
            }

            int i = tile.burnTime * 13 / burnTime;
            this.blit(guiLeft + 88, guiTop + 40 + 12 - i, 177, 96 - i, 14, i + 1);
        }
    }

    public void renderEnergy() {
        if(this.tile.storage.getEnergyStored() > 0) {
            int k = 40;
            int i = tile.storage.getEnergyStored() * k / tile.storage.getCapacity();
            this.blit(guiLeft + 10, guiTop + 50 - i, 178, 44 - i, 12, i);
        }
    }
}
