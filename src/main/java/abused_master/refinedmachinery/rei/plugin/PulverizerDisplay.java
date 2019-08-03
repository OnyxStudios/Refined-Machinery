package abused_master.refinedmachinery.rei.plugin;

import abused_master.refinedmachinery.registry.recipe.PulverizerRecipe;
import abused_master.refinedmachinery.rei.RefinedMachineryPlugin;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.recipe.Recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PulverizerDisplay implements RecipeDisplay<PulverizerRecipe> {

    private PulverizerRecipe recipe;
    private List<List<ItemStack>> input;
    private int percentDrop;
    private ItemStack randomDrop;

    public PulverizerDisplay(PulverizerRecipe recipe) {
        this.recipe = recipe;
        this.input = recipe.getPreviewInputs().stream().map(i -> Arrays.asList(i.getStackArray())).collect(Collectors.toList());
        this.percentDrop = recipe.getRandomOutputChance();
        this.randomDrop = recipe.getRandomOutput();
    }

    @Override
    public Optional<Recipe<?>> getRecipe() {
        return Optional.empty();
    }

    @Override
    public List<List<ItemStack>> getInput() {
        return input;
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
        return input;
    }

    public int getPercentDrop() {
        return percentDrop;
    }

    public ItemStack getRandomDrop() {
        return randomDrop;
    }
}
