package abused_master.refinedmachinery.client.gui.gui;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.client.gui.container.ContainerVacuum;
import abused_master.refinedmachinery.tiles.machine.BlockEntityVacuum;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GuiVacuum extends ContainerScreen {

    public Identifier vacuumGui = new Identifier(RefinedMachinery.MODID, "textures/gui/vacuum_gui.png");
    public BlockEntityVacuum tile;
    public int guiLeft, guiTop;

    public GuiVacuum(BlockEntityVacuum tile, ContainerVacuum containerVacuum) {
        super(containerVacuum, containerVacuum.playerInventory, new TextComponent("Vacuum"));
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
        String string_1 = "Vacuum";
        this.font.draw(string_1, (float)(this.containerWidth / 2 - this.font.getStringWidth(string_1) / 2), 6.0F, 4210752);
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(vacuumGui);
        blit(guiLeft, guiTop, 0, 0, containerWidth, containerHeight);
    }
}
