package abused_master.refinedmachinery.rei;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.recipe.PulverizerRecipe;
import abused_master.refinedmachinery.rei.plugin.EnergyFurnaceCategory;
import abused_master.refinedmachinery.rei.plugin.EnergyFurnaceDisplay;
import abused_master.refinedmachinery.rei.plugin.PulverizerCategory;
import abused_master.refinedmachinery.rei.plugin.PulverizerDisplay;
import me.shedaniel.rei.api.REIPluginEntry;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Identifier;

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
        recipeHelper.registerRecipes(PULVERIZER, PulverizerRecipe.class, PulverizerDisplay::new);

        for (Recipe recipe : recipeHelper.getAllSortedRecipes()) {
            if(recipe instanceof SmeltingRecipe) {
                recipeHelper.registerDisplay(ENERGY_FURNACE, new EnergyFurnaceDisplay((SmeltingRecipe) recipe));
            }
        }
    }
}
