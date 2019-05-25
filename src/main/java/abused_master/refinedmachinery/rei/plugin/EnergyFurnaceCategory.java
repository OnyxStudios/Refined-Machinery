package abused_master.refinedmachinery.rei.plugin;

import abused_master.refinedmachinery.registry.ModBlocks;
import abused_master.refinedmachinery.rei.RefinedMachineryPlugin;
import com.mojang.blaze3d.platform.GlStateManager;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.Renderable;
import me.shedaniel.rei.api.Renderer;
import me.shedaniel.rei.gui.renderables.RecipeRenderer;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.SlotWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.plugin.DefaultPlugin;
import me.shedaniel.rei.plugin.DefaultSmeltingDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class EnergyFurnaceCategory implements RecipeCategory<EnergyFurnaceDisplay> {

    @Override
    public Identifier getIdentifier() {
        return RefinedMachineryPlugin.ENERGY_FURNACE;
    }

    @Override
    public String getCategoryName() {
        return "Energy Furnace Recipes";
    }

    @Override
    public Renderer getIcon() {
        return Renderable.fromItemStack(new ItemStack(ModBlocks.ENERGY_FURNACE));
    }

    @Override
    public RecipeRenderer getSimpleRenderer(EnergyFurnaceDisplay recipe) {
        return Renderable.fromRecipe(() -> Arrays.asList(recipe.getInput().get(0)), recipe::getOutput);
    }

    @Override
    public List<Widget> setupDisplay(Supplier<EnergyFurnaceDisplay> recipeDisplaySupplier, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point startingPoint = new Point(bounds.x, bounds.y);
        widgets.add(new RecipeBaseWidget(bounds) {
            @Override
            public void render(int mouseX, int mouseY, float delta) {
                super.render(mouseX, mouseY, delta);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(RefinedMachineryPlugin.DISPLAY_TEXTURE);
                blit(startingPoint.x, startingPoint.y, 0, 69, 151, 66);

                int i = MathHelper.ceil((System.currentTimeMillis() / 250 % 24d) / 1f);
                this.blit(startingPoint.x + 72, startingPoint.y + 19, 152, 0, i, 15);
            }
        });

        List<List<ItemStack>> input = recipeDisplaySupplier.get().getInput();
        widgets.add(new SlotWidget(startingPoint.x + 48, startingPoint.y + 19, input.get(0), false, true, true));
        widgets.add(new SlotWidget(startingPoint.x + 108, startingPoint.y + 19, recipeDisplaySupplier.get().getOutput(), false, true, true));

        return widgets;
    }
}
