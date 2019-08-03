package abused_master.refinedmachinery.registry.recipe;

import abused_master.refinedmachinery.RefinedMachinery;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PulverizerRecipe implements Recipe<Inventory> {

    protected RecipeType<?> recipeType;
    protected Identifier ID;
    protected String group;
    protected Ingredient input;
    protected ItemStack result, randomOutput;
    protected int randomOutputChance;

    public PulverizerRecipe(Identifier id, String group, Ingredient input, ItemStack result, ItemStack randomOutput, int randomOutputChance) {
        this.recipeType = RefinedMachinery.PULVERIZER_TYPE;
        this.ID = id;
        this.group = group;
        this.input = input;
        this.result = result;
        this.randomOutput = randomOutput;
        this.randomOutputChance = randomOutputChance;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if(input.test(inventory.getInvStack(0))) {
            return true;
        }

        return false;
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return this.result.copy();
    }

    @Override
    public boolean fits(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.result;
    }

    @Override
    public Identifier getId() {
        return this.ID;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RefinedMachinery.PULVERIZER_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return this.recipeType;
    }

    @Override
    public DefaultedList<Ingredient> getPreviewInputs() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.input);
        return list;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getRandomOutput() {
        return randomOutput;
    }

    public int getRandomOutputChance() {
        return randomOutputChance;
    }
}
