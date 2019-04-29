package abused_master.refinedmachinery.mixins;

import abused_master.abusedlib.registry.RecipeGenerator;
import abused_master.refinedmachinery.registry.ModRecipes;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeManager.class)
public abstract class MixinRecipeManager {

    @Inject(method = "apply", at = @At("RETURN"))
    public void apply(ResourceManager resourceManager_1, CallbackInfo ci) {
        for (RecipeGenerator.Output output : ModRecipes.registerRecipes().getRecipes()) {
            this.add(RecipeManager.deserialize(output.name, output.recipe));
        }
    }

    @Shadow
    public abstract void add(Recipe<?> recipe_1);
}
