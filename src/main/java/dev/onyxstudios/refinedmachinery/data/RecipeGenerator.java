package dev.onyxstudios.refinedmachinery.data;

import dev.onyxstudios.refinedmachinery.registry.ModBlocks;
import dev.onyxstudios.refinedmachinery.registry.resource.Resource;
import dev.onyxstudios.refinedmachinery.registry.resource.ResourceBuilder;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.*;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Resource resource : ResourceBuilder.resourceList) {
            if(resource.isIngotValid()) {
                if(resource.isBlockValid()) {
                    ShapedRecipeBuilder.shapedRecipe(resource.getBlockObject().get())
                            .key('I', resource.getIngotObject().get())
                            .patternLine("III")
                            .patternLine("III")
                            .patternLine("III")
                            .addCriterion("item", InventoryChangeTrigger.Instance.forItems(resource.getIngotObject().get()))
                            .build(consumer, resource.getBlockObject().getId().toString() + "_crafting");

                    ShapelessRecipeBuilder.shapelessRecipe(resource.getIngotObject().get(), 9)
                            .addIngredient(resource.getBlockObject().get())
                            .addCriterion("item", InventoryChangeTrigger.Instance.forItems(resource.getBlockObject().get()))
                            .build(consumer, resource.getIngotObject().getId().toString() + "_crafting");
                }

                if(resource.isOreValid()) {
                    CookingRecipeBuilder.smeltingRecipe(
                            Ingredient.fromItems(resource.getOreObject().get()),
                            resource.getIngotObject().get(),
                            1,
                            200
                    ).addCriterion("item", InventoryChangeTrigger.Instance.forItems(resource.getOreObject().get()))
                            .build(consumer, resource.getIngotObject().getId().toString() + "_ore_smelting");
                }

                if(resource.isDustValid()) {
                    CookingRecipeBuilder.smeltingRecipe(
                            Ingredient.fromItems(resource.getDustObject().get()),
                            resource.getIngotObject().get(),
                            1,
                            200
                    ).addCriterion("item", InventoryChangeTrigger.Instance.forItems(resource.getDustObject().get()))
                            .build(consumer, resource.getIngotObject().getId().toString() + "_dust_smelting");
                }

                if(resource.isNuggetValid()) {
                    ShapedRecipeBuilder.shapedRecipe(resource.getIngotObject().get())
                            .key('I', resource.getNuggetObject().get())
                            .patternLine("III")
                            .patternLine("III")
                            .patternLine("III")
                            .addCriterion("item", InventoryChangeTrigger.Instance.forItems(resource.getNuggetObject().get()))
                            .build(consumer, resource.getIngotObject().getId().toString() + "_nugget_crafting");

                    ShapelessRecipeBuilder.shapelessRecipe(resource.getNuggetObject().get(), 9)
                            .addIngredient(resource.getIngotObject().get())
                            .addCriterion("item", InventoryChangeTrigger.Instance.forItems(resource.getIngotObject().get()))
                            .build(consumer, resource.getNuggetObject().getId().toString() + "_crafting");
                }
            }
        }

        CookingRecipeBuilder.smeltingRecipe(
                Ingredient.fromItems(ModBlocks.QUARTZ.getDustObject().get()),
                ModBlocks.SILICON.getIngotObject().get(),
                1,
                200)
                .addCriterion("item", InventoryChangeTrigger.Instance.forItems(ModBlocks.QUARTZ.getDustObject().get()))
                .build(consumer);
    }
}
