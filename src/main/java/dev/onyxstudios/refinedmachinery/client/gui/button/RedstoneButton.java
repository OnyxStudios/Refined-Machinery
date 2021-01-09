package dev.onyxstudios.refinedmachinery.client.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class RedstoneButton extends Button {

    public ResourceLocation screenLocation;
    public boolean requiresRedstone;

    public RedstoneButton(int x, int y, int width, int height, ITextComponent title, ResourceLocation screenLocation, boolean requiresRedstone, IPressable pressedAction) {
        super(x, y, width, height, title, pressedAction);
        this.screenLocation = screenLocation;
        this.requiresRedstone = requiresRedstone;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(screenLocation);
        blit(matrixStack, x + 3, y + 4, 177, requiresRedstone ? 27 : 15, 12, 12);

        if(this.isHovered()) {
            List<StringTextComponent> tooltip = new ArrayList<>();
            tooltip.add(new StringTextComponent(requiresRedstone ? "Requires Redstone" : "Ignores Redstone"));
            GuiUtils.drawHoveringText(matrixStack, tooltip, x, y, Minecraft.getInstance().currentScreen.width, Minecraft.getInstance().currentScreen.height, -1, Minecraft.getInstance().fontRenderer);
        }
    }
}
