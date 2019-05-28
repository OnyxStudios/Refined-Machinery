package abused_master.refinedmachinery.client.gui.widget;

import abused_master.refinedmachinery.client.gui.gui.GuiQuarry;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.MathHelper;

public class QuarryActionWidget extends ButtonWidget {

    public boolean selected;

    public QuarryActionWidget(int x, int y, boolean selected, String text, PressAction pressAction) {
        super(x, y, 56, 12, text, pressAction);
        this.selected = selected;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        MinecraftClient.getInstance().getTextureManager().bindTexture(GuiQuarry.quarryGui);
        this.blit(this.x, this.y, 177, isHovered() ? 62 : selected ? 77 : 48, 56, 12);

        this.drawCenteredString(MinecraftClient.getInstance().textRenderer, getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
