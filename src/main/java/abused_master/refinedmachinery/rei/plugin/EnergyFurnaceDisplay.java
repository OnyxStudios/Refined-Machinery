package abused_master.refinedmachinery.rei.plugin;

import abused_master.refinedmachinery.rei.RefinedMachineryPlugin;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnergyFurnaceDisplay implements RecipeDisplay {

    private SmeltingRecipe recipe;
    private List<List<ItemStack>> input;
    private List<ItemStack> output;

    public EnergyFurnaceDisplay(SmeltingRecipe recipe) {
        this.recipe = recipe;
        this.input = recipe.getPreviewInputs().stream().map((i) -> Arrays.asList(i.getStackArray())).collect(Collectors.toList());
        this.output = Collections.singletonList(recipe.getOutput());
    }

    @Override
    public Optional<SmeltingRecipe> getRecipe() {
        return Optional.of(recipe);
    }

    @Override
    public List<List<ItemStack>> getInput() {
        return input;
    }

    @Override
    public List<ItemStack> getOutput() {
        return output;
    }

    @Override
    public Identifier getRecipeCategory() {
        return RefinedMachineryPlugin.ENERGY_FURNACE;
    }
}
