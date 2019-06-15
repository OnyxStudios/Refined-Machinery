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
    private List<ItemStack> inputs;

    public PulverizerDisplay(List<ItemStack> inputs, PulverizerRecipes.PulverizerRecipe recipe) {
        this.recipe = recipe;
        this.inputs = inputs;
    }

    @Override
    public Optional<PulverizerRecipes.PulverizerRecipe> getRecipe() {
        return Optional.empty();
    }

    @Override
    public List<List<ItemStack>> getInput() {
        return Collections.singletonList(inputs);
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
        return Collections.singletonList(inputs);
    }
}
