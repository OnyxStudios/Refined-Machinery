package abused_master.refinedmachinery.rei;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.PulverizerRecipes;
import abused_master.refinedmachinery.rei.plugin.PulverizerCategory;
import abused_master.refinedmachinery.rei.plugin.PulverizerDisplay;
import me.shedaniel.rei.api.REIPluginEntry;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Map;

public class RefinedMachineryPlugin implements REIPluginEntry {

    public static final Identifier PLUGIN = new Identifier(RefinedMachinery.MODID, "refined_machinery_plugin");
    public static final Identifier PULVERIZER = new Identifier(RefinedMachinery.MODID, "plugins/pulverizer");

    @Override
    public Identifier getPluginIdentifier() {
        return PLUGIN;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new PulverizerCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        for (Map.Entry<Object, PulverizerRecipes.PulverizerRecipe> entry : PulverizerRecipes.INSTANCE.getRecipes().entrySet()) {
            if(entry.getKey() instanceof ItemStack) {
                recipeHelper.registerDisplay(PULVERIZER, new PulverizerDisplay((ItemStack) entry.getKey(), entry.getValue()));
            }else if(entry.getKey() instanceof Tag) {
                for (Item item : ((Tag<Item>) entry.getKey()).values()) {
                    recipeHelper.registerDisplay(PULVERIZER, new PulverizerDisplay(new ItemStack(item), entry.getValue()));
                }
            }
        }
    }
}
