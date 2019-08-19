package abused_master.refinedmachinery.rei.plugin;

import abused_master.refinedmachinery.registry.ModBlocks;
import abused_master.refinedmachinery.rei.RefinedMachineryPlugin;
import com.mojang.blaze3d.platform.GlStateManager;
import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.Renderer;
import me.shedaniel.rei.gui.renderers.RecipeRenderer;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.SlotWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class PulverizerCategory implements RecipeCategory<PulverizerDisplay> {

    @Override
    public Identifier getIdentifier() {
        return RefinedMachineryPlugin.PULVERIZER;
    }

    @Override
    public String getCategoryName() {
        return "Pulverizer Recipes";
    }

    @Override
    public Renderer getIcon() {
        return Renderer.fromItemStack(new ItemStack(ModBlocks.PULVERIZER));
    }

    @Override
    public RecipeRenderer getSimpleRenderer(PulverizerDisplay recipe) {
        return Renderer.fromRecipe(() -> Arrays.asList(recipe.getInput().get(0)), recipe::getOutput);
    }

    @Override
    public List<Widget> setupDisplay(Supplier<PulverizerDisplay> recipeDisplaySupplier, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point startingPoint = new Point(bounds.x, bounds.y);
        widgets.add(new RecipeBaseWidget(bounds) {
            @Override
            public void render(int mouseX, int mouseY, float delta) {
                super.render(mouseX, mouseY, delta);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(RefinedMachineryPlugin.DISPLAY_TEXTURE);
                blit(startingPoint.x, startingPoint.y, 0, 0, 151, 66);

                int i = MathHelper.ceil((System.currentTimeMillis() / 250 % 24d) / 1f);
                this.blit(startingPoint.x + 72, startingPoint.y + 19, 152, 0, i, 15);

                int percent = recipeDisplaySupplier.get().getPercentDrop();
                if(percent > 0)
                    font.draw(percent + "%", startingPoint.x + 134, startingPoint.y + 38, 4210752);
            }
        });

        List<List<ItemStack>> input = recipeDisplaySupplier.get().getInput();
        widgets.add(new SlotWidget(startingPoint.x + 48, startingPoint.y + 19, Renderer.fromItemStacks(input.get(0)), false, true, true));
        widgets.add(new SlotWidget(startingPoint.x + 108, startingPoint.y + 19, Renderer.fromItemStacks(recipeDisplaySupplier.get().getOutput()), false, true, true));
        widgets.add(new SlotWidget(startingPoint.x + 133, startingPoint.y + 19, Renderer.fromItemStack(recipeDisplaySupplier.get().getRandomDrop()), false, true, true));

        return widgets;
    }
}
