package abused_master.refinedmachinery.rei;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.PulverizerRecipes;
import abused_master.refinedmachinery.rei.plugin.EnergyFurnaceCategory;
import abused_master.refinedmachinery.rei.plugin.EnergyFurnaceDisplay;
import abused_master.refinedmachinery.rei.plugin.PulverizerCategory;
import abused_master.refinedmachinery.rei.plugin.PulverizerDisplay;
import me.shedaniel.rei.api.REIPluginEntry;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.plugin.DefaultPlugin;
import me.shedaniel.rei.plugin.DefaultSmeltingDisplay;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RefinedMachineryPlugin implements REIPluginEntry {

    public static final Identifier DISPLAY_TEXTURE = new Identifier(RefinedMachinery.MODID, "textures/gui/rei/display.png");

    public static final Identifier PLUGIN = new Identifier(RefinedMachinery.MODID, "refined_machinery_plugin");
    public static final Identifier PULVERIZER = new Identifier(RefinedMachinery.MODID, "plugins/pulverizer");
    public static final Identifier ENERGY_FURNACE = new Identifier(RefinedMachinery.MODID, "plugins/energy_furnace");

    @Override
    public Identifier getPluginIdentifier() {
        return PLUGIN;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new PulverizerCategory());
        recipeHelper.registerCategory(new EnergyFurnaceCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        for (Map.Entry<Object, PulverizerRecipes.PulverizerRecipe> entry : PulverizerRecipes.INSTANCE.getRecipes().entrySet()) {
            if (entry.getKey() instanceof ItemStack) {
                recipeHelper.registerDisplay(PULVERIZER, new PulverizerDisplay(Collections.singletonList((ItemStack) entry.getKey()), entry.getValue()));
            } else if (entry.getKey() instanceof Tag) {
                List<ItemStack> inputs = new ArrayList<>();
                for (Item item : ((Tag<Item>) entry.getKey()).values()) {
                    inputs.add(new ItemStack(item));
                }
                recipeHelper.registerDisplay(PULVERIZER, new PulverizerDisplay(inputs, entry.getValue()));
            }
        }

        for (Recipe recipe : recipeHelper.getAllSortedRecipes()) {
            if(recipe instanceof SmeltingRecipe) {
                recipeHelper.registerDisplay(ENERGY_FURNACE, new EnergyFurnaceDisplay((SmeltingRecipe) recipe));
            }
        }
    }
}
