package abused_master.refinedmachinery.rei.plugin;

import abused_master.refinedmachinery.registry.PulverizerRecipes;
import abused_master.refinedmachinery.rei.RefinedMachineryPlugin;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PulverizerDisplay implements RecipeDisplay {

    private PulverizerRecipes.PulverizerRecipe recipe;
    private ItemStack input;

    public PulverizerDisplay(ItemStack input, PulverizerRecipes.PulverizerRecipe recipe) {
        this.recipe = recipe;
        this.input = input;
    }

    @Override
    public Optional<PulverizerRecipes.PulverizerRecipe> getRecipe() {
        return Optional.ofNullable(recipe);
    }

    @Override
    public List<List<ItemStack>> getInput() {
        return Collections.singletonList(Collections.singletonList(input));
    }

    @Override
    public List<ItemStack> getOutput() {
        return Collections.singletonList(recipe.getOutput());
    }

    @Override
    public Identifier getRecipeCategory() {
        return RefinedMachineryPlugin.PULVERIZER;
    }

    @Override
    public List<List<ItemStack>> getRequiredItems() {
        return Collections.singletonList(Collections.singletonList(input));
    }
}
